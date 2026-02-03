#!/usr/bin/env python3
"""
AI Image Generation Script for CloudItemApp - FREE VERSION
Uses Pollinations.ai (completely free, no API key needed!)
Generates 235 item images based on prompts from PROMPTS.md

Usage:
    python generate_images_free.py                    # Generate all images
    python generate_images_free.py --category animals # Generate specific category
    python generate_images_free.py --test             # Test mode (dry run)
"""

import os
import sys
import time
import urllib.request
import urllib.parse
from pathlib import Path
from typing import List, Tuple, Dict
from concurrent.futures import ThreadPoolExecutor, as_completed
import threading

# Import the items data to get names
from items_data import ITEMS

# Configuration
OUTPUT_DIR = Path("../app/src/main/res/drawable")
STYLE_SUFFIX = ", children's educational illustration, cute 3D clay style, bright and vibrant colors, soft studio lighting, high resolution, isolated on white background, rounded edges, friendly appearance, masterpiece, high detail"

# All prompts from PROMPTS.md organized by category
ALL_PROMPTS = {
    # Animals (40 items)
    "cat": "A cute fluffy ginger cat with big eyes",
    "dog": "A happy golden retriever puppy", 
    "rabbit": "A soft white chubby bunny with a carrot",
    "bird": "A round adorable blue bird",
    "elephant": "A friendly baby elephant with large ears",
    "tiger": "A tiny brave tiger cub with soft fur",
    "lion": "A cute baby lion with a fluffy mane",
    "giraffe": "A tall friendly giraffe with brown spots",
    "zebra": "A sweet baby zebra with black stripes",
    "monkey": "A playful little monkey hanging by tail",
    "panda": "A chubby black and white panda with bamboo",
    "koala": "A grey fuzzy koala hugging a branch",
    "penguin": "A tiny round penguin with yellow beak",
    "pig": "A round pink piglet with a curly tail",
    "cow": "A friendly black and white spotted cow",
    "sheep": "A white fluffy sheep with soft wool",
    "horse": "A majestic small brown horse",
    "chicken": "A round yellow mother hen",
    "duck": "A bright yellow duckling with orange beak",
    "bear": "A soft brown bear with a round face",
    "fox": "A cute orange fox with a bushy tail",
    "deer": "A small brown deer with white spots",
    "hedgehog": "A tiny round hedgehog with soft prickles",
    "squirrel": "A bushy-tailed squirrel holding a nut",
    "camel": "A friendly camel with two humps",
    "snake": "A cute green coiled snake with big eyes",
    "crocodile": "A small green crocodile with a big smile",
    "turtle": "A slow green turtle with a patterned shell",
    "frog": "A happy green frog on a lily pad",
    "butterfly": "A colorful butterfly with patterned wings",
    "bee": "A fuzzy yellow and black honey bee",
    "ladybug": "A small red ladybug with black spots",
    "crab": "A red crab with two big claws",
    "lobster": "A long red lobster with big feelers",
    "octopus": "A purple octopus with eight wiggly arms",
    "whale": "A big blue whale spouting water",
    "dolphin": "A sleek grey dolphin jumping from water",
    "shark": "A small grey shark with a friendly grin",
    "seahorse": "A tiny colorful seahorse in water",
    "jellyfish": "A glowing pink translucent jellyfish",
    
    # Fruits (30 items)
    "apple": "A shiny red round apple with a leaf",
    "banana": "A bunch of bright yellow bananas",
    "orange": "A perfectly round orange fruit",
    "grape": "A bunch of purple round grapes",
    "watermelon": "A large round green striped watermelon",
    "strawberry": "A bright red strawberry with tiny seeds",
    "pineapple": "A golden pineapple with a green crown",
    "mango": "A smooth yellow and red mango",
    "pear": "A soft green pear with a narrow top",
    "peach": "A fuzzy pink and orange peach",
    "cherry": "Two bright red cherries with a stem",
    "blueberry": "A group of small round dark blue berries",
    "kiwi": "A fuzzy brown kiwi fruit sliced open",
    "lemon": "A bright yellow sour lemon",
    "dragonfruit": "A pink dragonfruit with green scales",
    "melon": "A pale green melon with textured skin",
    "lychee": "A small red bumpy lychee fruit",
    "coconut": "A brown hairy coconut with palm leaf",
    "pomegranate": "A red pomegranate with a small crown",
    "persimmon": "A bright orange persimmon fruit",
    "mangosteen": "A dark purple mangosteen with green cap",
    "pomelo": "A large yellow round pomelo fruit",
    "papaya": "A long orange papaya fruit with seeds",
    "apricot": "A small orange apricot with soft skin",
    "plum": "A round dark purple juicy plum",
    "fig": "A purple pear-shaped fig sliced open",
    "starfruit": "A yellow star-shaped starfruit",
    "durian": "A large green spiky durian fruit",
    "raspberry": "A small bumpy red raspberry fruit",
    
    # Vegetables (30 items)
    "carrot": "A long orange carrot with green leaves",
    "cabbage": "A round cabbage with crisp green leaves",
    "tomato": "A plump bright red tomato with stem",
    "broccoli": "A green broccoli tree-like vegetable",
    "potato": "A chunky brown potato with tiny eyes",
    "cucumber": "A long green bumpy cucumber",
    "eggplant": "A smooth shiny purple eggplant",
    "corn": "A yellow ear of corn with green husks",
    "pumpkin": "A large round orange pumpkin",
    "onion": "A round purple onion with thin skin",
    "garlic": "A white bulb of garlic with cloves",
    "chili": "A bright red spicy chili pepper",
    "mushroom": "A cute red mushroom with white spots",
    "pea": "A green pea pod with small round peas",
    "spinach": "A bunch of fresh green spinach leaves",
    "celery": "A bundle of long green celery stalks",
    "radish": "A round pink and white radish",
    "sweet_potato": "A long purple-skinned sweet potato",
    "bitter_gourd": "A long bumpy green bitter gourd",
    "luffa": "A long green luffa with ridges",
    "asparagus": "A bundle of thin green asparagus spears",
    "bell_pepper": "A shiny green bell pepper",
    "cauliflower": "A white cauliflower with green leaves",
    "green_bean": "A pile of thin long green beans",
    "lotus_root": "A sliced lotus root with holes",
    "bamboo_shoot": "A small brown bamboo shoot",
    "yam": "A long brown thin yam root",
    "wax_gourd": "A large long green wax gourd",
    "snow_pea": "A flat green snow pea pod",
    "lettuce": "A bunch of light green wavy lettuce",
    
    # Transportation (35 items)
    "car": "A small rounded red car",
    "bus": "A big yellow school bus",
    "airplane": "A chubby blue and white airplane",
    "bicycle": "A cute green bicycle with a bell",
    "motorcycle": "A cool red motorcycle",
    "train": "A colorful steam engine train",
    "high_speed_train": "A sleek white high-speed train",
    "ship": "A large white cruise ship",
    "submarine": "A yellow submarine with periscope",
    "helicopter": "A cute little helicopter",
    "ambulance": "A white ambulance with red cross",
    "firetruck": "A big red fire truck with ladder",
    "police_car": "A blue and white police car",
    "truck": "A large delivery truck",
    "tractor": "A green farm tractor",
    "hot_air_balloon": "A colorful striped hot air balloon",
    "spaceship": "A round silver UFO spaceship",
    "tank": "A small green toy tank",
    "excavator": "A yellow construction excavator",
    "sailboat": "A small wooden sailboat with sail",
    "taxi": "A yellow city taxi with checkers",
    "crane": "A big yellow crane truck with hook",
    "cable_car": "A red cable car hanging on a wire",
    "canoe": "A small wooden canoe with a paddle",
    "airship": "A big silver oval airship",
    "tricycle": "A small colorful kids tricycle",
    "skateboard": "A cool wooden skateboard with wheels",
    "steam_roller": "A yellow steam roller construction car",
    "garbage_truck": "A big green garbage truck",
    "tugboat": "A small strong red tugboat",
    "forklift": "A small yellow forklift with forks",
    "racing_car": "A fast red racing car with numbers",
    "rv": "A big white rv motorhome",
    "chopper": "A small black military chopper",
    "ebike": "A small pink electric scooter",
    
    # Daily Items (40 items)
    "pencil": "A long yellow pencil with eraser",
    "cup": "A chunky blue ceramic cup",
    "book": "A thick colorful book",
    "schoolbag": "A cute blue backpack",
    "toothbrush": "A small green toothbrush",
    "towel": "A soft folded fluffy white towel",
    "comb": "A small red hair comb",
    "mirror": "A small hand mirror with handle",
    "umbrella": "A bright yellow opened umbrella",
    "hat": "A cool blue baseball cap",
    "shoes": "A pair of small colorful sneakers",
    "clothes": "A small cute t-shirt with a sun",
    "bed": "A cozy bed with soft pillow",
    "chair": "A small wooden chair",
    "desk": "A sturdy wooden desk",
    "lamp": "A small desk lamp with shade",
    "tv": "A flat screen television",
    "phone": "A modern smartphone",
    "computer": "A desktop computer with monitor",
    "clock": "A round wall clock",
    "scissors": "A pair of small safety scissors",
    "soap": "A bar of pink bubbly soap",
    "basin": "A small round plastic basin",
    "slippers": "A pair of soft fuzzy slippers",
    "socks": "A pair of striped colorful socks",
    "bowl": "A round ceramic cereal bowl",
    "spoon": "A small shiny silver spoon",
    "chopsticks": "A pair of wooden chopsticks",
    "fork": "A small shiny silver fork",
    "pot": "A metal cooking pot with a lid",
    "fridge": "A big silver refrigerator",
    "washing_machine": "A white front-load washing machine",
    "air_conditioner": "A white wall-mounted air conditioner",
    "fan": "A small desk fan with blue blades",
    "hairdryer": "A small red hairdryer",
    "key": "A shiny gold key on a ring",
    "wallet": "A small brown leather wallet",
    "tissue": "A box of soft white tissues",
    "teddy_bear": "A soft brown teddy bear",
    "blocks": "A pile of colorful wooden blocks",
    
    # Nature (20 items)
    "sun": "A bright happy yellow sun with rays",
    "moon": "A yellow crescent moon with a face",
    "star": "A glowing yellow 3D star",
    "cloud": "A soft fluffy white cloud",
    "rainbow": "A colorful arched rainbow with clouds",
    "rain": "Small blue rain drops falling",
    "snow": "A white snowflake with intricate pattern",
    "wind": "Soft white swirls representing wind",
    "lightning": "A bright yellow lightning bolt",
    "mountain": "A tall mountain with snow on top",
    "ocean": "Blue ocean waves with white foam",
    "forest": "A group of green pine trees",
    "flower": "A bright pink flower with green leaves",
    "grass": "A patch of soft green grass",
    "tree": "A big tree with green leaves and trunk",
    "river": "A winding blue river with small rocks",
    "lake": "A calm blue lake with reflection",
    "fire": "A small warm orange campfire",
    "rock": "A smooth grey round rock",
    "island": "A small island with a palm tree",
    
    # Food & Drink (25 items)
    "bread": "A loaf of fresh brown bread",
    "milk": "A carton of fresh white milk",
    "egg": "A white egg in a small cup",
    "cake": "A colorful birthday cake with candles",
    "cookie": "A round chocolate chip cookie",
    "candy": "A colorful wrapped sweet candy",
    "ice_cream": "A pink ice cream cone with sprinkles",
    "juice": "A glass of orange juice with a straw",
    "water": "A glass of clear pure water",
    "burger": "A big burger with cheese and lettuce",
    "fries": "A box of golden crispy french fries",
    "pizza": "A slice of cheesy pepperoni pizza",
    "noodles": "A bowl of yellow noodles with egg",
    "rice": "A bowl of fluffy white rice",
    "steamed_bun": "A white fluffy steamed bun",
    "dumpling": "A plate of small white dumplings",
    "chocolate": "A bar of brown chocolate snapped",
    "donut": "A pink glazed donut with sprinkles",
    "sandwich": "A triangle sandwich with ham",
    "sushi": "A piece of sushi with fish and rice",
    "soup": "A warm bowl of vegetable soup",
    "honey": "A jar of golden sweet honey",
    "cheese": "A wedge of yellow Swiss cheese",
    "popcorn": "A bucket of white fluffy popcorn",
    "lollipop": "A colorful swirled lollipop",
    
    # Body Parts (15 items)
    "eyes": "A pair of big bright twinkling eyes",
    "nose": "A small cute button nose",
    "mouth": "A happy smiling mouth with red lips",
    "ears": "A pair of small rounded ears",
    "hair": "A bunch of soft brown wavy hair",
    "hand": "A small waving hand with five fingers",
    "foot": "A small foot with five tiny toes",
    "arm": "A strong small arm with a hand",
    "leg": "A long leg with a small foot",
    "head": "A round head with a happy face",
    "finger": "A small pointing index finger",
    "teeth": "A row of white shiny clean teeth",
    "tongue": "A small pink sticking-out tongue",
    "shoulder": "A small rounded shoulder",
    "tummy": "A round soft tummy"
}

