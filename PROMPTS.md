# AI 资源生成指南 - 云朵识物乐园 (CloudItemApp)

本文档提供符合 README 规划的 **235个核心识物物品** 的 AI 生成提示词。

## 🎨 核心风格定义 (Core Style)

### 优化版 v3 含幼儿（推荐：2-6岁认知审美、去抽象词、简化细节）
*   **统一后缀**：`, complete in frame no cropping, centered composition, cute 3D clay style, toddler-friendly educational illustration, bright vibrant primary colors, soft studio lighting, theme-fitting soft gradient background, rounded edges friendly, simple clear shapes, comfortable negative space, no watermark, clean, masterpiece, clear simple details, square 1:1, --ar 1:1`
*   **主体描述**：动物类加 `full body`, `cute/friendly`（去 brave/majestic）；蛇/鳄鱼/鲨鱼加 `adorable`；物体类加 `complete`；避免 `cool`/`military` 等抽象或敏感词

### 原版（白底抠图风格）
*   **统一后缀**：`, children's educational illustration, cute 3D clay style, bright and vibrant colors, soft studio lighting, high resolution, isolated on white background, rounded edges, friendly appearance, masterpiece, high detail, --ar 1:1`

---

## 🔊 音频生成指南 (Audio Generation)
**核心风格**：亲切、清晰、富有启发性的儿童教育语调。

### 1. 物品名称读音 (TTS - Text to Speech)
建议使用 **ElevenLabs** 或 **GPT-SoVITS**，选择“温柔女性教师”或“活泼小朋友”音色。

*   **中文提示词 (Chinese)**: `Gentle, clear female voice, preschool teacher style, standard Mandarin, enthusiastic but calm, slow pace for toddlers.`
*   **英文提示词 (English)**: `Cheerful, clear native English female voice, energetic and encouraging, perfect for kids learning, slightly slower speed.`

### 2. 背景音乐与音效 (BGM & Sound Effects)
建议使用 **Suno AI** 或 **Udio** (BGM)，以及 **Stable Audio** (SFX)。

| 类型 | 推荐工具 | 提示词 (Audio Prompt) |
| :--- | :--- | :--- |
| **主页 BGM** | Suno/Udio | `Happy nursery rhyme instrumental, xylophone, acoustic guitar, bouncy 4/4 beat, innocent, cheerful, loopable, no vocals.` |
| **正确音效** | Stable Audio | `Bright magic sparkle ding, rewarding chime, high pitched, happy, short (1s), clean.` |
| **错误音效** | Stable Audio | `Soft cartoon "boing" sound, gentle wobble, not scary, short (1s).` |
| **成功动画音效** | Stable Audio | `Celebration fan-fare, kids cheering, pop sound, party blower, joyful.` |

---

## 🎞️ 动画生成指南 (Lottie & Motion)
**核心风格**：Q弹（Bouncy）、流畅、色彩明快。

### 1. 庆祝动画 (celebration.json)
*   **提示词 (Prompt)**: `Colorful confetti bursting, a cute golden trophy jumping with a happy face, 2D vector flat animation, vibrant primary colors, white background, smooth loop.`

### 2. 加载动画 (loading.json)
*   **提示词 (Prompt)**: `A fluffy white cloud floating up and down, a small sun peeking behind it, cute breathing animation, minimalist vector style.`

### 3. 成就解锁 (achievement.json)
*   **提示词 (Prompt)**: `A shiny golden star spinning and growing, sparkling particles, rewarding animation, high contrast, clean background.`

---

## 📦 物品提示词列表 (Total: 235)

