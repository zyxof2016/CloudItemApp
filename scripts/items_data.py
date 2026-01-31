# -*- coding: utf-8 -*-
"""云朵识物乐园 - 235 个物品数据，与 DataInitializer.kt 保持一致。用于生成音频/图片清单。"""

# 每项: (中文名, 英文名, 资源名 imageRes/audio 前缀)
# 资源名必须符合 Android drawable/raw 命名: 小写字母、数字、下划线

ITEMS = []

def _add(category_items):
    ITEMS.extend(category_items)

_add([("猫", "Cat", "cat"), ("狗", "Dog", "dog"), ("兔子", "Rabbit", "rabbit"), ("小鸟", "Bird", "bird"),
      ("大象", "Elephant", "elephant"), ("老虎", "Tiger", "tiger"), ("狮子", "Lion", "lion"), ("长颈鹿", "Giraffe", "giraffe"),
      ("斑马", "Zebra", "zebra"), ("猴子", "Monkey", "monkey"), ("熊猫", "Panda", "panda"), ("考拉", "Koala", "koala"),
      ("企鹅", "Penguin", "penguin"), ("猪", "Pig", "pig"), ("牛", "Cow", "cow"), ("羊", "Sheep", "sheep"),
      ("马", "Horse", "horse"), ("鸡", "Chicken", "chicken"), ("鸭", "Duck", "duck"), ("熊", "Bear", "bear"),
      ("狐狸", "Fox", "fox"), ("鹿", "Deer", "deer"), ("刺猬", "Hedgehog", "hedgehog"), ("松鼠", "Squirrel", "squirrel"),
      ("骆驼", "Camel", "camel"), ("蛇", "Snake", "snake"), ("鳄鱼", "Crocodile", "crocodile"), ("乌龟", "Turtle", "turtle"),
      ("青蛙", "Frog", "frog"), ("蝴蝶", "Butterfly", "butterfly"), ("蜜蜂", "Bee", "bee"), ("瓢虫", "Ladybug", "ladybug"),
      ("螃蟹", "Crab", "crab"), ("龙虾", "Lobster", "lobster"), ("章鱼", "Octopus", "octopus"), ("鲸鱼", "Whale", "whale"),
      ("海豚", "Dolphin", "dolphin"), ("鲨鱼", "Shark", "shark"), ("海马", "Seahorse", "seahorse"), ("水母", "Jellyfish", "jellyfish")])

_add([("苹果", "Apple", "apple"), ("香蕉", "Banana", "banana"), ("橙子", "Orange", "orange"), ("葡萄", "Grape", "grape"),
      ("西瓜", "Watermelon", "watermelon"), ("草莓", "Strawberry", "strawberry"), ("菠萝", "Pineapple", "pineapple"), ("芒果", "Mango", "mango"),
      ("梨", "Pear", "pear"), ("桃子", "Peach", "peach"), ("樱桃", "Cherry", "cherry"), ("蓝莓", "Blueberry", "blueberry"),
      ("猕猴桃", "Kiwi", "kiwi"), ("柠檬", "Lemon", "lemon"), ("火龙果", "Dragonfruit", "dragonfruit"), ("哈密瓜", "Melon", "melon"),
      ("荔枝", "Lychee", "lychee"), ("椰子", "Coconut", "coconut"), ("石榴", "Pomegranate", "pomegranate"), ("柿子", "Persimmon", "persimmon"),
      ("山竹", "Mangosteen", "mangosteen"), ("柚子", "Pomelo", "pomelo"), ("木瓜", "Papaya", "papaya"), ("杏子", "Apricot", "apricot"),
      ("李子", "Plum", "plum"), ("无花果", "Fig", "fig"), ("杨桃", "Starfruit", "starfruit"), ("榴莲", "Durian", "durian"),
      ("蓝莓", "Blueberry", "blueberry_alt"), ("覆盆子", "Raspberry", "raspberry")])