# Category ranges (based on items_data.py order)
CATEGORY_RANGES = {
    "animals": (0, 40),
    "fruits": (40, 70),
    "vegetables": (70, 100),
    "transportation": (100, 135),
    "daily_items": (135, 175),
    "nature": (175, 195),
    "food_drink": (195, 220),
    "body_parts": (220, 235)
}

# Statistics
stats = {
    "success": 0,
    "failed": 0,
    "skipped": 0,
    "total": 0
}
stats_lock = threading.Lock()

def ensure_output_dir():
    """Ensure the output directory exists"""
    OUTPUT_DIR.mkdir(parents=True, exist_ok=True)
    print(f"Output directory: {OUTPUT_DIR.absolute()}")

def get_prompts_for_category(category: str) -> List[Tuple[str, str]]:
    """Get prompts for a specific category"""
    prompts = []
    
    if category not in CATEGORY_RANGES:
        print(f"Error: Unknown category '{category}'")
        print(f"Available categories: {', '.join(CATEGORY_RANGES.keys())}")
        return prompts
    
    start, end = CATEGORY_RANGES[category]
    category_items = ITEMS[start:end]
    
    for item in category_items:
        image_name = item[2]  # imageRes is the third element
        if image_name in ALL_PROMPTS:
            subject_prompt = ALL_PROMPTS[image_name]
            full_prompt = f"{subject_prompt}{STYLE_SUFFIX}"
            prompts.append((image_name, full_prompt))
        else:
            print(f"Warning: No prompt found for {image_name}")
    
    return prompts