### 1. 动物世界 (Animals - 40)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 猫 | cat | `A cute fluffy ginger cat with big eyes` |
| 狗 | dog | `A happy golden retriever puppy` |
| 兔子 | rabbit | `A soft white chubby bunny with a carrot` |
| 小鸟 | bird | `A round adorable blue bird` |
| 大象 | elephant | `A friendly baby elephant with large ears` |
| 老虎 | tiger | `A tiny brave tiger cub with soft fur` |
| 狮子 | lion | `A cute baby lion with a fluffy mane` |
| 长颈鹿 | giraffe | `A tall friendly giraffe with brown spots` |
| 斑马 | zebra | `A sweet baby zebra with black stripes` |
| 猴子 | monkey | `A playful little monkey hanging by tail` |
| 熊猫 | panda | `A chubby black and white panda with bamboo` |
| 考拉 | koala | `A grey fuzzy koala hugging a branch` |
| 企鹅 | penguin | `A tiny round penguin with yellow beak` |
| 猪 | pig | `A round pink piglet with a curly tail` |
| 牛 | cow | `A friendly black and white spotted cow` |
| 羊 | sheep | `A white fluffy sheep with soft wool` |
| 马 | horse | `A majestic small brown horse` |
| 鸡 | chicken | `A round yellow mother hen` |
| 鸭 | duck | `A bright yellow duckling with orange beak` |
| 熊 | bear | `A soft brown bear with a round face` |
| 狐狸 | fox | `A cute orange fox with a bushy tail` |
| 鹿 | deer | `A small brown deer with white spots` |
| 刺猬 | hedgehog | `A tiny round hedgehog with soft prickles` |
| 松鼠 | squirrel | `A bushy-tailed squirrel holding a nut` |
| 骆驼 | camel | `A friendly camel with two humps` |
| 蛇 | snake | `A cute green coiled snake with big eyes` |
| 鳄鱼 | crocodile | `A small green crocodile with a big smile` |
| 乌龟 | turtle | `A slow green turtle with a patterned shell` |
| 青蛙 | frog | `A happy green frog on a lily pad` |
| 蝴蝶 | butterfly | `A colorful butterfly with patterned wings` |
| 蜜蜂 | bee | `A fuzzy yellow and black honey bee` |
| 瓢虫 | ladybug | `A small red ladybug with black spots` |
| 螃蟹 | crab | `A red crab with two big claws` |
| 龙虾 | lobster | `A long red lobster with big feelers` |
| 章鱼 | octopus | `A purple octopus with eight wiggly arms` |
| 鲸鱼 | whale | `A big blue whale spouting water` |
| 海豚 | dolphin | `A sleek grey dolphin jumping from water` |
| 鲨鱼 | shark | `A small grey shark with a friendly grin` |
| 海马 | seahorse | `A tiny colorful seahorse in water` |
| 水母 | jellyfish | `A glowing pink translucent jellyfish` |

### 2. 美味水果 (Fruits - 30)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 苹果 | apple | `A shiny red round apple with a leaf` |
| 香蕉 | banana | `A bunch of bright yellow bananas` |
| 橙子 | orange | `A perfectly round orange fruit` |
| 葡萄 | grape | `A bunch of purple round grapes` |
| 西瓜 | watermelon | `A large round green striped watermelon` |
| 草莓 | strawberry | `A bright red strawberry with tiny seeds` |
| 菠萝 | pineapple | `A golden pineapple with a green crown` |
| 芒果 | mango | `A smooth yellow and red mango` |
| 梨 | pear | `A soft green pear with a narrow top` |
| 桃子 | peach | `A fuzzy pink and orange peach` |
| 樱桃 | cherry | `Two bright red cherries with a stem` |
| 蓝莓 | blueberry | `A group of small round dark blue berries` |
| 猕猴桃 | kiwi | `A fuzzy brown kiwi fruit sliced open` |
| 柠檬 | lemon | `A bright yellow sour lemon` |
| 火龙果 | dragonfruit | `A pink dragonfruit with green scales` |
| 哈密瓜 | melon | `A pale green melon with textured skin` |
| 荔枝 | lychee | `A small red bumpy lychee fruit` |
| 椰子 | coconut | `A brown hairy coconut with palm leaf` |
| 石榴 | pomegranate | `A red pomegranate with a small crown` |
| 柿子 | persimmon | `A bright orange persimmon fruit` |
| 山竹 | mangosteen | `A dark purple mangosteen with green cap` |
| 柚子 | pomelo | `A large yellow round pomelo fruit` |
| 木瓜 | papaya | `A long orange papaya fruit with seeds` |
| 杏子 | apricot | `A small orange apricot with soft skin` |
| 李子 | plum | `A round dark purple juicy plum` |
| 无花果 | fig | `A purple pear-shaped fig sliced open` |
| 杨桃 | starfruit | `A yellow star-shaped starfruit` |
| 榴莲 | durian | `A large green spiky durian fruit` |
| 蓝莓 | blueberry | `A small cluster of blue berries` |
| 覆盆子 | raspberry | `A small bumpy red raspberry fruit` |