_add([("胡萝卜", "Carrot", "carrot"), ("白菜", "Cabbage", "cabbage"), ("西红柿", "Tomato", "tomato"), ("西兰花", "Broccoli", "broccoli"),
      ("土豆", "Potato", "potato"), ("黄瓜", "Cucumber", "cucumber"), ("茄子", "Eggplant", "eggplant"), ("玉米", "Corn", "corn"),
      ("南瓜", "Pumpkin", "pumpkin"), ("洋葱", "Onion", "onion"), ("大蒜", "Garlic", "garlic"), ("辣椒", "Chili", "chili"),
      ("蘑菇", "Mushroom", "mushroom"), ("豌豆", "Pea", "pea"), ("菠菜", "Spinach", "spinach"), ("芹菜", "Celery", "celery"),
      ("萝卜", "Radish", "radish"), ("红薯", "Sweet Potato", "sweet_potato"), ("苦瓜", "Bitter Gourd", "bitter_gourd"), ("丝瓜", "Luffa", "luffa"),
      ("芦笋", "Asparagus", "asparagus"), ("甜椒", "Bell Pepper", "bell_pepper"), ("菜花", "Cauliflower", "cauliflower"), ("豆角", "Green Bean", "green_bean"),
      ("莲藕", "Lotus Root", "lotus_root"), ("竹笋", "Bamboo Shoot", "bamboo_shoot"), ("山药", "Yam", "yam"), ("冬瓜", "Wax Gourd", "wax_gourd"),
      ("荷兰豆", "Snow Pea", "snow_pea"), ("生菜", "Lettuce", "lettuce")])

_add([("汽车", "Car", "car"), ("公交车", "Bus", "bus"), ("飞机", "Airplane", "airplane"), ("自行车", "Bicycle", "bicycle"),
      ("摩托车", "Motorcycle", "motorcycle"), ("火车", "Train", "train"), ("高铁", "High-speed Train", "high_speed_train"), ("轮船", "Ship", "ship"),
      ("潜水艇", "Submarine", "submarine"), ("直升机", "Helicopter", "helicopter"), ("救护车", "Ambulance", "ambulance"), ("消防车", "Firetruck", "firetruck"),
      ("警车", "Police Car", "police_car"), ("卡车", "Truck", "truck"), ("拖拉机", "Tractor", "tractor"), ("热气球", "Hot Air Balloon", "hot_air_balloon"),
      ("飞船", "Spaceship", "spaceship"), ("坦克", "Tank", "tank"), ("挖掘机", "Excavator", "excavator"), ("帆船", "Sailboat", "sailboat"),
      ("出租车", "Taxi", "taxi"), ("吊车", "Crane", "crane"), ("缆车", "Cable Car", "cable_car"), ("划艇", "Canoe", "canoe"),
      ("飞艇", "Airship", "airship"), ("三轮车", "Tricycle", "tricycle"), ("滑板", "Skateboard", "skateboard"), ("压路机", "Steam Roller", "steam_roller"),
      ("垃圾车", "Garbage Truck", "garbage_truck"), ("拖船", "Tugboat", "tugboat"), ("叉车", "Forklift", "forklift"), ("赛车", "Racing Car", "racing_car"),
      ("房车", "RV", "rv"), ("直升飞机", "Chopper", "chopper"), ("电动车", "E-bike", "ebike")])

