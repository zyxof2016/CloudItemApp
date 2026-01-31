# -*- coding: utf-8 -*-
"""
云朵识物乐园 - 批量生成物品名称读音 MP3（中文/英文）。

依赖: pip install gTTS
用法:
  python generate_audio.py                    # 生成全部 235*2 个文件到 generated_audio/
  python generate_audio.py --limit 5          # 仅生成前 5 个物品（测试用）
  python generate_audio.py --out ../app/src/main/res/raw   # 直接输出到 res/raw

生成完成后，将 generated_audio/*.mp3 复制到 app/src/main/res/raw/
（Android raw 资源名仅允许小写字母、数字、下划线，如 cat_cn.mp3）
"""

import argparse
import os
import sys

try:
    from gtts import gTTS
except ImportError:
    print("请先安装 gTTS: pip install gTTS")
    sys.exit(1)

from items_data import ITEMS

DEFAULT_OUT = "generated_audio"


def safe_filename(res: str) -> str:
    """确保文件名符合 Android raw 命名：小写、数字、下划线。"""
    return res.strip().lower().replace(" ", "_").replace("-", "_")


def generate_one(text: str, lang: str, path: str) -> bool:
    try:
        tts = gTTS(text=text, lang=lang, slow=False)
        tts.save(path)
        return True
    except Exception as e:
        print("  [FAIL] %s: %s" % (path, e))
        return False


def main():
    parser = argparse.ArgumentParser(description="生成云朵识物乐园物品名称读音 MP3")
    parser.add_argument("--out", default=DEFAULT_OUT, help="输出目录（默认 generated_audio）")
    parser.add_argument("--limit", type=int, default=None, help="仅生成前 N 个物品（测试用）")
    parser.add_argument("--cn-only", action="store_true", help="仅生成中文")
    parser.add_argument("--en-only", action="store_true", help="仅生成英文")
    args = parser.parse_args()

    os.makedirs(args.out, exist_ok=True)
    items = ITEMS[: args.limit] if args.limit else ITEMS
    do_cn = not args.en_only
    do_en = not args.cn_only

    ok, fail = 0, 0
    for i, (name_cn, name_en, res) in enumerate(items):
        base = safe_filename(res)
        if do_cn:
            path_cn = os.path.join(args.out, "%s_cn.mp3" % base)
            if generate_one(name_cn, "zh-cn", path_cn):
                ok += 1
            else:
                fail += 1
        if do_en:
            path_en = os.path.join(args.out, "%s_en.mp3" % base)
            if generate_one(name_en, "en", path_en):
                ok += 1
            else:
                fail += 1
        if (i + 1) % 20 == 0:
            print("已处理 %d / %d 个物品..." % (i + 1, len(items)))

    total = len(items) * (2 if (do_cn and do_en) else 1)
    print("完成: 成功 %d, 失败 %d (共 %d 个文件)" % (ok, fail, total))
    if args.out == DEFAULT_OUT and ok > 0:
        print("请将 %s/*.mp3 复制到 app/src/main/res/raw/" % args.out)


if __name__ == "__main__":
    main()