### 3. 新鲜蔬菜 (Vegetables - 30)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 胡萝卜 | carrot | `A long orange carrot with green leaves` |
| 白菜 | cabbage | `A round cabbage with crisp green leaves` |
| 西红柿 | tomato | `A plump bright red tomato with stem` |
| 西兰花 | broccoli | `A green broccoli tree-like vegetable` |
| 土豆 | potato | `A chunky brown potato with tiny eyes` |
| 黄瓜 | cucumber | `A long green bumpy cucumber` |
| 茄子 | eggplant | `A smooth shiny purple eggplant` |
| 玉米 | corn | `A yellow ear of corn with green husks` |
| 南瓜 | pumpkin | `A large round orange pumpkin` |
| 洋葱 | onion | `A round purple onion with thin skin` |
| 大蒜 | garlic | `A white bulb of garlic with cloves` |
| 辣椒 | chili | `A bright red spicy chili pepper` |
| 蘑菇 | mushroom | `A cute red mushroom with white spots` |
| 豌豆 | pea | `A green pea pod with small round peas` |
| 菠菜 | spinach | `A bunch of fresh green spinach leaves` |
| 芹菜 | celery | `A bundle of long green celery stalks` |
| 萝卜 | radish | `A round pink and white radish` |
| 红薯 | sweet_potato | `A long purple-skinned sweet potato` |
| 苦瓜 | bitter_gourd | `A long bumpy green bitter gourd` |
| 丝瓜 | luffa | `A long green luffa with ridges` |
| 芦笋 | asparagus | `A bundle of thin green asparagus spears` |
| 甜椒 | bell_pepper | `A shiny green bell pepper` |
| 菜花 | cauliflower | `A white cauliflower with green leaves` |
| 豆角 | green_bean | `A pile of thin long green beans` |
| 莲藕 | lotus_root | `A sliced lotus root with holes` |
| 竹笋 | bamboo_shoot | `A small brown bamboo shoot` |
| 山药 | yam | `A long brown thin yam root` |
| 冬瓜 | wax_gourd | `A large long green wax gourd` |
| 荷兰豆 | snow_pea | `A flat green snow pea pod` |
| 生菜 | lettuce | `A bunch of light green wavy lettuce` |

