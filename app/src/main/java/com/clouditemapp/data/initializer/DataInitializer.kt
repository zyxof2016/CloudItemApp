package com.clouditemapp.data.initializer

import android.content.Context
import com.clouditemapp.data.local.dao.AchievementDao
import com.clouditemapp.data.local.dao.ItemDao
import com.clouditemapp.data.local.entity.AchievementEntity
import com.clouditemapp.data.local.entity.ItemEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataInitializer @Inject constructor(
    @ApplicationContext private val context: Context,
    private val itemDao: ItemDao,
    private val achievementDao: AchievementDao
) {

    fun initializeData() {
        CoroutineScope(Dispatchers.IO).launch {
            // 检查是否已经初始化
            val items = itemDao.getAllItems()
            items.collect { itemList ->
                if (itemList.isEmpty()) {
                    insertSampleItems()
                    insertSampleAchievements()
                }
            }
        }
    }

    private suspend fun insertSampleItems() {
        val sampleItems = mutableListOf<ItemEntity>()
        
        // 动物类 (1-40)
        val animals = listOf(
            Triple("猫", "Cat", "cat"), Triple("狗", "Dog", "dog"),
            Triple("兔子", "Rabbit", "rabbit"), Triple("小鸟", "Bird", "bird"),
            Triple("大象", "Elephant", "elephant"), Triple("老虎", "Tiger", "tiger"),
            Triple("狮子", "Lion", "lion"), Triple("长颈鹿", "Giraffe", "giraffe"),
            Triple("斑马", "Zebra", "zebra"), Triple("猴子", "Monkey", "monkey"),
            Triple("熊猫", "Panda", "panda"), Triple("考拉", "Koala", "koala"),
            Triple("企鹅", "Penguin", "penguin"), Triple("猪", "Pig", "pig"),
            Triple("牛", "Cow", "cow"), Triple("羊", "Sheep", "sheep"),
            Triple("马", "Horse", "horse"), Triple("鸡", "Chicken", "chicken"),
            Triple("鸭", "Duck", "duck"), Triple("熊", "Bear", "bear"),
            Triple("狐狸", "Fox", "fox"), Triple("鹿", "Deer", "deer"),
            Triple("刺猬", "Hedgehog", "hedgehog"), Triple("松鼠", "Squirrel", "squirrel"),
            Triple("骆驼", "Camel", "camel"), Triple("蛇", "Snake", "snake"),
            Triple("鳄鱼", "Crocodile", "crocodile"), Triple("乌龟", "Turtle", "turtle"),
            Triple("青蛙", "Frog", "frog"), Triple("蝴蝶", "Butterfly", "butterfly"),
            Triple("蜜蜂", "Bee", "bee"), Triple("瓢虫", "Ladybug", "ladybug"),
            Triple("螃蟹", "Crab", "crab"), Triple("龙虾", "Lobster", "lobster"),
            Triple("章鱼", "Octopus", "octopus"), Triple("鲸鱼", "Whale", "whale"),
            Triple("海豚", "Dolphin", "dolphin"), Triple("鲨鱼", "Shark", "shark"),
            Triple("海马", "Seahorse", "seahorse"), Triple("水母", "Jellyfish", "jellyfish")
        )
        
        animals.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 1).toLong(),
                nameCN = cn, nameEN = en, category = "动物世界",
                difficulty = if (index < 20) 1 else 2,
                descriptionCN = "可爱的${cn}", descriptionEN = "A cute ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"生命\", \"可爱\"]", scenarios = "[\"大自然\"]"
            ))
        }

        // 水果类 (101-130)
        val fruits = listOf(
            Triple("苹果", "Apple", "apple"), Triple("香蕉", "Banana", "banana"),
            Triple("橙子", "Orange", "orange"), Triple("葡萄", "Grape", "grape"),
            Triple("西瓜", "Watermelon", "watermelon"), Triple("草莓", "Strawberry", "strawberry"),
            Triple("菠萝", "Pineapple", "pineapple"), Triple("芒果", "Mango", "mango"),
            Triple("梨", "Pear", "pear"), Triple("桃子", "Peach", "peach"),
            Triple("樱桃", "Cherry", "cherry"), Triple("蓝莓", "Blueberry", "blueberry"),
            Triple("猕猴桃", "Kiwi", "kiwi"), Triple("柠檬", "Lemon", "lemon"),
            Triple("火龙果", "Dragonfruit", "dragonfruit"), Triple("哈密瓜", "Melon", "melon"),
            Triple("荔枝", "Lychee", "lychee"), Triple("椰子", "Coconut", "coconut"),
            Triple("石榴", "Pomegranate", "pomegranate"), Triple("柿子", "Persimmon", "persimmon"),
            Triple("山竹", "Mangosteen", "mangosteen"), Triple("柚子", "Pomelo", "pomelo"),
            Triple("木瓜", "Papaya", "papaya"), Triple("杏子", "Apricot", "apricot"),
            Triple("李子", "Plum", "plum"), Triple("无花果", "Fig", "fig"),
            Triple("杨桃", "Starfruit", "starfruit"), Triple("榴莲", "Durian", "durian"),
            Triple("蓝莓", "Blueberry", "blueberry_alt"), Triple("覆盆子", "Raspberry", "raspberry")
        )

        fruits.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 101).toLong(),
                nameCN = cn, nameEN = en, category = "美味水果",
                difficulty = 1,
                descriptionCN = "好吃的${cn}", descriptionEN = "Delicious ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"甜的\", \"多汁\"]", scenarios = "[\"水果店\"]"
            ))
        }

        // 蔬菜类 (201-230)
        val vegetables = listOf(
            Triple("胡萝卜", "Carrot", "carrot"), Triple("白菜", "Cabbage", "cabbage"),
            Triple("西红柿", "Tomato", "tomato"), Triple("西兰花", "Broccoli", "broccoli"),
            Triple("土豆", "Potato", "potato"), Triple("黄瓜", "Cucumber", "cucumber"),
            Triple("茄子", "Eggplant", "eggplant"), Triple("玉米", "Corn", "corn"),
            Triple("南瓜", "Pumpkin", "pumpkin"), Triple("洋葱", "Onion", "onion"),
            Triple("大蒜", "Garlic", "garlic"), Triple("辣椒", "Chili", "chili"),
            Triple("蘑菇", "Mushroom", "mushroom"), Triple("豌豆", "Pea", "pea"),
            Triple("菠菜", "Spinach", "spinach"), Triple("芹菜", "Celery", "celery"),
            Triple("萝卜", "Radish", "radish"), Triple("红薯", "Sweet Potato", "sweet_potato"),
            Triple("苦瓜", "Bitter Gourd", "bitter_gourd"), Triple("丝瓜", "Luffa", "luffa"),
            Triple("芦笋", "Asparagus", "asparagus"), Triple("甜椒", "Bell Pepper", "bell_pepper"),
            Triple("菜花", "Cauliflower", "cauliflower"), Triple("豆角", "Green Bean", "green_bean"),
            Triple("莲藕", "Lotus Root", "lotus_root"), Triple("竹笋", "Bamboo Shoot", "bamboo_shoot"),
            Triple("山药", "Yam", "yam"), Triple("冬瓜", "Wax Gourd", "wax_gourd"),
            Triple("荷兰豆", "Snow Pea", "snow_pea"), Triple("生菜", "Lettuce", "lettuce")
        )

        vegetables.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 201).toLong(),
                nameCN = cn, nameEN = en, category = "新鲜蔬菜",
                difficulty = 1,
                descriptionCN = "健康的${cn}", descriptionEN = "Healthy ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"绿色\", \"健康\"]", scenarios = "[\"菜园\"]"
            ))
        }

        // 交通工具 (301-335)
        val transport = listOf(
            Triple("汽车", "Car", "car"), Triple("公交车", "Bus", "bus"),
            Triple("飞机", "Airplane", "airplane"), Triple("自行车", "Bicycle", "bicycle"),
            Triple("摩托车", "Motorcycle", "motorcycle"), Triple("火车", "Train", "train"),
            Triple("高铁", "High-speed Train", "high_speed_train"), Triple("轮船", "Ship", "ship"),
            Triple("潜水艇", "Submarine", "submarine"), Triple("直升机", "Helicopter", "helicopter"),
            Triple("救护车", "Ambulance", "ambulance"), Triple("消防车", "Firetruck", "firetruck"),
            Triple("警车", "Police Car", "police_car"), Triple("卡车", "Truck", "truck"),
            Triple("拖拉机", "Tractor", "tractor"), Triple("热气球", "Hot Air Balloon", "hot_air_balloon"),
            Triple("飞船", "Spaceship", "spaceship"), Triple("坦克", "Tank", "tank"),
            Triple("挖掘机", "Excavator", "excavator"), Triple("帆船", "Sailboat", "sailboat"),
            Triple("出租车", "Taxi", "taxi"), Triple("吊车", "Crane", "crane"),
            Triple("缆车", "Cable Car", "cable_car"), Triple("划艇", "Canoe", "canoe"),
            Triple("飞艇", "Airship", "airship"), Triple("三轮车", "Tricycle", "tricycle"),
            Triple("滑板", "Skateboard", "skateboard"), Triple("压路机", "Steam Roller", "steam_roller"),
            Triple("垃圾车", "Garbage Truck", "garbage_truck"), Triple("拖船", "Tugboat", "tugboat"),
            Triple("叉车", "Forklift", "forklift"), Triple("赛车", "Racing Car", "racing_car"),
            Triple("房车", "RV", "rv"), Triple("直升飞机", "Chopper", "chopper"),
            Triple("电动车", "E-bike", "ebike")
        )

        transport.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 301).toLong(),
                nameCN = cn, nameEN = en, category = "交通工具",
                difficulty = if (index < 20) 1 else 2,
                descriptionCN = "快的${cn}", descriptionEN = "Fast ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"会动\", \"运输\"]", scenarios = "[\"马路\", \"天空\", \"大海\"]"
            ))
        }

        // 日常用品 (401-440)
        val daily = listOf(
            Triple("铅笔", "Pencil", "pencil"), Triple("杯子", "Cup", "cup"),
            Triple("书本", "Book", "book"), Triple("书包", "Schoolbag", "schoolbag"),
            Triple("牙刷", "Toothbrush", "toothbrush"), Triple("毛巾", "Towel", "towel"),
            Triple("梳子", "Comb", "comb"), Triple("镜子", "Mirror", "mirror"),
            Triple("伞", "Umbrella", "umbrella"), Triple("帽子", "Hat", "hat"),
            Triple("鞋子", "Shoes", "shoes"), Triple("衣服", "Clothes", "clothes"),
            Triple("床", "Bed", "bed"), Triple("椅子", "Chair", "chair"),
            Triple("桌子", "Desk", "desk"), Triple("灯", "Lamp", "lamp"),
            Triple("电视", "TV", "tv"), Triple("手机", "Phone", "phone"),
            Triple("电脑", "Computer", "computer"), Triple("钟表", "Clock", "clock"),
            Triple("剪刀", "Scissors", "scissors"), Triple("肥皂", "Soap", "soap"),
            Triple("盆", "Basin", "basin"), Triple("拖鞋", "Slippers", "slippers"),
            Triple("袜子", "Socks", "socks"), Triple("碗", "Bowl", "bowl"),
            Triple("勺子", "Spoon", "spoon"), Triple("筷子", "Chopsticks", "chopsticks"),
            Triple("叉子", "Fork", "fork"), Triple("锅", "Pot", "pot"),
            Triple("冰箱", "Fridge", "fridge"), Triple("洗衣机", "Washing Machine", "washing_machine"),
            Triple("空调", "Air Conditioner", "air_conditioner"), Triple("风扇", "Fan", "fan"),
            Triple("吹风机", "Hairdryer", "hairdryer"), Triple("钥匙", "Key", "key"),
            Triple("钱包", "Wallet", "wallet"), Triple("纸巾", "Tissue", "tissue"),
            Triple("玩具熊", "Teddy Bear", "teddy_bear"), Triple("积木", "Blocks", "blocks")
        )

        daily.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 401).toLong(),
                nameCN = cn, nameEN = en, category = "日常用品",
                difficulty = 1,
                descriptionCN = "常用的${cn}", descriptionEN = "Useful ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"日用\", \"生活\"]", scenarios = "[\"家里\"]"
            ))
        }

        // 自然现象 (501-520)
        val nature = listOf(
            Triple("太阳", "Sun", "sun"), Triple("月亮", "Moon", "moon"),
            Triple("星星", "Star", "star"), Triple("云朵", "Cloud", "cloud"),
            Triple("彩虹", "Rainbow", "rainbow"), Triple("雨", "Rain", "rain"),
            Triple("雪", "Snow", "snow"), Triple("风", "Wind", "wind"),
            Triple("雷电", "Lightning", "lightning"), Triple("大山", "Mountain", "mountain"),
            Triple("大海", "Ocean", "ocean"), Triple("森林", "Forest", "forest"),
            Triple("花朵", "Flower", "flower"), Triple("草地", "Grass", "grass"),
            Triple("树木", "Tree", "tree"), Triple("河流", "River", "river"),
            Triple("湖泊", "Lake", "lake"), Triple("火", "Fire", "fire"),
            Triple("岩石", "Rock", "rock"), Triple("岛屿", "Island", "island")
        )

        nature.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 501).toLong(),
                nameCN = cn, nameEN = en, category = "自然现象",
                difficulty = 2,
                descriptionCN = "奇妙的${cn}", descriptionEN = "Wonderful ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"自然\", \"景观\"]", scenarios = "[\"户外\"]"
            ))
        }

        // 食物与饮料 (601-625)
        val food = listOf(
            Triple("面包", "Bread", "bread"), Triple("牛奶", "Milk", "milk"),
            Triple("鸡蛋", "Egg", "egg"), Triple("蛋糕", "Cake", "cake"),
            Triple("饼干", "Cookie", "cookie"), Triple("糖果", "Candy", "candy"),
            Triple("冰淇淋", "Ice Cream", "ice_cream"), Triple("果汁", "Juice", "juice"),
            Triple("水", "Water", "water"), Triple("汉堡", "Burger", "burger"),
            Triple("薯条", "Fries", "fries"), Triple("披萨", "Pizza", "pizza"),
            Triple("面条", "Noodles", "noodles"), Triple("米饭", "Rice", "rice"),
            Triple("包子", "Steamed Bun", "steamed_bun"), Triple("饺子", "Dumpling", "dumpling"),
            Triple("巧克力", "Chocolate", "chocolate"), Triple("甜甜圈", "Donut", "donut"),
            Triple("三明治", "Sandwich", "sandwich"), Triple("寿司", "Sushi", "sushi"),
            Triple("汤", "Soup", "soup"), Triple("蜂蜜", "Honey", "honey"),
            Triple("奶酪", "Cheese", "cheese"), Triple("爆米花", "Popcorn", "popcorn"),
            Triple("棒棒糖", "Lollipop", "lollipop")
        )

        food.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 601).toLong(),
                nameCN = cn, nameEN = en, category = "食物与饮料",
                difficulty = 1,
                descriptionCN = "美味的${cn}", descriptionEN = "Tasty ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"好吃\", \"饮料\"]", scenarios = "[\"餐厅\", \"家里\"]"
            ))
        }

        // 身体部位 (701-715)
        val body = listOf(
            Triple("眼睛", "Eyes", "eyes"), Triple("鼻子", "Nose", "nose"),
            Triple("嘴巴", "Mouth", "mouth"), Triple("耳朵", "Ears", "ears"),
            Triple("头发", "Hair", "hair"), Triple("手", "Hand", "hand"),
            Triple("脚", "Foot", "foot"), Triple("胳膊", "Arm", "arm"),
            Triple("腿", "Leg", "leg"), Triple("头", "Head", "head"),
            Triple("手指", "Finger", "finger"), Triple("牙齿", "Teeth", "teeth"),
            Triple("舌头", "Tongue", "tongue"), Triple("肩膀", "Shoulder", "shoulder"),
            Triple("肚子", "Tummy", "tummy")
        )

        body.forEachIndexed { index, (cn, en, res) ->
            sampleItems.add(ItemEntity(
                id = (index + 701).toLong(),
                nameCN = cn, nameEN = en, category = "身体部位",
                difficulty = 2,
                descriptionCN = "我们的${cn}", descriptionEN = "Our ${en}",
                imageRes = res, audioCN = "${res}_cn", audioEN = "${res}_en",
                features = "[\"身体\", \"重要\"]", scenarios = "[\"我自己\"]"
            ))
        }

        sampleItems.forEach { item ->
            itemDao.insertItem(item)
        }
    }

    private suspend fun insertSampleAchievements() {
        val sampleAchievements = listOf(
            AchievementEntity(
                id = "first_explore",
                name = "初次探索",
                description = "完成第一次学习",
                iconRes = "🎯",
                type = "learning",
                requirement = "{ \"learned_count\": 1 }",
                reward = 10,
                unlocked = false
            ),
            AchievementEntity(
                id = "learning_master",
                name = "学习达人",
                description = "学习10个物品",
                iconRes = "📚",
                type = "learning",
                requirement = "{ \"learned_count\": 10 }",
                reward = 50,
                unlocked = false
            ),
            AchievementEntity(
                id = "game_master",
                name = "游戏高手",
                description = "完成5次游戏",
                iconRes = "🎮",
                type = "game",
                requirement = "{ \"game_count\": 5 }",
                reward = 30,
                unlocked = false
            ),
            AchievementEntity(
                id = "continuous_learning",
                name = "连续学习",
                description = "连续学习3天",
                iconRes = "🔥",
                type = "learning",
                requirement = "{ \"continuous_days\": 3 }",
                reward = 40,
                unlocked = false
            ),
            AchievementEntity(
                id = "all_knowing",
                name = "全知全能",
                description = "学习所有分类",
                iconRes = "🌟",
                type = "learning",
                requirement = "{ \"categories_learned\": 5 }",
                reward = 100,
                unlocked = false
            ),
            AchievementEntity(
                id = "perfect_answer",
                name = "完美答案",
                description = "连续答对10题",
                iconRes = "💯",
                type = "game",
                requirement = "{ \"correct_streak\": 10 }",
                reward = 60,
                unlocked = false
            )
        )

        sampleAchievements.forEach { achievement ->
            achievementDao.insertAchievement(achievement)
        }
    }
}
