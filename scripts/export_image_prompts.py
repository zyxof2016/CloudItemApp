# -*- coding: utf-8 -*-
"""
导出图片生成清单 - 方案1：在 Cursor 中手动生成

运行: python export_image_prompts.py
输出: scripts/image_prompts_for_cursor.txt

用法：在 Cursor 聊天或 Composer 中，复制「完整提示词」到图片生成功能，
      生成后保存为「文件名」并放入 app/src/main/res/drawable/
"""

# 与 PROMPTS.md 表格顺序一致的 Subject Prompt（按 items_data 的 imageRes 顺序）
# 优化版 v3：含幼儿(2-6岁)认知审美 - 去抽象词、简化细节、强化可爱度
# 原则：主体在前；幼儿偏好简单清晰、圆润温暖、无威胁感
STYLE_SUFFIX = ", complete in frame no cropping, centered composition, cute 3D clay style, toddler-friendly educational illustration, bright vibrant primary colors, soft studio lighting, theme-fitting soft gradient background, rounded edges friendly, simple clear shapes, comfortable negative space, no watermark, clean, masterpiece, clear simple details, square 1:1, --ar 1:1"

# 动物类(40)：幼儿友好 - 去抽象词(brave/majestic)、强化可爱、潜在恐吓动物加 adorable
SUBJECT_PROMPTS = [
    "A cute fluffy ginger cat with big bright eyes, full body sitting from head to tail",
    "A happy golden retriever puppy with friendly smile, full body sitting from head to paws",
    "A soft white chubby bunny with a carrot, full body standing from head to feet",
    "A round adorable blue bird, full body with wings and tail visible",
    "A friendly baby elephant with large ears, full body standing from head to feet",
    "A cute tiny tiger cub with soft fur, full body sitting from head to tail",
    "A cute baby lion with a fluffy mane, full body sitting from head to paws",
    "A tall friendly giraffe with brown spots, full body standing from head to hooves",
    "A sweet baby zebra with black stripes, full body standing from head to hooves",
    "A playful little monkey hanging by tail, full body visible from head to feet",
    "A chubby black and white panda with bamboo, full body sitting from head to feet",
    "A grey fuzzy koala hugging a branch, full body visible from head to feet",
    "A tiny round penguin with yellow beak, full body standing from head to feet",
    "A round pink piglet with a curly tail, full body standing from head to feet",
    "A friendly black and white spotted cow, full body standing from head to hooves",
    "A white fluffy sheep with soft wool, full body standing from head to hooves",
    "A friendly small brown horse, full body standing from head to hooves",
    "A round yellow mother hen, full body standing from head to feet",
    "A bright yellow duckling with orange beak, full body standing from head to webbed feet",
    "A soft brown bear with a round face, full body sitting from head to paws",
    "A cute orange fox with a bushy tail, full body sitting from head to tail",
    "A small brown deer with white spots, full body standing from head to hooves",
    "A tiny round hedgehog with soft prickles, full body visible from head to feet",
    "A bushy-tailed squirrel holding a nut, full body visible from head to tail",
    "A friendly camel with two humps, full body standing from head to hooves",
    "An adorable cute green coiled snake with big round eyes, complete body in coil from head to tail",
    "An adorable small green crocodile with a big friendly smile, full body visible from snout to tail",
    "A slow green turtle with a patterned shell, full body visible from head to feet",
    "A happy green frog on a lily pad, full body visible from head to webbed feet",
    "A colorful butterfly with patterned wings, complete butterfly with both wings fully visible",
    "A fuzzy yellow and black honey bee, complete bee with wings and legs visible",
    "A small red ladybug with black spots, complete ladybug with wings and legs visible",
    "A red crab with two big claws, full body visible with all legs and claws",
    "A long red lobster with big feelers, complete lobster from antennae to tail",
    "A purple octopus with eight wiggly arms, full body visible with all eight arms",
    "A big blue whale spouting water, full body visible from head to tail fluke",
    "A sleek grey dolphin jumping from water, full body visible from snout to tail",
    "An adorable small grey shark with a big friendly grin, full body visible from snout to tail",
    "A tiny colorful seahorse in water, complete seahorse from head to curled tail",
    "A glowing pink translucent jellyfish, full body visible with bell and tentacles",
    # Fruits (30)：complete 前缀
    "A complete shiny red round apple with a leaf", "A complete bunch of bright yellow bananas",
    "A complete perfectly round orange fruit", "A complete bunch of purple round grapes",
    "A complete large round green striped watermelon", "A complete bright red strawberry with tiny seeds",
    "A complete golden pineapple with a green crown", "A complete smooth yellow and red mango",
    "A complete soft green pear with a narrow top", "A complete fuzzy pink and orange peach",
    "A complete pair of bright red cherries with stem", "A complete cluster of small round dark blue berries",
    "A complete fuzzy brown kiwi fruit sliced open", "A complete bright yellow sour lemon",
    "A complete pink dragonfruit with green scales", "A complete pale green melon with textured skin",
    "A complete small red bumpy lychee fruit", "A complete brown hairy coconut with palm leaf",
    "A complete red pomegranate with a small crown", "A complete bright orange persimmon fruit",
    "A complete dark purple mangosteen with green cap", "A complete large yellow round pomelo fruit",
    "A complete long orange papaya fruit with seeds", "A complete small orange apricot with soft skin",
    "A complete round dark purple juicy plum", "A complete purple pear-shaped fig sliced open",
    "A complete yellow star-shaped starfruit", "A complete large green spiky durian fruit",
    "A complete small cluster of blue berries", "A complete small bumpy red raspberry fruit",
    # Vegetables (30)：complete 前缀
    "A complete long orange carrot with green leaves", "A complete round cabbage with crisp green leaves",
    "A complete plump bright red tomato with stem", "A complete green broccoli tree-like vegetable",
    "A complete chunky brown potato with tiny eyes", "A complete long green bumpy cucumber",
    "A complete smooth shiny purple eggplant", "A complete yellow ear of corn with green husks",
    "A complete large round orange pumpkin", "A complete round purple onion with thin skin",
    "A complete white bulb of garlic with cloves", "A complete bright red spicy chili pepper",
    "A complete cute red mushroom with white spots", "A complete green pea pod with small round peas",
    "A complete bunch of fresh green spinach leaves", "A complete bundle of long green celery stalks",
    "A complete round pink and white radish", "A complete long purple-skinned sweet potato",
    "A complete long bumpy green bitter gourd", "A complete long green luffa with ridges",
    "A complete bundle of thin green asparagus spears", "A complete shiny green bell pepper",
    "A complete white cauliflower with green leaves", "A complete pile of thin long green beans",
    "A complete sliced lotus root with holes", "A complete small brown bamboo shoot",
    "A complete long brown thin yam root", "A complete large long green wax gourd",
    "A complete flat green snow pea pod", "A complete bunch of light green wavy lettuce",
    # Transport (35)：加 full vehicle / entire body 确保完整
    "A complete small rounded red car, full vehicle visible", "A complete big yellow school bus, full vehicle visible",
    "A complete chubby blue and white airplane, full aircraft visible",
    "A complete cute green bicycle with a bell, full bicycle visible",
    "A complete cute red motorcycle, full motorcycle visible",
    "A complete colorful steam engine train, full train visible",
    "A complete sleek white high-speed train, full train visible",
    "A complete large white cruise ship, full ship visible",
    "A complete yellow submarine with periscope, full submarine visible",
    "A complete cute little helicopter, full helicopter visible",
    "A complete white ambulance with red cross, full vehicle visible",
    "A complete big red fire truck with ladder, full vehicle visible",
    "A complete blue and white police car, full vehicle visible",
    "A complete large delivery truck, full truck visible",
    "A complete green farm tractor, full tractor visible",
    "A complete colorful striped hot air balloon, full balloon visible",
    "A complete round silver UFO spaceship, full craft visible",
    "A complete small green toy tank, full tank visible",
    "A complete yellow construction excavator, full machine visible",
    "A complete small wooden sailboat with sail, full boat visible",
    "A complete yellow city taxi with checkers, full vehicle visible",
    "A complete big yellow crane truck with hook, full vehicle visible",
    "A complete red cable car hanging on a wire, full car visible",
    "A complete small wooden canoe with a paddle, full canoe visible",
    "A complete big silver oval airship, full airship visible",
    "A complete small colorful kids tricycle, full tricycle visible",
    "A complete cute wooden skateboard with wheels, full skateboard visible",
    "A complete yellow steam roller construction car, full vehicle visible",
    "A complete big green garbage truck, full truck visible",
    "A complete small strong red tugboat, full boat visible",
    "A complete small yellow forklift with forks, full forklift visible",
    "A complete fast red racing car with numbers, full car visible",
    "A complete big white rv motorhome, full vehicle visible",
    "A complete small black helicopter, full helicopter visible",
    "A complete small pink electric scooter, full scooter visible",
    # Daily (40)：complete 前缀
    "A complete long yellow pencil with eraser", "A complete chunky blue ceramic cup",
    "A complete thick colorful book", "A complete cute blue backpack",
    "A complete small green toothbrush", "A complete soft folded fluffy white towel",
    "A complete small red hair comb", "A complete small hand mirror with handle",
    "A complete bright yellow opened umbrella", "A complete cute blue baseball cap",
    "A complete pair of small colorful sneakers", "A complete small cute t-shirt with a sun",
    "A complete cozy bed with soft pillow", "A complete small wooden chair",
    "A complete sturdy wooden desk", "A complete small desk lamp with shade",
    "A complete flat screen television", "A complete modern smartphone",
    "A complete desktop computer with monitor", "A complete round wall clock",
    "A complete pair of small safety scissors", "A complete bar of pink bubbly soap",
    "A complete small round plastic basin", "A complete pair of soft fuzzy slippers",
    "A complete pair of striped colorful socks", "A complete round ceramic cereal bowl",
    "A complete small shiny silver spoon", "A complete pair of wooden chopsticks",
    "A complete small shiny silver fork", "A complete metal cooking pot with a lid",
    "A complete big silver refrigerator", "A complete white front-load washing machine",
    "A complete white wall-mounted air conditioner", "A complete small desk fan with blue blades",
    "A complete small red hairdryer", "A complete shiny gold key on a ring",
    "A complete small brown leather wallet", "A complete box of soft white tissues",
    "A complete soft brown teddy bear", "A complete pile of colorful wooden blocks",
    # Nature (20)：complete 前缀
    "A complete bright happy yellow sun with rays", "A complete yellow crescent moon with a face",
    "A complete glowing yellow 3D star", "A complete soft fluffy white cloud",
    "A complete colorful arched rainbow with clouds", "A complete scene of small blue rain drops falling",
    "A complete white snowflake with simple pretty pattern", "A complete soft white swirls representing wind",
    "A complete soft stylized yellow lightning bolt", "A complete tall mountain with snow on top",
    "A complete blue ocean waves with white foam", "A complete group of green pine trees",
    "A complete bright pink flower with green leaves", "A complete patch of soft green grass",
    "A complete big tree with green leaves and trunk", "A complete winding blue river with small rocks",
    "A complete calm blue lake with reflection", "A complete small warm orange campfire",
    "A complete smooth grey round rock", "A complete small island with a palm tree",
    # Food (25)：complete 前缀
    "A complete loaf of fresh brown bread", "A complete carton of fresh white milk",
    "A complete white egg in a small cup", "A complete colorful birthday cake with candles",
    "A complete round chocolate chip cookie", "A complete colorful wrapped sweet candy",
    "A complete pink ice cream cone with sprinkles", "A complete glass of orange juice with a straw",
    "A complete glass of clear pure water", "A complete big burger with cheese and lettuce",
    "A complete box of golden crispy french fries", "A complete slice of cheesy pepperoni pizza",
    "A complete bowl of yellow noodles with egg", "A complete bowl of fluffy white rice",
    "A complete white fluffy steamed bun", "A complete plate of small white dumplings",
    "A complete bar of brown chocolate", "A complete pink glazed donut with sprinkles",
    "A complete triangle sandwich with ham", "A complete piece of sushi with fish and rice",
    "A complete warm bowl of vegetable soup", "A complete jar of golden sweet honey",
    "A complete wedge of yellow Swiss cheese", "A complete bucket of white fluffy popcorn",
    "A complete colorful swirled lollipop",
    # Body (15)：局部器官，complete 确保清晰呈现
    "A complete pair of big bright twinkling eyes", "A complete small cute button nose",
    "A complete happy smiling mouth with red lips", "A complete pair of small rounded ears",
    "A complete bunch of soft brown wavy hair", "A complete small waving hand with five fingers",
    "A complete small foot with five tiny toes", "A complete strong small arm with a hand",
    "A complete long leg with a small foot", "A complete round head with a happy face",
    "A complete small pointing index finger", "A complete row of white shiny clean teeth",
    "A complete small pink sticking-out tongue", "A complete small rounded shoulder",
    "A complete round soft tummy",
]


