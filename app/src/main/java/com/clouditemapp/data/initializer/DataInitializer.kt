package com.clouditemapp.data.initializer

import android.content.Context
import com.clouditemapp.data.local.dao.AchievementDao
import com.clouditemapp.data.local.dao.ItemDao
import com.clouditemapp.data.local.entity.AchievementEntity
import com.clouditemapp.data.local.entity.ItemEntity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
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
            try {
                // 检查是否已经初始化
                val items = itemDao.getAllItems().first()
                if (items.isEmpty()) {
                    android.util.Log.d("DataInitializer", "Starting data initialization...")
                    insertSampleItems()
                    insertSampleAchievements()
                    android.util.Log.d("DataInitializer", "Data initialization completed successfully.")
                } else {
                    android.util.Log.d("DataInitializer", "Data already initialized, skipping.")
                }
            } catch (e: Exception) {
                android.util.Log.e("DataInitializer", "Error during data initialization: ${e.message}", e)
            }
        }
    }

    private suspend fun insertSampleItems() {
        val sampleItems = mutableListOf<ItemEntity>()
        
        // 动物类 (1-40) - 谜语风格描述
        val animals = listOf(
            Triple("猫", "Cat", "喵喵叫，爱抓老鼠，胡须长长的小动物"),
            Triple("狗", "Dog", "汪汪叫，人类的好朋友，会看家护院"),
            Triple("兔子", "Rabbit", "长耳朵，短尾巴，爱吃胡萝卜蹦蹦跳"),
            Triple("小鸟", "Bird", "长着翅膀，在树上筑巢，会叽叽喳喳唱歌"),
            Triple("大象", "Elephant", "身体像小山，耳朵像大扇子，鼻子长长的"),
            Triple("老虎", "Tiger", "森林之王，身上长满黑色的条纹，非常威风"),
            Triple("狮子", "Lion", "长着浓密的鬃毛，吼声很大，被称为万兽之王"),
            Triple("长颈鹿", "Giraffe", "脖子非常长，能吃到树顶上的叶子"),
            Triple("斑马", "Zebra", "身上长满黑白相间的条纹，像穿了件斑马线衣服"),
            Triple("猴子", "Monkey", "爱吃香蕉，喜欢在树间荡来荡去，非常聪明"),
            Triple("熊猫", "Panda", "黑眼圈，胖乎乎，最喜欢吃翠绿的竹子"),
            Triple("考拉", "Koala", "总是抱着树睡觉，看起来懒洋洋的澳洲小动物"),
            Triple("企鹅", "Penguin", "穿着“燕尾服”，走起路来摇摇摆摆，住在冰天雪地里"),
            Triple("猪", "Pig", "大耳朵，大肚子，鼻子圆圆的，睡起觉来呼噜响"),
            Triple("牛", "Cow", "头上有角，辛勤耕地，还能挤出好喝的牛奶"),
            Triple("羊", "Sheep", "身上长满白白的卷毛，像一朵朵白云在草地上跑"),
            Triple("马", "Horse", "跑得飞快，长着漂亮的鬃毛，古代人的交通工具"),
            Triple("鸡", "Chicken", "尖尖嘴，红鸡冠，早起喔喔叫，提醒大家起床"),
            Triple("鸭", "Duck", "扁扁嘴，脚上有蹼，走起路来摇摇摆摆，喜欢在水里游"),
            Triple("熊", "Bear", "身体强壮，毛茸茸的，喜欢吃蜂蜜，冬天会冬眠"),
            Triple("狐狸", "Fox", "长尾巴，尖耳朵，看起来很机灵，住在洞穴里"),
            Triple("鹿", "Deer", "头上有像树枝一样的角，胆子很小，跑得很快"),
            Triple("刺猬", "Hedgehog", "身上长满尖尖的刺，遇到危险会缩成一个球"),
            Triple("松鼠", "Squirrel", "长着蓬松的大尾巴，喜欢收集坚果藏在树洞里"),
            Triple("骆驼", "Camel", "背上有驼峰，能在干旱的沙漠里走很长路"),
            Triple("蛇", "Snake", "身体细长，没有脚，走起路来弯弯曲曲地爬"),
            Triple("鳄鱼", "Crocodile", "嘴巴很大，牙齿尖尖，披着绿色的硬甲皮"),
            Triple("乌龟", "Turtle", "背着重重的壳，爬得很慢，遇到危险头会缩进去"),
            Triple("青蛙", "Frog", "绿衣裳，大嘴巴，害虫天敌，田野里的歌唱家"),
            Triple("蝴蝶", "Butterfly", "翅膀五颜六色，在花丛中翩翩起舞，非常漂亮"),
            Triple("蜜蜂", "Bee", "勤劳的小工人，飞在花丛中采蜜，发出嗡嗡声"),
            Triple("瓢虫", "Ladybug", "圆圆的身体，红衣服黑点点，像个小红球"),
            Triple("螃蟹", "Crab", "披着硬壳，长着八只脚，横着走，还有大钳子"),
            Triple("龙虾", "Lobster", "生活在海底，穿着红盔甲，长长的胡须大钳子"),
            Triple("章鱼", "Octopus", "生活在大海里，长着八条长长的触手，会喷墨汁"),
            Triple("鲸鱼", "Whale", "世界上最大的动物，生活在海里，头顶会喷水柱"),
            Triple("海豚", "Dolphin", "非常聪明，皮肤滑溜溜，喜欢在海面上跳跃"),
            Triple("鲨鱼", "Shark", "牙齿尖利，是海洋里的猎手，背上有个三角形的鳍"),
            Triple("海马", "Seahorse", "头长得像小马，尾巴卷卷的，生活在温暖的海底"),
            Triple("水母", "Jellyfish", "透明的身体，像一把撑开的小伞，在水里漂啊漂")
        )
        
        animals.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 1).toLong(),
                nameCN = cn, nameEN = en, category = "动物世界",
                difficulty = if (index < 20) 1 else 2,
                descriptionCN = desc, descriptionEN = "A child-friendly description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"生命\", \"可爱\"]", scenarios = "[\"大自然\"]"
            ))
        }

        // 水果类 (101-130)
        val fruits = listOf(
            Triple("苹果", "Apple", "红红的圆果子，吃起来脆脆的，又甜又多汁"),
            Triple("香蕉", "Banana", "弯弯像月亮，剥开黄外皮，果肉软软糯又甜"),
            Triple("橙子", "Orange", "圆圆的，橘色的皮，剥开一瓣瓣，酸酸甜甜水分足"),
            Triple("葡萄", "Grape", "一串串，紫晶莹，像一颗颗小圆珠，酸甜可口"),
            Triple("西瓜", "Watermelon", "绿衣服，红肚子，黑籽籽，夏天解暑最好吃"),
            Triple("草莓", "Strawberry", "红彤彤，心形脸，身上长满小芝麻点点"),
            Triple("菠萝", "Pineapple", "披着盔甲，长着绿叶冠，味道酸甜有清香"),
            Triple("芒果", "Mango", "金黄的皮，椭圆形状，果肉细腻，香甜浓郁"),
            Triple("梨", "Pear", "上小下大，像个小葫芦，果肉洁白很清甜"),
            Triple("桃子", "Peach", "粉红脸蛋毛茸茸，肉质鲜嫩汁水多"),
            Triple("樱桃", "Cherry", "小巧玲珑红透了，像一颗颗红色的小宝石"),
            Triple("蓝莓", "Blueberry", "深蓝色的小圆球，酸甜可口营养丰富"),
            Triple("猕猴桃", "Kiwi", "棕色毛皮，绿色心，黑籽点点营养高"),
            Triple("柠檬", "Lemon", "黄黄的果皮，味道特别酸，能泡出好喝的水"),
            Triple("火龙果", "Dragonfruit", "红皮绿鳞，像个小火球，里面黑籽密密麻麻"),
            Triple("哈密瓜", "Melon", "披着网状绿衣服，果肉橘红非常甜"),
            Triple("荔枝", "Lychee", "红皮凹凸不平，剥开像珍珠，晶莹剔透好滋味"),
            Triple("椰子", "Coconut", "硬硬的棕壳，里面有清甜的水，还有白白的肉"),
            Triple("石榴", "Pomegranate", "红红的像小灯笼，剥开里面满是红珍珠"),
            Triple("柿子", "Persimmon", "橘红圆脸蛋，熟透了像蜜一样甜"),
            Triple("山竹", "Mangosteen", "紫黑外皮硬邦邦，剥开肉像白蒜瓣"),
            Triple("柚子", "Pomelo", "大大的圆球，厚厚的黄皮，果肉酸甜一瓣瓣"),
            Triple("木瓜", "Papaya", "黄皮红心，果肉软糯，香气清新很健康"),
            Triple("杏子", "Apricot", "黄里透红，圆润可爱，酸酸甜甜有嚼劲"),
            Triple("李子", "Plum", "紫红或深红，圆圆的一颗，酸甜适口很开胃"),
            Triple("无花果", "Fig", "紫皮红心，样子很独特，味道香甜营养好"),
            Triple("杨桃", "Starfruit", "横着切一刀，就是漂亮的五角星形状"),
            Triple("榴莲", "Durian", "披着尖刺盔甲，闻着臭吃着香，热带果王"),
            Triple("覆盆子", "Raspberry", "小小的红果子，像一顶顶小帽子，酸甜诱人")
        )

        fruits.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 101).toLong(),
                nameCN = cn, nameEN = en, category = "美味水果",
                difficulty = 1,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"甜的\", \"多汁\"]", scenarios = "[\"水果店\"]"
            ))
        }

        // 蔬菜类 (201-230)
        val vegetables = listOf(
            Triple("胡萝卜", "Carrot", "橘红身子尖尖头，小兔子最爱吃它了"),
            Triple("白菜", "Cabbage", "白白的帮，绿绿的叶，一层层裹成圆球状"),
            Triple("西红柿", "Tomato", "红红的圆脸蛋，能当水果也能做菜"),
            Triple("西兰花", "Broccoli", "绿绿的，像一棵棵迷你小树林"),
            Triple("土豆", "Potato", "土里长的圆疙瘩，削了皮做薯条最好吃"),
            Triple("黄瓜", "Cucumber", "细长身子绿皮衣，吃起来清脆又爽口"),
            Triple("茄子", "Eggplant", "紫亮的外皮，弯弯或长圆，肉质软绵绵"),
            Triple("玉米", "Corn", "穿着绿衣服，排着整齐的金豆豆，甜甜的"),
            Triple("南瓜", "Pumpkin", "橘黄大圆脸，万圣节的小灯笼，味道甜甜糯糯"),
            Triple("洋葱", "Onion", "紫红圆球一层层，剥它的时候会让人流泪"),
            Triple("大蒜", "Garlic", "白白蒜瓣聚成团，味道辛辣能除菌"),
            Triple("辣椒", "Chili", "红红或绿绿，身材细长，吃一口嘴巴火辣辣"),
            Triple("蘑菇", "Mushroom", "像一把把撑开的小雨伞，长在阴凉潮湿的地方"),
            Triple("豌豆", "Pea", "绿绿的小房子，住着圆圆的绿豆宝宝"),
            Triple("菠菜", "Spinach", "绿绿的叶子红红的根，大力水手最爱吃"),
            Triple("芹菜", "Celery", "长长的杆子，清脆有嚼劲，还有特殊香气"),
            Triple("萝卜", "Radish", "白白胖胖土里钻，吃起来清脆水分足"),
            Triple("红薯", "Sweet Potato", "红皮黄心，烤着吃又香又甜又糯"),
            Triple("苦瓜", "Bitter Gourd", "长满疙瘩绿皮衣，味道苦苦但很健康"),
            Triple("丝瓜", "Luffa", "长长的绿身子，里面有很多丝，清热解暑"),
            Triple("莲藕", "Lotus Root", "长在泥潭里，切开有很多圆圆的小孔"),
            Triple("生菜", "Lettuce", "绿绿的叶子大大的，吃烤肉时最喜欢包着它")
        )

        vegetables.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 201).toLong(),
                nameCN = cn, nameEN = en, category = "新鲜蔬菜",
                difficulty = 1,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"绿色\", \"健康\"]", scenarios = "[\"菜园\"]"
            ))
        }

        // 交通工具 (301-335)
        val transport = listOf(
            Triple("汽车", "Car", "有四个轮子，嘟嘟响，在马路上带我们去远方"),
            Triple("公交车", "Bus", "长长的车身很多座，带大家一起出门旅行"),
            Triple("飞机", "Airplane", "长着大翅膀，在蓝天白云间飞翔，速度非常快"),
            Triple("自行车", "Bicycle", "两个轮子，要用脚踩，既能锻炼又能代步"),
            Triple("摩托车", "Motorcycle", "发出轰鸣声，跑得很快，戴上头盔真威风"),
            Triple("火车", "Train", "长长的车厢连成串，在铁轨上跑得又稳又远"),
            Triple("高铁", "High-speed Train", "像一道闪电在铁轨上飞过，速度超级快"),
            Triple("轮船", "Ship", "巨大的身体在大海里航行，带人们去远方"),
            Triple("潜水艇", "Submarine", "在大海深处游动，像一条巨大的铁鱼"),
            Triple("直升机", "Helicopter", "头顶螺旋桨转啊转，能原地起飞和停在空中"),
            Triple("救护车", "Ambulance", "呜哇呜哇响，争分夺秒救助生病的人"),
            Triple("消防车", "Firetruck", "红红的身体大水炮，勇敢地去灭火"),
            Triple("警车", "Police Car", "蓝红灯光闪啊闪，警察叔叔开着它抓坏人"),
            Triple("卡车", "Truck", "力气非常大，能运送很多重重的货物"),
            Triple("挖掘机", "Excavator", "长着有力的大铁臂，在工地上挖土忙"),
            Triple("热气球", "Hot Air Balloon", "大大的圆球飘在空中，带人们看美丽的风景"),
            Triple("飞船", "Spaceship", "飞向遥远的宇宙，探索星星和月亮的秘密")
        )

        transport.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 301).toLong(),
                nameCN = cn, nameEN = en, category = "交通工具",
                difficulty = if (index < 20) 1 else 2,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"会动\", \"运输\"]", scenarios = "[\"马路\", \"天空\", \"大海\"]"
            ))
        }

        // 日常用品 (401-440)
        val daily = listOf(
            Triple("铅笔", "Pencil", "细细长长，写字画画都要用到它"),
            Triple("杯子", "Cup", "用来盛水喝，我们每天都要用它补充水分"),
            Triple("书本", "Book", "里面有很多有趣的故事和知识"),
            Triple("书包", "Schoolbag", "背在背上，装着书本去上学"),
            Triple("牙刷", "Toothbrush", "小刷子刷刷牙，让牙齿白白又整洁"),
            Triple("毛巾", "Towel", "洗完脸擦擦水，软绵绵的很舒服"),
            Triple("梳子", "Comb", "梳理头发乱糟糟，让发型变得整齐"),
            Triple("伞", "Umbrella", "下雨天撑开它，就不会被淋湿了"),
            Triple("帽子", "Hat", "戴在头上遮太阳，或者让小朋友变得更帅气"),
            Triple("鞋子", "Shoes", "穿在脚上走走路，保护脚丫不受伤"),
            Triple("衣服", "Clothes", "穿在身上保暖又漂亮"),
            Triple("床", "Bed", "软绵绵的，晚上睡个香甜的好觉"),
            Triple("椅子", "Chair", "有靠背，让我们坐下来休息"),
            Triple("桌子", "Desk", "平平的台面，可以在上面吃饭或写作业"),
            Triple("灯", "Lamp", "黑夜里发出亮光，照亮房间"),
            Triple("电视", "TV", "屏幕里有动画片和精彩的世界"),
            Triple("手机", "Phone", "小小机器能通话，还能拍照看视频"),
            Triple("钟表", "Clock", "滴答滴答走，告诉我们现在是几点"),
            Triple("剪刀", "Scissors", "锋利的两片嘴，能剪纸也能剪布"),
            Triple("碗", "Bowl", "圆圆的，用来装香喷喷的米饭"),
            Triple("勺子", "Spoon", "舀起汤水送进嘴，吃饭的小帮手"),
            Triple("筷子", "Chopsticks", "细长两根，中国人吃饭必不可少的工具"),
            Triple("冰箱", "Fridge", "肚子里冷冰冰，能让食物新鲜不坏"),
            Triple("洗衣机", "Washing Machine", "转啊转，把脏衣服洗得干干净净"),
            Triple("吹风机", "Hairdryer", "吹出暖风，让湿头发变干快"),
            Triple("钥匙", "Key", "小铁片能开门，保护我们的家"),
            Triple("玩具熊", "Teddy Bear", "毛茸茸的，抱在怀里很有安全感"),
            Triple("积木", "Blocks", "方块圆块堆一堆，拼出想要的大城堡")
        )

        daily.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 401).toLong(),
                nameCN = cn, nameEN = en, category = "日常用品",
                difficulty = 1,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"日用\", \"生活\"]", scenarios = "[\"家里\"]"
            ))
        }

        // 自然现象 (501-520)
        val nature = listOf(
            Triple("太阳", "Sun", "大火球挂天上，白天给我们带来光和热"),
            Triple("月亮", "Moon", "有时圆圆像银盘，有时弯弯像小船"),
            Triple("星星", "Star", "黑夜里眨眼睛，亮晶晶地挂在夜空"),
            Triple("云朵", "Cloud", "像棉花糖一样在蓝天里飘动"),
            Triple("彩虹", "Rainbow", "雨后出现的七色桥，横跨在天空"),
            Triple("雨", "Rain", "从云里落下来的小水滴，滋润大地"),
            Triple("雪", "Snow", "白茫茫的一片，冬天从天上飘落"),
            Triple("大山", "Mountain", "高耸入云，屹立在大地上"),
            Triple("大海", "Ocean", "无边无际的蓝色水域，浪花朵朵"),
            Triple("森林", "Forest", "长满大树的地方，是动物们的家"),
            Triple("花朵", "Flower", "五颜六色很芬芳，在枝头悄悄开放")
        )

        nature.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 501).toLong(),
                nameCN = cn, nameEN = en, category = "自然现象",
                difficulty = 2,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"自然\", \"景观\"]", scenarios = "[\"户外\"]"
            ))
        }

        // 食物与饮料 (601-625)
        val food = listOf(
            Triple("面包", "Bread", "软绵绵，香喷喷，是好吃的早点"),
            Triple("牛奶", "Milk", "白白的液体，喝了它小朋友长得高"),
            Triple("鸡蛋", "Egg", "椭圆外壳，营养丰富，能煎也能煮"),
            Triple("蛋糕", "Cake", "过生日必备，甜甜的，有漂亮的奶油"),
            Triple("饼干", "Cookie", "薄薄的一片，又酥又脆，各种形状都有"),
            Triple("糖果", "Candy", "五颜六色，吃到嘴里甜丝丝"),
            Triple("冰淇淋", "Ice Cream", "冰冰凉凉，入口即化，夏天最受欢迎"),
            Triple("果汁", "Juice", "水果榨出来的水，各种味道酸甜好喝"),
            Triple("水", "Water", "透明的，生命离不开它，我们要多喝水"),
            Triple("汉堡", "Burger", "面包夹肉片，快餐店里的明星"),
            Triple("薯条", "Fries", "土豆切成长条炸，蘸着番茄酱最好吃"),
            Triple("披萨", "Pizza", "大圆饼盖满料，切成小片大家分"),
            Triple("面条", "Noodles", "长长一根根，溜滑顺口很有趣"),
            Triple("米饭", "Rice", "白白的小颗粒，是我们每天的主食"),
            Triple("饺子", "Dumpling", "像个小耳朵，里面包着香香的馅"),
            Triple("巧克力", "Chocolate", "棕色的，味道香醇，甜中带点苦")
        )

        food.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 601).toLong(),
                nameCN = cn, nameEN = en, category = "食物与饮料",
                difficulty = 1,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"好吃\", \"饮料\"]", scenarios = "[\"餐厅\", \"家里\"]"
            ))
        }

        // 身体部位 (701-715)
        val body = listOf(
            Triple("眼睛", "Eyes", "心灵的小窗户，让我们看清美丽的世界"),
            Triple("鼻子", "Nose", "长在脸中央，能闻到各种各样的气味"),
            Triple("嘴巴", "Mouth", "用来吃饭说话，还能露出甜美的笑容"),
            Triple("耳朵", "Ears", "长在头两边，帮我们听到好听的声音"),
            Triple("头发", "Hair", "长在头顶，能变换各种漂亮的造型"),
            Triple("手", "Hand", "长着十个手指头，能写字也能拿东西"),
            Triple("脚", "Foot", "穿上小鞋子，带我们走遍天下"),
            Triple("头", "Head", "身体的总司令，思考问题全靠它"),
            Triple("手指", "Finger", "灵巧的小棒，能帮我们做精细的事")
        )

        body.forEachIndexed { index, (cn, en, desc) ->
            sampleItems.add(ItemEntity(
                id = (index + 701).toLong(),
                nameCN = cn, nameEN = en, category = "身体部位",
                difficulty = 2,
                descriptionCN = desc, descriptionEN = "A description of $en",
                imageRes = cnToRes(cn), audioCN = "${cnToRes(cn)}_cn", audioEN = "${cnToRes(cn)}_en",
                audioDescCN = "${cnToRes(cn)}_desc_cn",
                features = "[\"身体\", \"重要\"]", scenarios = "[\"我自己\"]"
            ))
        }

        sampleItems.forEach { item ->
            itemDao.insertItem(item)
        }
    }

    private fun cnToRes(cn: String): String {
        return when(cn) {
            "苹果" -> "apple"
            "香蕉" -> "banana"
            "橙子" -> "orange"
            "葡萄" -> "grape"
            "西瓜" -> "watermelon"
            "草莓" -> "strawberry"
            "菠萝" -> "pineapple"
            "芒果" -> "mango"
            "梨" -> "pear"
            "桃子" -> "peach"
            "樱桃" -> "cherry"
            "蓝莓" -> "blueberry"
            "猕猴桃" -> "kiwi"
            "柠檬" -> "lemon"
            "火龙果" -> "dragonfruit"
            "哈密瓜" -> "melon"
            "荔枝" -> "lychee"
            "椰子" -> "coconut"
            "石榴" -> "pomegranate"
            "柿子" -> "persimmon"
            "山竹" -> "mangosteen"
            "柚子" -> "pomelo"
            "木瓜" -> "papaya"
            "杏子" -> "apricot"
            "李子" -> "plum"
            "无花果" -> "fig"
            "杨桃" -> "starfruit"
            "榴莲" -> "durian"
            "覆盆子" -> "raspberry"
            "胡萝卜" -> "carrot"
            "白菜" -> "cabbage"
            "西红柿" -> "tomato"
            "西兰花" -> "broccoli"
            "土豆" -> "potato"
            "黄瓜" -> "cucumber"
            "茄子" -> "eggplant"
            "玉米" -> "corn"
            "南瓜" -> "pumpkin"
            "洋葱" -> "onion"
            "大蒜" -> "garlic"
            "辣椒" -> "chili"
            "蘑菇" -> "mushroom"
            "豌豆" -> "pea"
            "菠菜" -> "spinach"
            "芹菜" -> "celery"
            "萝卜" -> "radish"
            "红薯" -> "sweet_potato"
            "苦瓜" -> "bitter_gourd"
            "丝瓜" -> "luffa"
            "芦笋" -> "asparagus"
            "甜椒" -> "bell_pepper"
            "菜花" -> "cauliflower"
            "豆角" -> "green_bean"
            "莲藕" -> "lotus_root"
            "竹笋" -> "bamboo_shoot"
            "山药" -> "yam"
            "冬瓜" -> "wax_gourd"
            "荷兰豆" -> "snow_pea"
            "生菜" -> "lettuce"
            "汽车" -> "car"
            "公交车" -> "bus"
            "飞机" -> "airplane"
            "自行车" -> "bicycle"
            "摩托车" -> "motorcycle"
            "火车" -> "train"
            "高铁" -> "high_speed_train"
            "轮船" -> "ship"
            "潜水艇" -> "submarine"
            "直升机" -> "helicopter"
            "救护车" -> "ambulance"
            "消防车" -> "firetruck"
            "警车" -> "police_car"
            "卡车" -> "truck"
            "拖拉机" -> "tractor"
            "热气球" -> "hot_air_balloon"
            "飞船" -> "spaceship"
            "坦克" -> "tank"
            "挖掘机" -> "excavator"
            "帆船" -> "sailboat"
            "出租车" -> "taxi"
            "吊车" -> "crane"
            "缆车" -> "cable_car"
            "划艇" -> "canoe"
            "飞艇" -> "airship"
            "三轮车" -> "tricycle"
            "滑板" -> "skateboard"
            "压路机" -> "steam_roller"
            "垃圾车" -> "garbage_truck"
            "拖船" -> "tugboat"
            "叉车" -> "forklift"
            "赛车" -> "racing_car"
            "房车" -> "rv"
            "直升飞机" -> "chopper"
            "电动车" -> "ebike"
            "铅笔" -> "pencil"
            "杯子" -> "cup"
            "书本" -> "book"
            "书包" -> "schoolbag"
            "牙刷" -> "toothbrush"
            "毛巾" -> "towel"
            "梳子" -> "comb"
            "镜子" -> "mirror"
            "伞" -> "umbrella"
            "帽子" -> "hat"
            "鞋子" -> "shoes"
            "衣服" -> "clothes"
            "床" -> "bed"
            "椅子" -> "chair"
            "桌子" -> "desk"
            "灯" -> "lamp"
            "电视" -> "tv"
            "手机" -> "phone"
            "电脑" -> "computer"
            "钟表" -> "clock"
            "剪刀" -> "scissors"
            "肥皂" -> "soap"
            "盆" -> "basin"
            "拖鞋" -> "slippers"
            "袜子" -> "socks"
            "碗" -> "bowl"
            "勺子" -> "spoon"
            "筷子" -> "chopsticks"
            "叉子" -> "fork"
            "锅" -> "pot"
            "冰箱" -> "fridge"
            "洗衣机" -> "washing_machine"
            "空调" -> "air_conditioner"
            "风扇" -> "fan"
            "吹风机" -> "hairdryer"
            "钥匙" -> "key"
            "钱包" -> "wallet"
            "纸巾" -> "tissue"
            "玩具熊" -> "teddy_bear"
            "积木" -> "blocks"
            "太阳" -> "sun"
            "月亮" -> "moon"
            "星星" -> "star"
            "云朵" -> "cloud"
            "彩虹" -> "rainbow"
            "雨" -> "rain"
            "雪" -> "snow"
            "风" -> "wind"
            "雷电" -> "lightning"
            "大山" -> "mountain"
            "大海" -> "ocean"
            "森林" -> "forest"
            "花朵" -> "flower"
            "草地" -> "grass"
            "树木" -> "tree"
            "河流" -> "river"
            "湖泊" -> "lake"
            "火" -> "fire"
            "岩石" -> "rock"
            "岛屿" -> "island"
            "面包" -> "bread"
            "牛奶" -> "milk"
            "鸡蛋" -> "egg"
            "蛋糕" -> "cake"
            "饼干" -> "cookie"
            "糖果" -> "candy"
            "冰淇淋" -> "ice_cream"
            "果汁" -> "juice"
            "水" -> "water"
            "汉堡" -> "burger"
            "薯条" -> "fries"
            "披萨" -> "pizza"
            "面条" -> "noodles"
            "米饭" -> "rice"
            "包子" -> "steamed_bun"
            "饺子" -> "dumpling"
            "巧克力" -> "chocolate"
            "甜甜圈" -> "donut"
            "三明治" -> "sandwich"
            "寿司" -> "sushi"
            "汤" -> "soup"
            "蜂蜜" -> "honey"
            "奶酪" -> "cheese"
            "爆米花" -> "popcorn"
            "棒棒糖" -> "lollipop"
            "眼睛" -> "eyes"
            "鼻子" -> "nose"
            "嘴巴" -> "mouth"
            "耳朵" -> "ears"
            "头发" -> "hair"
            "手" -> "hand"
            "脚" -> "foot"
            "胳膊" -> "arm"
            "腿" -> "leg"
            "头" -> "head"
            "手指" -> "finger"
            "牙齿" -> "teeth"
            "舌头" -> "tongue"
            "肩膀" -> "shoulder"
            "肚子" -> "tummy"
            "猫" -> "cat"
            "狗" -> "dog"
            "兔子" -> "rabbit"
            "小鸟" -> "bird"
            "大象" -> "elephant"
            "老虎" -> "tiger"
            "狮子" -> "lion"
            "长颈鹿" -> "giraffe"
            "斑马" -> "zebra"
            "猴子" -> "monkey"
            "熊猫" -> "panda"
            "考拉" -> "koala"
            "企鹅" -> "penguin"
            "猪" -> "pig"
            "牛" -> "cow"
            "羊" -> "sheep"
            "马" -> "horse"
            "鸡" -> "chicken"
            "鸭" -> "duck"
            "熊" -> "bear"
            "狐狸" -> "fox"
            "鹿" -> "deer"
            "刺猬" -> "hedgehog"
            "松鼠" -> "squirrel"
            "骆驼" -> "camel"
            "蛇" -> "snake"
            "鳄鱼" -> "crocodile"
            "乌龟" -> "turtle"
            "青蛙" -> "frog"
            "蝴蝶" -> "butterfly"
            "蜜蜂" -> "bee"
            "瓢虫" -> "ladybug"
            "螃蟹" -> "crab"
            "龙虾" -> "lobster"
            "章鱼" -> "octopus"
            "鲸鱼" -> "whale"
            "海豚" -> "dolphin"
            "鲨鱼" -> "shark"
            "海马" -> "seahorse"
            "水母" -> "jellyfish"
            else -> "ic_placeholder_default"
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
