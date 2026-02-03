#!/usr/bin/env python3
"""
Generate cat image for CloudItemApp
"""

import os
import requests
from pathlib import Path

# Configuration
OUTPUT_DIR = Path("../app/src/main/res/drawable")
IMAGE_NAME = "cat"
PROMPT = "A cute fluffy ginger cat with big eyes, children's educational illustration, cute 3D clay style, bright and vibrant colors, soft studio lighting, high resolution, isolated on white background, rounded edges, friendly appearance, masterpiece, high detail, --ar 1:1"


def load_api_key():
    """Load API key from environment or user input"""
    api_key = os.getenv("OPENAI_API_KEY")
    if not api_key:
        print("OpenAI API key not found in environment variables.")
        print("Please enter your OpenAI API key to generate the cat image:")
        api_key = input("Enter your OpenAI API key: ").strip()
        if not api_key:
            print("Error: API key is required")
            exit(1)
    return api_key


def ensure_output_dir():
    """Ensure the output directory exists"""
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    print(f"Output directory: {OUTPUT_DIR.absolute()}")


def generate_image(api_key):
    """Generate cat image using OpenAI API"""
    output_path = OUTPUT_DIR / f"{IMAGE_NAME}.png"
    
    # Skip if image already exists
    if output_path.exists():
        print(f"Image {IMAGE_NAME}.png already exists. Skipping generation.")
        return True
    
    print(f"Generating {IMAGE_NAME}.png...")
    
    try:
        # API request to OpenAI
        headers = {
            "Content-Type": "application/json",
            "Authorization": f"Bearer {api_key}"
        }
        
        payload = {
            "model": "dall-e-3",
            "prompt": PROMPT,
            "size": "1024x1024",
            "quality": "standard",
            "n": 1
        }
        
        response = requests.post(
            "https://api.openai.com/v1/images/generations",
            headers=headers,
            json=payload
        )
        
        response.raise_for_status()
        data = response.json()
        
        # Get image URL from response
        image_url = data["data"][0]["url"]
        print(f"Image generated successfully! URL: {image_url}")
        
        # Download and save the image
        img_response = requests.get(image_url)
        img_response.raise_for_status()
        
        with open(output_path, 'wb') as f:
            f.write(img_response.content)
        
        print(f"Image saved to: {output_path.absolute()}")
        return True
        
    except Exception as e:
        print(f"Error generating image: {str(e)}")
        return False


def main():
    """Main function"""
    print("=== CloudItemApp Cat Image Generator ===")
    print("This script will generate a cute cat image for the app.")
    print("\nNote: You need an OpenAI API key to use this script.")
    print("Image generation with DALL-E 3 costs approximately $0.04 per image.")
    print("\n" + "="*50)
    
    # Load API key
    api_key = load_api_key()
    
    # Ensure output directory exists
    ensure_output_dir()
    
    # Generate image
    if generate_image(api_key):
        print("\n✅ Cat image generated successfully!")
        print(f"The image has been saved to: {OUTPUT_DIR.absolute()}/{IMAGE_NAME}.png")
    else:
        print("\n❌ Failed to generate cat image.")


if __name__ == "__main__":
    main()
