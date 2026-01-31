# 云朵识物乐园 - 资源生成说明

本文档说明如何生成 **图片**、**音频**、**Lottie** 三类资源。

---

## 一、Lottie 动画 ✅ 已就绪

- **loading.json**：护眼休息/加载动画（云朵呼吸效果）
- **celebration.json**：游戏结束庆祝动画

已放在 `app/src/main/res/raw/`，无需再生成。若需替换，可从 [LottieFiles](https://lottiefiles.com/) 下载 JSON 后覆盖同名文件。

---

## 二、图片（235 张 drawable）

### 规范

- **路径**：`app/src/main/res/drawable/`
- **文件名**：与 `imageRes` 一致，小写、下划线，如 `cat.png`、`sweet_potato.png`
- **风格**：见项目根目录 `PROMPTS.md` 中的「核心风格定义」与各物品「提示词」

### 生成方式

#### 方案 1：在 Cursor 中手动生成（推荐）

1. 运行导出脚本生成清单：
   ```bash
   cd scripts
   python export_image_prompts.py
   ```
2. 打开 `scripts/image_prompts_for_cursor.txt`，每项包含「文件名」和「完整提示词」。
3. 在 Cursor 聊天或 Composer 中使用图片生成功能，复制「完整提示词」到生成框。
4. 生成后保存为对应文件名（如 `cat.png`），放入 `app/src/main/res/drawable/`。

#### 其他方式

- 使用 DALL·E、Midjourney、Stable Diffusion 等，按 `PROMPTS.md` 中每行的「Subject Prompt + 统一后缀」生成，导出为 PNG，重命名为 **`{imageRes}.png`** 后放入 `app/src/main/res/drawable/`。

- **统一风格后缀**：
  ```
  , children's educational illustration, cute 3D clay style, bright and vibrant colors, soft studio lighting, high resolution, isolated on white background, rounded edges, friendly appearance, masterpiece, high detail, --ar 1:1
  ```  

---

## 三、音频（470 个 MP3：235 物品 × 中/英）

### 规范

- **路径**：`app/src/main/res/raw/`
- **文件名**：`{imageRes}_cn.mp3`（中文）、`{imageRes}_en.mp3`（英文），如 `cat_cn.mp3`、`cat_en.mp3`
- **内容**：仅读该物品名称，亲切、清晰，适合 2–6 岁儿童

### 使用脚本生成（gTTS）

1. 安装依赖：
   ```bash
   pip install gTTS
   ```

2. 在项目根目录执行：
   ```bash
   cd scripts
   python generate_audio.py
   ```
   会生成到 `scripts/generated_audio/`，文件名已符合 raw 规范。

3. 测试时只生成前几条：
   ```bash
   python generate_audio.py --limit 5
   ```

4. 可选：直接输出到 res/raw（需先建好目录）：
   ```bash
   python generate_audio.py --out ../app/src/main/res/raw
   ```
   否则可手动把 `generated_audio/*.mp3` 复制到 `app/src/main/res/raw/`。

### 使用其他 TTS（如 ElevenLabs）

参考 `PROMPTS.md` 中的「音频生成指南」，用同一批 `(nameCN, nameEN, imageRes)`（见 `items_data.py`）生成 MP3，并按上述命名放入 `res/raw/`。

---

## 四、资源与代码对应关系

| 类型   | 数量        | 命名/位置                    | 代码引用处           |
|--------|-------------|-----------------------------|----------------------|
| 图片   | 235         | `drawable/{imageRes}.png`   | `Item.imageRes`      |
| 中文音 | 235         | `raw/{imageRes}_cn.mp3`     | `Item.audioCN`       |
| 英文音 | 235         | `raw/{imageRes}_en.mp3`     | `Item.audioEN`       |
| Lottie | 2           | `raw/loading.json` 等      | 护眼弹窗、游戏结果页 |

`imageRes` 与 `items_data.py` / `DataInitializer.kt` 中的资源名一致（如 `cat`、`sweet_potato`）。