def get_all_prompts() -> List[Tuple[str, str]]:
    """Get all prompts for all items"""
    all_prompts = []
    for category in CATEGORY_RANGES.keys():
        category_prompts = get_prompts_for_category(category)
        all_prompts.extend(category_prompts)
    return all_prompts

def generate_single_image(image_name: str, prompt: str) -> bool:
    """Generate a single image using Pollinations.ai (FREE API)"""
    output_path = OUTPUT_DIR / f"{image_name}.png"
    
    # Skip if image already exists
    if output_path.exists():
        with stats_lock:
            stats["skipped"] += 1
        print(f"  [SKIP] {image_name}.png (already exists)")
        return True
    
    try:
        # Pollinations.ai API - Completely FREE, no API key needed!
        # Using Flux model for best quality
        encoded_prompt = urllib.parse.quote(prompt)
        seed = hash(image_name) % 100000  # Consistent seed for reproducibility
        
        # Using Pollinations.ai with Flux model
        url = f"https://image.pollinations.ai/prompt/{encoded_prompt}?width=1024&height=1024&seed={seed}&nologo=true&negative_prompt=blurry,low%20quality,text,watermark,signature"
        
        print(f"  [GEN] {image_name}.png...")
        
        # Download the image
        headers = {
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36'
        }
        request = urllib.request.Request(url, headers=headers)
        
        with urllib.request.urlopen(request, timeout=120) as response:
            image_data = response.read()
            
            # Save the image
            with open(output_path, 'wb') as f:
                f.write(image_data)
        
        with stats_lock:
            stats["success"] += 1
        
        print(f"  [OK] {image_name}.png ({len(image_data)//1024}KB)")
        return True
        
    except Exception as e:
        with stats_lock:
            stats["failed"] += 1
        print(f"  [FAIL] {image_name}.png: {str(e)[:50]}")
        return False

