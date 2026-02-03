Batch generate drawable images from PROMPTS.md

Prerequisites:
- Python 3.8+ (recommended)
- Internet access
- An image generation API key (OpenAI API key recommended by default)

How it works:
- Parses PROMPTS.md to extract items: name (Chinese), slug (English), and prompt.
- Appends a core styling suffix to each prompt to ensure consistent visuals.
- Calls the API to generate an image and saves it to app/src/main/res/drawable as <slug>.png

Usage:
- Update scripts/config.json with your API key and preferred settings.
- Install dependencies: `pip install -r scripts/requirements.txt`
- Run: `python scripts/generate_drawable_images.py`
- Images will be saved in: `app/src/main/res/drawable/`

Notes:
- If a slug already exists, the script will create a unique filename by appending _2, _3, …
- You can easily switch API provider by modifying the provider logic in the script.
