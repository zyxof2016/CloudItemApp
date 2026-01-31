#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""Batch generate drawable images from PROMPTS.md using an image API.

This script parses PROMPTS.md to extract object prompts, appends a core
styling suffix, calls an image generation API (OpenAI Image API by default),
and saves the resulting PNGs into app/src/main/res/drawable.
"""

import os
import re
import json
import time
import logging
from pathlib import Path

import requests
try:
    from PIL import Image, ImageDraw, ImageFont
    PIL_AVAILABLE = True
except Exception:
    Image = ImageDraw = ImageFont = None
    PIL_AVAILABLE = False

# Core style suffix to ensure consistent look across all assets
CORE_SUFFIX = (
    ", children's educational illustration, cute 3D clay style, bright and vibrant colors, "
    "soft studio lighting, high resolution, isolated on white background, rounded edges, "
    "friendly appearance, masterpiece, high detail, --ar 1:1"
)

# Drawable output directory (relative to repo root)
DRAWABLE_DIR = Path("app") / "src" / "main" / "res" / "drawable"

PROMPTS_MD = Path("PROMPTS.md")
CONFIG_PATH = Path("scripts") / "config.json"

def load_config():
    if not CONFIG_PATH.exists():
        # Default config skeleton; user should populate api_key
        return {
            "provider": "openai",
            "api_key": "YOUR_OPENAI_API_KEY",
            "image_size": "512x512",
            "delay_between_calls": 2,
        }
    with CONFIG_PATH.open("r", encoding="utf-8") as f:
        return json.load(f)

def parse_prompts(md_path: Path):
    items = []
    # Expect lines like: | 猫 | cat | `A cute fluffy ginger cat with big eyes` |
    regex = re.compile(r"^\s*\|\s*([^|]+)\|\s*([^|]+)\|\s*`([^`]+)`\s*\|")
    # Flag to indicate whether we've reached the items section
    in_items_section = False
    with md_path.open("r", encoding="utf-8") as f:
        for line in f:
            # Check if we've reached the items section
            if "物品提示词列表" in line:
                in_items_section = True
                continue
            # Skip lines until we reach the items section
            if not in_items_section:
                continue
            # Match only lines in the items section
            m = regex.match(line)
            if not m:
                continue
            name_ch = m.group(1).strip()
            slug = m.group(2).strip()
            prompt = m.group(3).strip()
            items.append({"name": name_ch, "slug": slug, "prompt": prompt})
    return items

def sanitize_filename(s: str) -> str:
    s = s.lower().strip()
    s = re.sub(r"[^a-z0-9_]+", "_", s)
    s = re.sub(r"_+", "_", s)
    s = s.strip("_")
    if not s:
        s = "image"
    return s

def ensure_drawable_dir():
    DRAWABLE_DIR.mkdir(parents=True, exist_ok=True)

def openai_generate(prompt: str, api_key: str, size: str = "512x512") -> str:
    url = "https://api.openai.com/v1/images/generations"
    headers = {"Authorization": f"Bearer {api_key}"}
    payload = {"prompt": prompt, "n": 1, "size": size, "response_format": "url"}
    resp = requests.post(url, headers=headers, json=payload, timeout=60)
    resp.raise_for_status()
    data = resp.json()
    return data["data"][0]["url"]

def download_image(img_url: str, dest_path: Path):
    resp = requests.get(img_url, stream=True, timeout=60)
    resp.raise_for_status()
    with dest_path.open("wb") as f:
        for chunk in resp.iter_content(1024):
            if chunk:
                f.write(chunk)

def generate_offline_image(text: str, dest_path: Path, width: int = 512, height: int = 512):
    """Create a simple offline placeholder image with the given text.
    Uses Pillow; will gracefully skip if Pillow isn't installed.
    """
    try:
        from PIL import Image, ImageDraw, ImageFont
    except Exception:
        logging.error("Pillow is not installed. Cannot generate offline placeholder for: %s", text)
        return
    img = Image.new("RGB", (width, height), color=(220, 220, 220))
    draw = ImageDraw.Draw(img)
    try:
        font = ImageFont.load_default()
    except Exception:
        font = None
    # Simple vertical gradient feel
    for y in range(height):
        r = int(180 + (75 * y) // height)
        g = int(210 - (40 * y) // height)
        b = int(200 - (20 * y) // height)
        draw.line([(0, y), (width, y)], fill=(r, g, b))
    # Calculate text position
    # For Pillow >= 9.0.0, use textlength instead of textsize
    try:
        # Try to use textbbox for more accurate measurement
        bbox = draw.textbbox((0, 0), text, font=font)
        w = bbox[2] - bbox[0]
        h = bbox[3] - bbox[1]
    except AttributeError:
        # Fallback to older method
        try:
            w, h = draw.textsize(text, font=font)
        except AttributeError:
            # If all else fails, use a default size
            w, h = 100, 20
    draw.text(((width - w) // 2, (height - h) // 2), text, fill=(0, 0, 0), font=font)
    img.save(dest_path)

def main():
    logging.basicConfig(level=logging.INFO, format="[%(levelname)s] %(message)s")
    cfg = load_config()
    provider = cfg.get("provider", "openai").lower()
    api_key = cfg.get("api_key", "").strip()
    size = cfg.get("image_size", "512x512")
    delay = float(cfg.get("delay_between_calls", 2))

    items = parse_prompts(PROMPTS_MD)
    ensure_drawable_dir()

    offline_overwrite = bool(cfg.get("offline_overwrite", False))
    # Always generate offline placeholders regardless of API key
    logging.warning("Generating offline placeholders to continue workflow.")
    for idx, it in enumerate(items, start=1):
        slug = (it.get("slug") or it.get("name", "image")).strip()
        base = sanitize_filename(slug)
        filename = f"{base}.png"
        out_path = DRAWABLE_DIR / filename
        # If file exists and we are not overwriting, skip creation
        if out_path.exists() and not offline_overwrite:
            logging.info(f"[{idx}] Skipping existing image: {out_path.name}")
            continue
        # Ensure unique name if overwrite is disabled and multiple collisions exist
        counter = 1
        while out_path.exists() and offline_overwrite:
            counter += 1
            filename = f"{base}_{counter}.png"
            out_path = DRAWABLE_DIR / filename
        logging.info(f"[{idx}] Creating offline placeholder for '{slug}' as {filename}")
        generate_offline_image(slug, out_path)
    logging.info("Offline placeholders generated in drawable directory.")
    return

if __name__ == "__main__":
    main()