### 4. 交通工具 (Transportation - 35)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 汽车 | car | `A small rounded red car` |
| 公交车 | bus | `A big yellow school bus` |
| 飞机 | airplane | `A chubby blue and white airplane` |
| 自行车 | bicycle | `A cute green bicycle with a bell` |
| 摩托车 | motorcycle | `A cool red motorcycle` |
| 火车 | train | `A colorful steam engine train` |
| 高铁 | high_speed_train | `A sleek white high-speed train` |
| 轮船 | ship | `A large white cruise ship` |
| 潜水艇 | submarine | `A yellow submarine with periscope` |
| 直升机 | helicopter | `A cute little helicopter` |
| 救护车 | ambulance | `A white ambulance with red cross` |
| 消防车 | firetruck | `A big red fire truck with ladder` |
| 警车 | police_car | `A blue and white police car` |
| 卡车 | truck | `A large delivery truck` |
| 拖拉机 | tractor | `A green farm tractor` |
| 热气球 | hot_air_balloon | `A colorful striped hot air balloon` |
| 飞船 | spaceship | `A round silver UFO spaceship` |
| 坦克 | tank | `A small green toy tank` |
| 挖掘机 | excavator | `A yellow construction excavator` |
| 帆船 | sailboat | `A small wooden sailboat with sail` |
| 出租车 | taxi | `A yellow city taxi with checkers` |
| 吊车 | crane | `A big yellow crane truck with hook` |
| 缆车 | cable_car | `A red cable car hanging on a wire` |
| 划艇 | canoe | `A small wooden canoe with a paddle` |
| 飞艇 | airship | `A big silver oval airship` |
| 三轮车 | tricycle | `A small colorful kids tricycle` |
| 滑板 | skateboard | `A cool wooden skateboard with wheels` |
| 压路机 | steam_roller | `A yellow steam roller construction car` |
| 垃圾车 | garbage_truck | `A big green garbage truck` |
| 拖船 | tugboat | `A small strong red tugboat` |
| 叉车 | forklift | `A small yellow forklift with forks` |
| 赛车 | racing_car | `A fast red racing car with numbers` |
| 房车 | rv | `A big white rv motorhome` |
| 直升飞机 | chopper | `A small black military chopper` |
| 电动车 | ebike | `A small pink electric scooter` |

### 5. 日常用品 (Daily Items - 40)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 铅笔 | pencil | `A long yellow pencil with eraser` |
| 杯子 | cup | `A chunky blue ceramic cup` |
| 书本 | book | `A thick colorful book` |
| 书包 | schoolbag | `A cute blue backpack` |
| 牙刷 | toothbrush | `A small green toothbrush` |
| 毛巾 | towel | `A soft folded fluffy white towel` |
| 梳子 | comb | `A small red hair comb` |
| 镜子 | mirror | `A small hand mirror with handle` |
| 伞 | umbrella | `A bright yellow opened umbrella` |
| 帽子 | hat | `A cool blue baseball cap` |
| 鞋子 | shoes | `A pair of small colorful sneakers` |
| 衣服 | clothes | `A small cute t-shirt with a sun` |
| 床 | bed | `A cozy bed with soft pillow` |
| 椅子 | chair | `A small wooden chair` |
| 桌子 | desk | `A sturdy wooden desk` |
| 灯 | lamp | `A small desk lamp with shade` |
| 电视 | tv | `A flat screen television` |
| 手机 | phone | `A modern smartphone` |
| 电脑 | computer | `A desktop computer with monitor` |
| 钟表 | clock | `A round wall clock` |
| 剪刀 | scissors | `A pair of small safety scissors` |
| 肥皂 | soap | `A bar of pink bubbly soap` |
| 盆 | basin | `A small round plastic basin` |
| 拖鞋 | slippers | `A pair of soft fuzzy slippers` |
| 袜子 | socks | `A pair of striped colorful socks` |
| 碗 | bowl | `A round ceramic cereal bowl` |
| 勺子 | spoon | `A small shiny silver spoon` |
| 筷子 | chopsticks | `A pair of wooden chopsticks` |
| 叉子 | fork | `A small shiny silver fork` |
| 锅 | pot | `A metal cooking pot with a lid` |
| 冰箱 | fridge | `A big silver refrigerator` |
| 洗衣机 | washing_machine | `A white front-load washing machine` |
| 空调 | air_conditioner | `A white wall-mounted air conditioner` |
| 风扇 | fan | `A small desk fan with blue blades` |
| 吹风机 | hairdryer | `A small red hairdryer` |
| 钥匙 | key | `A shiny gold key on a ring` |
| 钱包 | wallet | `A small brown leather wallet` |
| 纸巾 | tissue | `A box of soft white tissues` |
| 玩具熊 | teddy_bear | `A soft brown teddy bear` |
| 积木 | blocks | `A pile of colorful wooden blocks` |