_add([("铅笔", "Pencil", "pencil"), ("杯子", "Cup", "cup"), ("书本", "Book", "book"), ("书包", "Schoolbag", "schoolbag"),
      ("牙刷", "Toothbrush", "toothbrush"), ("毛巾", "Towel", "towel"), ("梳子", "Comb", "comb"), ("镜子", "Mirror", "mirror"),
      ("伞", "Umbrella", "umbrella"), ("帽子", "Hat", "hat"), ("鞋子", "Shoes", "shoes"), ("衣服", "Clothes", "clothes"),
      ("床", "Bed", "bed"), ("椅子", "Chair", "chair"), ("桌子", "Desk", "desk"), ("灯", "Lamp", "lamp"),
      ("电视", "TV", "tv"), ("手机", "Phone", "phone"), ("电脑", "Computer", "computer"), ("钟表", "Clock", "clock"),
      ("剪刀", "Scissors", "scissors"), ("肥皂", "Soap", "soap"), ("盆", "Basin", "basin"), ("拖鞋", "Slippers", "slippers"),
      ("袜子", "Socks", "socks"), ("碗", "Bowl", "bowl"), ("勺子", "Spoon", "spoon"), ("筷子", "Chopsticks", "chopsticks"),
      ("叉子", "Fork", "fork"), ("锅", "Pot", "pot"), ("冰箱", "Fridge", "fridge"), ("洗衣机", "Washing Machine", "washing_machine"),
      ("空调", "Air Conditioner", "air_conditioner"), ("风扇", "Fan", "fan"), ("吹风机", "Hairdryer", "hairdryer"), ("钥匙", "Key", "key"),
      ("钱包", "Wallet", "wallet"), ("纸巾", "Tissue", "tissue"), ("玩具熊", "Teddy Bear", "teddy_bear"), ("积木", "Blocks", "blocks")])

_add([("太阳", "Sun", "sun"), ("月亮", "Moon", "moon"), ("星星", "Star", "star"), ("云朵", "Cloud", "cloud"),
      ("彩虹", "Rainbow", "rainbow"), ("雨", "Rain", "rain"), ("雪", "Snow", "snow"), ("风", "Wind", "wind"),
      ("雷电", "Lightning", "lightning"), ("大山", "Mountain", "mountain"), ("大海", "Ocean", "ocean"), ("森林", "Forest", "forest"),
      ("花朵", "Flower", "flower"), ("草地", "Grass", "grass"), ("树木", "Tree", "tree"), ("河流", "River", "river"),
      ("湖泊", "Lake", "lake"), ("火", "Fire", "fire"), ("岩石", "Rock", "rock"), ("岛屿", "Island", "island")])

_add([("面包", "Bread", "bread"), ("牛奶", "Milk", "milk"), ("鸡蛋", "Egg", "egg"), ("蛋糕", "Cake", "cake"),
      ("饼干", "Cookie", "cookie"), ("糖果", "Candy", "candy"), ("冰淇淋", "Ice Cream", "ice_cream"), ("果汁", "Juice", "juice"),
      ("水", "Water", "water"), ("汉堡", "Burger", "burger"), ("薯条", "Fries", "fries"), ("披萨", "Pizza", "pizza"),
      ("面条", "Noodles", "noodles"), ("米饭", "Rice", "rice"), ("包子", "Steamed Bun", "steamed_bun"), ("饺子", "Dumpling", "dumpling"),
      ("巧克力", "Chocolate", "chocolate"), ("甜甜圈", "Donut", "donut"), ("三明治", "Sandwich", "sandwich"), ("寿司", "Sushi", "sushi"),
      ("汤", "Soup", "soup"), ("蜂蜜", "Honey", "honey"), ("奶酪", "Cheese", "cheese"), ("爆米花", "Popcorn", "popcorn"),
      ("棒棒糖", "Lollipop", "lollipop")])

_add([("眼睛", "Eyes", "eyes"), ("鼻子", "Nose", "nose"), ("嘴巴", "Mouth", "mouth"), ("耳朵", "Ears", "ears"),
      ("头发", "Hair", "hair"), ("手", "Hand", "hand"), ("脚", "Foot", "foot"), ("胳膊", "Arm", "arm"),
      ("腿", "Leg", "leg"), ("头", "Head", "head"), ("手指", "Finger", "finger"), ("牙齿", "Teeth", "teeth"),
      ("舌头", "Tongue", "tongue"), ("肩膀", "Shoulder", "shoulder"), ("肚子", "Tummy", "tummy")])

assert len(ITEMS) == 235, "Expected 235 items, got %d" % len(ITEMS)