def main():
    from items_data import ITEMS
    if len(SUBJECT_PROMPTS) != len(ITEMS):
        print("WARNING: SUBJECT_PROMPTS count (%d) != ITEMS count (%d)" % (len(SUBJECT_PROMPTS), len(ITEMS)))
    out_path = "scripts/image_prompts_for_cursor.txt"
    lines = [
        "=" * 80,
        "云朵识物乐园 - 图片生成清单（方案1：Cursor 手动生成）",
        "=" * 80,
        "",
        "【使用步骤】",
        "1. 在 Cursor 中使用图片生成功能（Composer 或聊天）",
        "2. 复制下方「完整提示词」到生成框",
        "3. 生成后保存为「文件名」列所示（如 cat.png）",
        "4. 将图片放入 app/src/main/res/drawable/",
        "",
        "【优化版 v3 含幼儿】去抽象词、简化细节、强化可爱、潜在恐吓动物加 adorable",
        "",
        "=" * 80,
        ""
    ]
    for i, (name_cn, name_en, image_res) in enumerate(ITEMS):
        if i < len(SUBJECT_PROMPTS):
            subject = SUBJECT_PROMPTS[i]
        else:
            subject = "A cute 3D clay style object"  # fallback
        full_prompt = subject + STYLE_SUFFIX
        filename = image_res + ".png"
        lines.append("-" * 60)
        lines.append("【%d】%s (%s)" % (i + 1, name_cn, name_en))
        lines.append("文件名: %s" % filename)
        lines.append("完整提示词:")
        lines.append(full_prompt)
        lines.append("")
    with open(out_path, "w", encoding="utf-8") as f:
        f.write("\n".join(lines))
    print("已导出到 %s" % out_path)
    print("共 %d 条，请按清单在 Cursor 中逐条生成" % len(ITEMS))


if __name__ == "__main__":
    main()
