# How to Generate Images for CloudItemApp

## Step 1: Get OpenAI API Key

1. Go to https://platform.openai.com/
2. Sign up or log in to your account
3. Navigate to "API Keys" section
4. Click "Create new secret key"
5. Copy the key (it starts with `sk-`)

**Note:** Image generation with DALL-E 3 costs approximately $0.04 per image (1024x1024).  
Total cost for 235 images: ~$9.40 USD

## Step 2: Install Dependencies

Open terminal/command prompt and run:

```bash
cd scripts
pip install -r requirements.txt
```

## Step 3: Set API Key

### Option A: Environment Variable (Recommended)

**Windows:**
```cmd
set OPENAI_API_KEY=sk-your-key-here
```

**Mac/Linux:**
```bash
export OPENAI_API_KEY=sk-your-key-here
```

### Option B: Enter when prompted

Just run the script and it will ask for the key.

## Step 4: Generate Images

### Generate all 235 images at once:
```bash
cd scripts
python generate_images.py
```

### Generate by category:
```bash
# Animals only (40 images)
python generate_images.py --category animals

# Fruits only (30 images)
python generate_images.py --category fruits

# Available categories:
# - animals (40 items)
# - fruits (30 items)
# - vegetables (30 items)
# - transportation (35 items)
# - daily_items (40 items)
# - nature (20 items)
# - food_drink (25 items)
# - body_parts (15 items)
```

### Test without generating (dry run):
```bash
python generate_images.py --dry-run
```

## Step 5: Check Results

Images will be saved to: `app/src/main/res/drawable/`

The script will:
- Skip images that already exist (safe to re-run)
- Show progress for each image
- Add 2-second delays to avoid rate limits
- Report success/failure count at the end

## Troubleshooting

### "OpenAI library not found"
Run: `pip install openai requests`

### "API key invalid"
Double-check your API key at https://platform.openai.com/api-keys

### Rate limit errors
The script already includes 2-second delays. If you still get rate limited, increase the delay in the script or generate in smaller batches by category.

### Images not saving
Make sure the directory `app/src/main/res/drawable/` exists and you have write permissions.

## Image Style

All images are generated with this style:
- 3D clay style
- Bright and vibrant colors
- White background
- Children's educational illustration style
- 1024x1024 resolution
- Perfect for the CloudItemApp!

## Cost Estimate

- 235 images × $0.04 = ~$9.40 USD
- You can generate by category to spread costs over time
- Each category costs:
  - Animals: $1.60
  - Fruits: $1.20
  - Vegetables: $1.20
  - Transportation: $1.40
  - Daily Items: $1.60
  - Nature: $0.80
  - Food & Drink: $1.00
  - Body Parts: $0.60

Happy generating! 🎨