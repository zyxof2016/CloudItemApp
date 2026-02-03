#!/bin/bash

# 云朵识物乐园 - 资源检查脚本

RES_DIR="app/src/main/res"
INITIALIZER="app/src/main/java/com/clouditemapp/data/initializer/DataInitializer.kt"

echo "开始检查资源..."

# 提取所有在 DataInitializer 中定义的资源名
RESOURCES=$(grep -oE 'Triple\("[^"]+", "[^"]+", "[^"]+"\)' $INITIALIZER | cut -d'"' -f6)

MISSING_IMAGES=0
MISSING_AUDIO_CN=0
MISSING_AUDIO_EN=0

for res in $RESOURCES; do
    # 检查图片
    if [ ! -f "$RES_DIR/drawable/$res.png" ] && [ ! -f "$RES_DIR/drawable/$res.jpg" ] && [ ! -f "$RES_DIR/drawable/$res.webp" ] && [ ! -f "$RES_DIR/drawable/$res.xml" ]; then
        echo "[缺失图片] $res"
        MISSING_IMAGES=$((MISSING_IMAGES + 1))
    fi

    # 检查中文音频
    if [ ! -f "$RES_DIR/raw/${res}_cn.mp3" ] && [ ! -f "$RES_DIR/raw/${res}_cn.wav" ]; then
        echo "[缺失音频-CN] ${res}_cn"
        MISSING_AUDIO_CN=$((MISSING_AUDIO_CN + 1))
    fi

    # 检查英文音频
    if [ ! -f "$RES_DIR/raw/${res}_en.mp3" ] && [ ! -f "$RES_DIR/raw/${res}_en.wav" ]; then
        echo "[缺失音频-EN] ${res}_en"
        MISSING_AUDIO_EN=$((MISSING_AUDIO_EN + 1))
    fi
done

echo "--------------------------------"
echo "检查完成！"
echo "缺失图片: $MISSING_IMAGES"
echo "缺失中文音频: $MISSING_AUDIO_CN"
echo "缺失英文音频: $MISSING_AUDIO_EN"
echo "--------------------------------"
echo "提示：请按照 PROMPTS.md 中的指南生成资源并放入对应目录。"
