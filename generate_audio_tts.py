import os
import re
import asyncio
import argparse
import requests
from tqdm import tqdm
import edge_tts

# 项目路径配置
INITIALIZER_PATH = "app/src/main/java/com/clouditemapp/data/initializer/DataInitializer.kt"
OUTPUT_DIR = "app/src/main/res/raw"

# TTS 配置 (Edge-TTS 默认音色)
# 推荐：Xiaoxiao (温柔女声), Yunxi (活泼男童), Xiaoyi (亲切女声)
VOICE_CN = "zh-CN-XiaoxiaoNeural" 
# 推荐：Emma (亲切女声), Ana (活泼女声)
VOICE_EN = "en-US-EmmaNeural"

# OpenAI TTS 配置 (可选)
API_KEY = os.getenv("GOOGLE_API_KEY") # 复用之前的 Key 变量名，或自定义
API_BASE_URL = os.getenv("API_BASE_URL")

def parse_items(file_path):
    """从 Kotlin 文件中解析物品列表"""
    items = []
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
            pattern = r'Triple\("([^"]+)",\s*"([^"]+)",\s*"([^"]+)"\)'
            matches = re.findall(pattern, content)
            
            for cn, en, res_name in matches:
                items.append({
                    "name_cn": cn,
                    "name_en": en,
                    "res_name": res_name
                })
        print(f"成功解析 {len(items)} 个物品。")
        return items
    except Exception as e:
        print(f"解析文件失败: {e}")
        return []

async def generate_with_edge_tts(text, voice, output_path):
    """使用免费的 Edge-TTS 生成音频"""
    try:
        communicate = edge_tts.Communicate(text, voice)
        await communicate.save(output_path)
        return True
    except Exception as e:
        print(f"Edge-TTS 生成失败: {e}")
        return False

def generate_with_openai_tts(text, model, voice, output_path):
    """使用 OpenAI 兼容格式调用 TTS API"""
    if not API_KEY or not API_BASE_URL:
        return False
        
    url = f"{API_BASE_URL}/v1/audio/speech"
    headers = {
        "Authorization": f"Bearer {API_KEY}",
        "Content-Type": "application/json"
    }
    payload = {
        "model": model,
        "input": text,
        "voice": voice # alloy, echo, fable, onyx, nova, shimmer
    }

    try:
        response = requests.post(url, headers=headers, json=payload, timeout=30)
        response.raise_for_status()
        with open(output_path, 'wb') as f:
            f.write(response.content)
        return True
    except Exception as e:
        print(f"OpenAI TTS 调用失败: {e}")
        return False

async def generate_manual_sounds():
    """生成系统手动定义的音效"""
    manual_items = [
        {"text": "答对了！", "file": "correct.mp3", "voice": VOICE_CN},
        {"text": "答错了，再试一次吧", "file": "wrong.mp3", "voice": VOICE_CN},
        {"text": "游戏结束", "file": "game_over.mp3", "voice": VOICE_CN},
        {"text": "获得新成就", "file": "achievement.mp3", "voice": VOICE_CN}
    ]
    
    print("正在生成系统音效...")
    for item in manual_items:
        output_path = os.path.join(OUTPUT_DIR, item['file'])
        if not os.path.exists(output_path):
            await generate_with_edge_tts(item['text'], item['voice'], output_path)

async def main():
    parser = argparse.ArgumentParser(description="批量生成游戏音频资源")
    parser.add_argument("--engine", type=str, default="edge", choices=["edge", "openai"], help="使用的 TTS 引擎")
    parser.add_argument("--model", type=str, default="tts-1", help="OpenAI TTS 模型")
    parser.add_argument("--limit", type=int, default=0, help="限制生成的数量")
    args = parser.parse_args()

    # 先生成手动定义的音效
    await generate_manual_sounds()

    items = parse_items(INITIALIZER_PATH)
    if not items:
        return

    if not os.path.exists(OUTPUT_DIR):
        os.makedirs(OUTPUT_DIR)

    print(f"开始使用 {args.engine} 引擎生成音频...")
    print(f"输出目录: {OUTPUT_DIR}")

    count = 0
    success_count = 0

    for item in tqdm(items):
        if args.limit > 0 and count >= args.limit:
            break

        # 生成中文音频
        cn_file = f"{item['res_name']}_cn.mp3"
        cn_path = os.path.join(OUTPUT_DIR, cn_file)
        
        # 生成英文音频
        en_file = f"{item['res_name']}_en.mp3"
        en_path = os.path.join(OUTPUT_DIR, en_file)

        # 中文处理
        if not os.path.exists(cn_path):
            if args.engine == "edge":
                if await generate_with_edge_tts(item['name_cn'], VOICE_CN, cn_path):
                    success_count += 1
            else:
                if generate_with_openai_tts(item['name_cn'], args.model, "nova", cn_path):
                    success_count += 1
        
        # 英文处理
        if not os.path.exists(en_path):
            if args.engine == "edge":
                if await generate_with_edge_tts(item['name_en'], VOICE_EN, en_path):
                    success_count += 1
            else:
                if generate_with_openai_tts(item['name_en'], args.model, "shimmer", en_path):
                    success_count += 1
            
        count += 1

    print(f"完成! 成功生成音频文件。")

if __name__ == "__main__":
    asyncio.run(main())
