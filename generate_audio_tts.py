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
            # 匹配 Triple("猫", "Cat", "喵喵叫...") 格式
            pattern = r'Triple\("([^"]+)",\s*"([^"]+)",\s*"([^"]+)"\)'
            matches = re.findall(pattern, content)
            
            # 模拟 cnToRes 函数逻辑
            def get_res_name(cn):
                mapping = {
                    "苹果": "apple", "香蕉": "banana", "橙子": "orange", "葡萄": "grape",
                    "西瓜": "watermelon", "草莓": "strawberry", "菠萝": "pineapple", "芒果": "mango",
                    "梨": "pear", "桃子": "peach", "樱桃": "cherry", "蓝莓": "blueberry",
                    "猕猴桃": "kiwi", "柠檬": "lemon", "火龙果": "dragonfruit", "哈密瓜": "melon",
                    "荔枝": "lychee", "椰子": "coconut", "石榴": "pomegranate", "柿子": "persimmon",
                    "山竹": "mangosteen", "柚子": "pomelo", "木瓜": "papaya", "杏子": "apricot",
                    "李子": "plum", "无花果": "fig", "杨桃": "starfruit", "榴莲": "durian",
                    "覆盆子": "raspberry", "胡萝卜": "carrot", "白菜": "cabbage", "西红柿": "tomato",
                    "西兰花": "broccoli", "土豆": "potato", "黄瓜": "cucumber", "茄子": "eggplant",
                    "玉米": "corn", "南瓜": "pumpkin", "洋葱": "onion", "大蒜": "garlic",
                    "辣椒": "chili", "蘑菇": "mushroom", "豌豆": "pea", "菠菜": "spinach",
                    "芹菜": "celery", "萝卜": "radish", "红薯": "sweet_potato", "苦瓜": "bitter_gourd",
                    "丝瓜": "luffa", "芦笋": "asparagus", "甜椒": "bell_pepper", "菜花": "cauliflower",
                    "豆角": "green_bean", "莲藕": "lotus_root", "竹笋": "bamboo_shoot", "山药": "yam",
                    "冬瓜": "wax_gourd", "荷兰豆": "snow_pea", "生菜": "lettuce", "汽车": "car",
                    "公交车": "bus", "飞机": "airplane", "自行车": "bicycle", "摩托车": "motorcycle",
                    "火车": "train", "高铁": "high_speed_train", "轮船": "ship", "潜水艇": "submarine",
                    "直升机": "helicopter", "救护车": "ambulance", "消防车": "firetruck", "警车": "police_car",
                    "卡车": "truck", "拖拉机": "tractor", "热气球": "hot_air_balloon", "飞船": "spaceship",
                    "坦克": "tank", "挖掘机": "excavator", "帆船": "sailboat", "出租车": "taxi",
                    "吊车": "crane", "缆车": "cable_car", "划艇": "canoe", "飞艇": "airship",
                    "三轮车": "tricycle", "滑板": "skateboard", "压路机": "steam_roller", "垃圾车": "garbage_truck",
                    "拖船": "tugboat", "叉车": "forklift", "赛车": "racing_car", "房车": "rv",
                    "直升飞机": "chopper", "电动车": "ebike", "铅笔": "pencil", "杯子": "cup",
                    "书本": "book", "书包": "schoolbag", "牙刷": "toothbrush", "毛巾": "towel",
                    "梳子": "comb", "镜子": "mirror", "伞": "umbrella", "帽子": "hat",
                    "鞋子": "shoes", "衣服": "clothes", "床": "bed", "椅子": "chair",
                    "桌子": "desk", "灯": "lamp", "电视": "tv", "手机": "phone",
                    "电脑": "computer", "钟表": "clock", "剪刀": "scissors", "肥皂": "soap",
                    "盆": "basin", "拖鞋": "slippers", "袜子": "socks", "碗": "bowl",
                    "勺子": "spoon", "筷子": "chopsticks", "叉子": "fork", "锅": "pot",
                    "冰箱": "fridge", "洗衣机": "washing_machine", "空调": "air_conditioner", "风扇": "fan",
                    "吹风机": "hairdryer", "钥匙": "key", "钱包": "wallet", "纸巾": "tissue",
                    "玩具熊": "teddy_bear", "积木": "blocks", "太阳": "sun", "月亮": "moon",
                    "星星": "star", "云朵": "cloud", "彩虹": "rainbow", "雨": "rain",
                    "雪": "snow", "风": "wind", "雷电": "lightning", "大山": "mountain",
                    "大海": "ocean", "森林": "forest", "花朵": "flower", "草地": "grass",
                    "树木": "tree", "河流": "river", "湖泊": "lake", "火": "fire",
                    "岩石": "rock", "岛屿": "island", "面包": "bread", "牛奶": "milk",
                    "鸡蛋": "egg", "蛋糕": "cake", "饼干": "cookie", "糖果": "candy",
                    "冰淇淋": "ice_cream", "果汁": "juice", "水": "water", "汉堡": "burger",
                    "薯条": "fries", "披萨": "pizza", "面条": "noodles", "米饭": "rice",
                    "包子": "steamed_bun", "饺子": "dumpling", "巧克力": "chocolate", "甜甜圈": "donut",
                    "三明治": "sandwich", "寿司": "sushi", "汤": "soup", "蜂蜜": "honey",
                    "奶酪": "cheese", "爆米花": "popcorn", "棒棒糖": "lollipop", "眼睛": "eyes",
                    "鼻子": "nose", "嘴巴": "mouth", "耳朵": "ears", "头发": "hair",
                    "手": "hand", "脚": "foot", "胳膊": "arm", "腿": "leg",
                    "头": "head", "手指": "finger", "牙齿": "teeth", "舌头": "tongue",
                    "肩膀": "shoulder", "肚子": "tummy", "猫": "cat", "狗": "dog",
                    "兔子": "rabbit", "小鸟": "bird", "大象": "elephant", "老虎": "tiger",
                    "狮子": "lion", "长颈鹿": "giraffe", "斑马": "zebra", "猴子": "monkey",
                    "熊猫": "panda", "考拉": "koala", "企鹅": "penguin", "猪": "pig",
                    "牛": "cow", "羊": "sheep", "马": "horse", "鸡": "chicken",
                    "鸭": "duck", "熊": "bear", "狐狸": "fox", "鹿": "deer",
                    "刺猬": "hedgehog", "松鼠": "squirrel", "骆驼": "camel", "蛇": "snake",
                    "鳄鱼": "crocodile", "乌龟": "turtle", "青蛙": "frog", "蝴蝶": "butterfly",
                    "蜜蜂": "bee", "瓢虫": "ladybug", "螃蟹": "crab", "龙虾": "lobster",
                    "章鱼": "octopus", "鲸鱼": "whale", "海豚": "dolphin", "鲨鱼": "shark",
                    "海马": "seahorse", "水母": "jellyfish"
                }
                return mapping.get(cn, "ic_placeholder_default")

            for cn, en, desc in matches:
                items.append({
                    "name_cn": cn,
                    "name_en": en,
                    "description_cn": desc,
                    "res_name": get_res_name(cn)
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
    """生成系统手动定义的音效及分类名称"""
    manual_items = [
        {"text": "答对了！", "file": "correct.mp3", "voice": VOICE_CN},
        {"text": "答错了，再试一次吧", "file": "wrong.mp3", "voice": VOICE_CN},
        {"text": "游戏结束", "file": "game_over.mp3", "voice": VOICE_CN},
        {"text": "获得新成就", "file": "achievement.mp3", "voice": VOICE_CN},
        {"text": "动物世界", "file": "cat_animals.mp3", "voice": VOICE_CN},
        {"text": "美味水果", "file": "cat_fruits.mp3", "voice": VOICE_CN},
        {"text": "新鲜蔬菜", "file": "cat_vegetables.mp3", "voice": VOICE_CN},
        {"text": "交通工具", "file": "cat_transport.mp3", "voice": VOICE_CN},
        {"text": "日常用品", "file": "cat_daily.mp3", "voice": VOICE_CN},
        {"text": "自然现象", "file": "cat_nature.mp3", "voice": VOICE_CN},
        {"text": "食物与饮料", "file": "cat_food.mp3", "voice": VOICE_CN},
        {"text": "身体部位", "file": "cat_body.mp3", "voice": VOICE_CN},
        {"text": "这是谁的影子呢？", "file": "shadow_prompt.mp3", "voice": VOICE_CN},
        {"text": "太棒了，你全都答对啦！你是识物小天才！", "file": "perfect_score.mp3", "voice": VOICE_CN},
        {"text": "背景音乐正在播放，换成你喜欢的儿歌吧！", "file": "bgm_main.mp3", "voice": VOICE_CN}
    ]
    
    print("正在生成系统音效及分类语音...")
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

        # 生成中文音频 (名称)
        cn_file = f"{item['res_name']}_cn.mp3"
        cn_path = os.path.join(OUTPUT_DIR, cn_file)
        
        # 生成中文音频 (描述/谜语)
        desc_cn_file = f"{item['res_name']}_desc_cn.mp3"
        desc_cn_path = os.path.join(OUTPUT_DIR, desc_cn_file)

        # 生成英文音频
        en_file = f"{item['res_name']}_en.mp3"
        en_path = os.path.join(OUTPUT_DIR, en_file)

        # 中文处理 (名称)
        if not os.path.exists(cn_path):
            if args.engine == "edge":
                if await generate_with_edge_tts(item['name_cn'], VOICE_CN, cn_path):
                    success_count += 1
            else:
                if generate_with_openai_tts(item['name_cn'], args.model, "nova", cn_path):
                    success_count += 1
        
        # 中文处理 (描述)
        if not os.path.exists(desc_cn_path):
            if args.engine == "edge":
                if await generate_with_edge_tts(item['description_cn'], VOICE_CN, desc_cn_path):
                    success_count += 1
            else:
                if generate_with_openai_tts(item['description_cn'], args.model, "nova", desc_cn_path):
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
