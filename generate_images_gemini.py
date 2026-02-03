import os
import re
import time
import argparse
import requests
import json
import base64
from PIL import Image
from io import BytesIO
from tqdm import tqdm

# 配置
# 请设置环境变量 GOOGLE_API_KEY，或在此处直接填入（不推荐）
API_KEY = os.getenv("GOOGLE_API_KEY")
# 默认 API 基础地址 (Google 官方)
DEFAULT_API_BASE = "https://generativelanguage.googleapis.com"
# 从环境变量获取 API 基础地址 (用于支持第三方代理)
API_BASE_URL = os.getenv("API_BASE_URL", DEFAULT_API_BASE).rstrip('/')

# 目标分辨率
TARGET_SIZE = (512, 512)

# 主提示词模板 (Master Prompt)
MASTER_PROMPT_TEMPLATE = (
    "{subject}, children's educational illustration, cute 3D clay style, "
    "bright and vibrant colors, soft studio lighting, high resolution, "
    "isolated on pure white background, rounded edges, friendly appearance, "
    "masterpiece, high detail"
)

# 项目路径配置
INITIALIZER_PATH = "app/src/main/java/com/clouditemapp/data/initializer/DataInitializer.kt"
OUTPUT_DIR = "app/src/main/res/drawable"

def setup_api():
    if not API_KEY:
        print("错误: 未找到 GOOGLE_API_KEY 环境变量。")
        print("请运行: export GOOGLE_API_KEY='your_api_key'")
        return False
    return True

def parse_items(file_path):
    """从 Kotlin 文件中解析物品列表"""
    items = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            # 匹配 Triple("中文", "英文", "资源名")
            # 示例: Triple("猫", "Cat", "cat")
            pattern = r'Triple\("([^"]+)",\s*"([^"]+)",\s*"([^"]+)"\)'
            matches = re.findall(pattern, content)
            
            for cn, en, res_name in matches:
                # 简单的英文描述构建
                subject = f"A cute {en.lower()}"
                items.append({
                    "name_cn": cn,
                    "name_en": en,
                    "res_name": res_name,
                    "subject": subject
                })
        print(f"成功解析 {len(items)} 个物品。")
        return items
    except Exception as e:
        print(f"解析文件失败: {e}")
        return []

def generate_image_openai_compatible(model_name, prompt, output_path):
    """使用 OpenAI 兼容格式 (v1/images/generations) 调用 API"""
    url = f"{API_BASE_URL}/v1/images/generations"
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": model_name,
        "prompt": prompt,
        "n": 1,
        "size": "1024x1024", # 大多数 DALL-E 兼容接口默认支持此尺寸
        "response_format": "b64_json"
    }

    try:
        response = requests.post(url, headers=headers, json=payload, timeout=60)
        response.raise_for_status()
        result = response.json()
        
        # 解析返回的 Base64 图片
        image_b64 = result['data'][0]['b64_json']
        image_data = base64.b64decode(image_b64)
        
        image = Image.open(BytesIO(image_data))
        image = image.resize(TARGET_SIZE, Image.Resampling.LANCZOS)
        image.save(output_path, "PNG")
        return True
        
    except Exception as e:
        print(f"OpenAI 兼容模式调用失败: {e}")
        if 'response' in locals():
            print(f"响应内容: {response.text}")
        return False

def generate_image_google_rest(model_name, prompt, output_path):
    """使用 Google REST 格式调用 API (适用于 Gemini/Imagen)"""
    # 注意: 不同的代理对路径的处理可能不同。
    # 标准 Gemini 路径: /v1beta/models/{model}:generateContent
    # 标准 Imagen 路径: /v1beta/models/{model}:predict (Vertex AI) 或其他
    
    # 尝试使用 generateContent (Gemini 标准)
    url = f"{API_BASE_URL}/v1beta/models/{model_name}:generateContent?key={API_KEY}"
    
    headers = {"Content-Type": "application/json"}
    
    # 构建 Gemini 请求体
    payload = {
        "contents": [{
            "parts": [{"text": prompt}]
        }],
        "generationConfig": {
            "temperature": 0.9,
            "topK": 40,
            "topP": 0.95,
            "maxOutputTokens": 1024,
        }
    }

    try:
        response = requests.post(url, headers=headers, json=payload, timeout=60)
        response.raise_for_status()
        result = response.json()
        
        # 尝试从 Gemini 响应中提取图片
        # 注意：目前的 Gemini Pro Vision (1.0/1.5) 主要用于输入图片。
        # 如果是 Imagen 模型，返回结构完全不同。
        # 如果代理将 DALL-E 风格的请求转发给 Imagen，请使用 generate_image_openai_compatible
        
        # 这里仅作简单的文本响应检查，因为标准的 Gemini 1.5 Pro 文本 API 不直接返回图片二进制流
        # 除非代理做了特殊封装返回 Base64
        print("警告: 使用 Google REST 格式调用 Gemini 文本接口可能无法直接获得图片。")
        print("如果您的代理支持 OpenAI 格式 (v1/images/generations)，请使用 --use-openai-format 参数。")
        print(f"API 响应片段: {str(result)[:200]}")
        return False

    except Exception as e:
        print(f"Google REST 模式调用失败: {e}")
        return False