### 6. 自然现象 (Nature - 20)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 太阳 | sun | `A bright happy yellow sun with rays` |
| 月亮 | moon | `A yellow crescent moon with a face` |
| 星星 | star | `A glowing yellow 3D star` |
| 云朵 | cloud | `A soft fluffy white cloud` |
| 彩虹 | rainbow | `A colorful arched rainbow with clouds` |
| 雨 | rain | `Small blue rain drops falling` |
| 雪 | snow | `A white snowflake with intricate pattern` |
| 风 | wind | `Soft white swirls representing wind` |
| 雷电 | lightning | `A bright yellow lightning bolt` |
| 大山 | mountain | `A tall mountain with snow on top` |
| 大海 | ocean | `Blue ocean waves with white foam` |
| 森林 | forest | `A group of green pine trees` |
| 花朵 | flower | `A bright pink flower with green leaves` |
| 草地 | grass | `A patch of soft green grass` |
| 树木 | tree | `A big tree with green leaves and trunk` |
| 河流 | river | `A winding blue river with small rocks` |
| 湖泊 | lake | `A calm blue lake with reflection` |
| 火 | fire | `A small warm orange campfire` |
| 岩石 | rock | `A smooth grey round rock` |
| 岛屿 | island | `A small island with a palm tree` |

### 7. 食物与饮料 (Food & Drink - 25)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 面包 | bread | `A loaf of fresh brown bread` |
| 牛奶 | milk | `A carton of fresh white milk` |
| 鸡蛋 | egg | `A white egg in a small cup` |
| 蛋糕 | cake | `A colorful birthday cake with candles` |
| 饼干 | cookie | `A round chocolate chip cookie` |
| 糖果 | candy | `A colorful wrapped sweet candy` |
| 冰淇淋 | ice_cream | `A pink ice cream cone with sprinkles` |
| 果汁 | juice | `A glass of orange juice with a straw` |
| 水 | water | `A glass of clear pure water` |
| 汉堡 | burger | `A big burger with cheese and lettuce` |
| 薯条 | fries | `A box of golden crispy french fries` |
| 披萨 | pizza | `A slice of cheesy pepperoni pizza` |
| 面条 | noodles | `A bowl of yellow noodles with egg` |
| 米饭 | rice | `A bowl of fluffy white rice` |
| 包子 | steamed_bun | `A white fluffy steamed bun` |
| 饺子 | dumpling | `A plate of small white dumplings` |
| 巧克力 | chocolate | `A bar of brown chocolate snapped` |
| 甜甜圈 | donut | `A pink glazed donut with sprinkles` |
| 三明治 | sandwich | `A triangle sandwich with ham` |
| 寿司 | sushi | `A piece of sushi with fish and rice` |
| 汤 | soup | `A warm bowl of vegetable soup` |
| 蜂蜜 | honey | `A jar of golden sweet honey` |
| 奶酪 | cheese | `A wedge of yellow Swiss cheese` |
| 爆米花 | popcorn | `A bucket of white fluffy popcorn` |
| 棒棒糖 | lollipop | `A colorful swirled lollipop` |

### 8. 身体部位 (Body Parts - 15)
| 物体 | ID | 提示词 (Subject Prompt) |
| :--- | :--- | :--- |
| 眼睛 | eyes | `A pair of big bright twinkling eyes` |
| 鼻子 | nose | `A small cute button nose` |
| 嘴巴 | mouth | `A happy smiling mouth with red lips` |
| 耳朵 | ears | `A pair of small rounded ears` |
| 头发 | hair | `A bunch of soft brown wavy hair` |
| 手 | hand | `A small waving hand with five fingers` |
| 脚 | foot | `A small foot with five tiny toes` |
| 胳膊 | arm | `A strong small arm with a hand` |
| 腿 | leg | `A long leg with a small foot` |
| 头 | head | `A round head with a happy face` |
| 手指 | finger | `A small pointing index finger` |
| 牙齿 | teeth | `A row of white shiny clean teeth` |
| 舌头 | tongue | `A small pink sticking-out tongue` |
| 肩膀 | shoulder | `A small rounded shoulder` |
| 肚子 | tummy | `A round soft tummy` |

---
*Updated to match README.md content plan.*