def generate_images(category: str = None, max_workers: int = 3, dry_run: bool = False):
    """Generate images for a specific category or all categories"""
    
    # Reset stats
    stats["success"] = 0
    stats["failed"] = 0
    stats["skipped"] = 0
    
    if category:
        items_to_generate = get_prompts_for_category(category)
        print(f"\n{'='*60}")
        print(f"  CloudItemApp Image Generator - {category.upper()}")
        print(f"  Using FREE Pollinations.ai API (No API key needed!)")
        print(f"{'='*60}")
    else:
        items_to_generate = get_all_prompts()
        print(f"\n{'='*60}")
        print(f"  CloudItemApp Image Generator - ALL 235 ITEMS")
        print(f"  Using FREE Pollinations.ai API (No API key needed!)")
        print(f"{'='*60}")
    
    stats["total"] = len(items_to_generate)
    print(f"\n  Items to generate: {stats['total']}")
    print(f"  Output directory: {OUTPUT_DIR.absolute()}")
    print(f"  Max concurrent: {max_workers}")
    
    if dry_run:
        print("\n  DRY RUN MODE - Showing first 10 prompts:")
        for i, (image_name, prompt) in enumerate(items_to_generate[:10], 1):
            print(f"  {i}. {image_name}: {prompt[:70]}...")
        print(f"\n  Total: {stats['total']} items")
        return
    
    # Ensure output directory exists
    ensure_output_dir()
    
    print(f"\n  Starting generation...\n")
    
    # Generate images with thread pool for better speed
    completed = 0
    with ThreadPoolExecutor(max_workers=max_workers) as executor:
        futures = {
            executor.submit(generate_single_image, name, prompt): name 
            for name, prompt in items_to_generate
        }
        
        for future in as_completed(futures):
            completed += 1
            image_name = futures[future]
            
            try:
                future.result()
            except Exception as e:
                print(f"  [ERROR] {image_name}: {str(e)[:50]}")
            
            # Progress bar
            progress = (completed / stats["total"]) * 100
            print(f"\r  Progress: {completed}/{stats['total']} ({progress:.1f}%) | "
                  f"Success: {stats['success']} | Failed: {stats['failed']} | Skipped: {stats['skipped']}", 
                  end='', flush=True)
    
    print(f"\n\n{'='*60}")
    print(f"  GENERATION COMPLETE!")
    print(f"{'='*60}")
    print(f"  Total:     {stats['total']}")
    print(f"  Success:   {stats['success']} ✓")
    print(f"  Failed:    {stats['failed']} ✗")
    print(f"  Skipped:   {stats['skipped']} ⏭")
    print(f"{'='*60}")

if __name__ == "__main__":
    import argparse
    
    parser = argparse.ArgumentParser(
        description='Generate images for CloudItemApp using FREE Pollinations.ai API',
        formatter_class=argparse.RawDescriptionHelpFormatter,
        epilog="""
Examples:
  python generate_images_free.py                    # Generate all 235 images
  python generate_images_free.py -c animals         # Generate only animals
  python generate_images_free.py -t                 # Test mode (dry run)
  python generate_images_free.py -c fruits -w 5     # Generate fruits with 5 workers

Categories:
  animals, fruits, vegetables, transportation, daily_items, nature, food_drink, body_parts

FREE API - No signup or API key required!
        """
    )
    
    parser.add_argument('-c', '--category', type=str, 
                        help='Generate specific category only')
    parser.add_argument('-t', '--test', action='store_true',
                        help='Test mode (dry run, no images generated)')
    parser.add_argument('-w', '--workers', type=int, default=3,
                        help='Number of concurrent workers (default: 3)')
    
    args = parser.parse_args()
    
    generate_images(
        category=args.category,
        dry_run=args.test,
        max_workers=args.workers
    )