def generate_text_placeholder(item, output_path):
    """生成带有文字的占位图"""
    try:
        # 创建一个带有渐变背景的图片
        img = Image.new('RGB', TARGET_SIZE, color=(240, 240, 240))
        from PIL import ImageDraw, ImageFont
        
        draw = ImageDraw.Draw(img)
        
        # 尝试加载字体，如果失败则使用默认字体
        # 在 Manjaro/Linux 系统上常见的字体路径
        font_paths = [
            "/usr/share/fonts/noto-cjk/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/truetype/noto/NotoSansCJK-Bold.ttc",
            "/usr/share/fonts/TTF/DejaVuSans-Bold.ttf",
            "/usr/share/fonts/liberation/LiberationSans-Bold.ttf"
        ]
        
        font_large = None
        font_small = None
        for path in font_paths:
            if os.path.exists(path):
                try:
                    font_large = ImageFont.truetype(path, 80)
                    font_small = ImageFont.truetype(path, 40)
                    break
                except:
                    continue
        
        if font_large is None:
            font_large = ImageFont.load_default()
            font_small = ImageFont.load_default()

        # 绘制背景装饰
        draw.rectangle([20, 20, 492, 492], outline=(200, 200, 200), width=5)
        
        # 写入中文名称
        text_cn = item['name_cn']
        draw.text((256, 200), text_cn, fill=(50, 50, 50), anchor="mm", font=font_large)
        
        # 写入英文名称
        text_en = item['name_en']
        draw.text((256, 300), text_en, fill=(100, 100, 100), anchor="mm", font=font_small)
        
        # 写入资源ID (底部小字)
        draw.text((256, 450), f"ID: {item['res_name']}", fill=(180, 180, 180), anchor="mm", font=font_small)

        img.save(output_path, "PNG")
        return True
    except Exception as e:
        print(f"生成占位图失败: {e}")
        return False

def main():
    parser = argparse.ArgumentParser(description="批量生成游戏资源图片")
    parser.add_argument("--model", type=str, default="dall-e-3", help="使用的模型名称 (例如 dall-e-3, gemini-pro-vision, etc)")
    parser.add_argument("--limit", type=int, default=0, help="限制生成的数量 (0为不限制)")
    parser.add_argument("--category", type=str, help="只生成特定分类")
    parser.add_argument("--use-openai-format", action="store_true", default=True, help="使用 OpenAI 兼容 API 格式 (默认开启，适用于大多数代理)")
    parser.add_argument("--use-google-format", action="store_true", help="使用 Google 原生 REST API 格式")
    parser.add_argument("--placeholder-only", action="store_true", help="仅生成文字占位图，不调用API")
    
    args = parser.parse_args()

    # 只有在非占位模式下才检查 API
    if not args.placeholder_only:
        if not setup_api():
            return

    items = parse_items(INITIALIZER_PATH)
    if not items:
        return

    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    count = 0
    success_count = 0
    
    if args.placeholder_only:
        print("开始生成文字占位图...")
    else:
        print(f"开始生成图片...")
        print(f"API 地址: {API_BASE_URL}")
        print(f"模型: {args.model}")
        print(f"模式: {'OpenAI 兼容' if not args.use_google_format else 'Google REST'}")
    
    print(f"输出目录: {OUTPUT_DIR}")

    for item in tqdm(items):
        if args.limit > 0 and count >= args.limit:
            break

        file_name = f"{item['res_name']}.png"
        output_path = os.path.join(OUTPUT_DIR, file_name)

        if os.path.exists(output_path):
            # 在生成占位图时，我们可能想跳过已经存在的真实图片
            # 这里默认跳过所有已存在的文件
            count += 1
            continue

        if args.placeholder_only:
            success = generate_text_placeholder(item, output_path)
        else:
            full_prompt = MASTER_PROMPT_TEMPLATE.format(subject=item['subject'])
            
            success = False
            if args.use_google_format:
                success = generate_image_google_rest(args.model, full_prompt, output_path)
            else:
                success = generate_image_openai_compatible(args.model, full_prompt, output_path)

        if success:
            success_count += 1
            if not args.placeholder_only:
                time.sleep(1) 
        else:
            print(f"生成失败: {file_name}")
            
        count += 1

    print(f"完成! 成功生成: {success_count}/{count}")

if __name__ == "__main__":
    main()
