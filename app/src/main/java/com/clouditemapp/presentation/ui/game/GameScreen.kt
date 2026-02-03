package com.clouditemapp.presentation.ui.game

import androidx.compose.animation.core.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.clouditemapp.presentation.viewmodel.GameViewModel
import com.clouditemapp.presentation.ui.common.ResourceUtils
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest

import com.airbnb.lottie.compose.*
import com.clouditemapp.R

import androidx.compose.ui.res.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val score by viewModel.score.collectAsState()
    val totalQuestions by viewModel.totalQuestions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val currentItems by viewModel.currentItems.collectAsState()
    val currentItem = currentItems.getOrNull(currentIndex)

    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(R.string.game_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
                    }
                },
                actions = {
                    if (gameState is GameViewModel.GameState.Playing) {
                        Text(
                            text = "得分: $score",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD),
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(skyGradient)
                .padding(paddingValues)
        ) {
            when (gameState) {
                is GameViewModel.GameState.Menu -> GameMenu(
                    onStartGuessGame = { viewModel.selectGameMode("guess") },
                    onStartListenGame = { viewModel.selectGameMode("listen") },
                    onShowLeaderboard = { viewModel.showLeaderboard() }
                )
                is GameViewModel.GameState.LevelSelection -> LevelSelectionScreen(
                    onLevelSelected = { category -> viewModel.startGame(category) },
                    onBack = { viewModel.goToMenu() }
                )
                is GameViewModel.GameState.Playing -> GamePlay(
                    currentItem = currentItem,
                    currentIndex = currentIndex,
                    totalQuestions = totalQuestions,
                    currentGameType = viewModel.currentGameType.collectAsState().value,
                    onAnswer = { isCorrect -> viewModel.answer(isCorrect) },
                    viewModel = viewModel
                )
                is GameViewModel.GameState.Leaderboard -> LeaderboardView(
                    topScores = viewModel.topScores.collectAsState().value,
                    onBack = { viewModel.goToMenu() }
                )
                is GameViewModel.GameState.Result -> GameResult(
                    result = gameState as GameViewModel.GameState.Result,
                    onPlayAgain = { viewModel.startGame(viewModel.selectedCategory.value) },
                    onBackToMenu = { viewModel.goToMenu() }
                )
            }
        }
    }
}

@Composable
fun LevelSelectionScreen(
    onLevelSelected: (String) -> Unit,
    onBack: () -> Unit
) {
    val levels = listOf(
        "动物世界", "美味水果", "新鲜蔬菜", "交通工具",
        "日常用品", "自然现象", "食物与饮料", "身体部位", "全部随机"
    )
    
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "选择关卡",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        levels.forEach { level ->
            LevelCard(
                title = level,
                onClick = { onLevelSelected(level) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF90A4AE)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("返回模式选择", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun LevelCard(
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (title == "全部随机") Color(0xFFBA68C8) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = title,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = if (title == "全部随机") Color.White else Color(0xFF0277BD)
            )
        }
    }
}

@Composable
fun GameMenu(
    onStartGuessGame: () -> Unit,
    onStartListenGame: () -> Unit,
    onShowLeaderboard: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.game_menu),
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.game_menu_desc),
            fontSize = 18.sp,
            color = Color(0xFF546E7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 猜猜看模式
        GameModeCard(
            title = stringResource(R.string.game_mode_guess),
            description = stringResource(R.string.game_mode_guess_desc),
            color = Color(0xFF81C784),
            onClick = onStartGuessGame
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 听音识图模式
        GameModeCard(
            title = stringResource(R.string.game_mode_listen),
            description = stringResource(R.string.game_mode_listen_desc),
            color = Color(0xFF64B5F6),
            onClick = onStartListenGame
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 排行榜入口
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { onShowLeaderboard() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFB74D)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 4.dp
            )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "🏆 排行榜",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun GameModeCard(
    title: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
fun LeaderboardView(
    topScores: List<com.clouditemapp.domain.model.GameRecord>,
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "🏆 荣誉殿堂",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (topScores.isEmpty()) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(text = "暂无记录，快去挑战吧！", color = Color(0xFF546E7A))
            }
        } else {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                androidx.compose.foundation.lazy.LazyColumn(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(topScores.size) { index ->
                        val record = topScores[index]
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    if (index < 3) Color(0xFFFFF9C4) else Color.Transparent,
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}",
                                modifier = Modifier.width(40.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (index < 3) Color(0xFFFF9800) else Color(0xFF546E7A)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "得分: ${record.score}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "正确率: ${(record.correctCount * 100 / record.totalCount)}%",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                            Text(
                                text = if (index == 0) "🥇" else if (index == 1) "🥈" else if (index == 2) "🥉" else "⭐",
                                fontSize = 24.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text("返回菜单", color = Color.White, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun GamePlay(
    currentItem: com.clouditemapp.domain.model.Item?,
    currentIndex: Int,
    totalQuestions: Int,
    currentGameType: String,
    onAnswer: (Boolean) -> Unit,
    viewModel: GameViewModel
) {
    val options by viewModel.options.collectAsState()
    var selectedOptionId by remember(currentIndex) { mutableStateOf<Long?>(null) }
    val context = LocalContext.current

    val answerScale by animateFloatAsState(
        targetValue = if (selectedOptionId != null) 1.05f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "answerScale"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 进度
        LinearProgressIndicator(
            progress = (currentIndex + 1).toFloat() / totalQuestions,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF81C784),
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "问题 ${currentIndex + 1} / $totalQuestions",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )

        Spacer(modifier = Modifier.height(16.dp))

        currentItem?.let { item ->
            // 问题卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (currentGameType == "guess") {
                        // 提示文字 (较大，居中，有呼吸效果)
                        val infiniteTransition = rememberInfiniteTransition(label = "hint")
                        val hintScale by infiniteTransition.animateFloat(
                            initialValue = 1f,
                            targetValue = 1.02f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(1000, easing = LinearEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "hintScale"
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.clickable { viewModel.playCurrentItemDescriptionAudio() }
                        ) {
                            Text(
                                text = item.descriptionCN,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0277BD),
                                textAlign = TextAlign.Center,
                                lineHeight = 38.sp,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .scale(hintScale)
                            )
                            Icon(
                                Icons.Default.VolumeUp,
                                contentDescription = "播放描述",
                                tint = Color(0xFF0277BD).copy(alpha = 0.5f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    } else {
                        // 播放按钮 (很大，适合幼儿)
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "点我听声音",
                                fontSize = 16.sp,
                                color = Color(0xFF0277BD),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE3F2FD))
                                    .clickable { viewModel.playCurrentItemAudio() },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.VolumeUp,
                                    contentDescription = "播放声音",
                                    modifier = Modifier.size(64.dp),
                                    tint = Color(0xFF0277BD)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 答案选项 (九宫格形式，显示图片)
            androidx.compose.foundation.lazy.grid.LazyVerticalGrid(
                columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f),
                contentPadding = PaddingValues(4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(options.size) { index ->
                    val option = options[index]
                    val isCorrect = option.id == item.id
                    val isSelected = selectedOptionId == option.id
                    val imageSource = ResourceUtils.getItemImageRes(context, option.imageRes, option.category)

                    val backgroundColor = when {
                        selectedOptionId == null -> Color.White
                        isSelected && isCorrect -> Color(0xFF81C784)
                        isSelected && !isCorrect -> Color(0xFFEF5350)
                        !isSelected && isCorrect -> Color(0xFF81C784).copy(alpha = 0.5f)
                        else -> Color.White
                    }

                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .scale(if (isSelected) answerScale else 1f)
                            .clickable(enabled = selectedOptionId == null) {
                                selectedOptionId = option.id
                                onAnswer(isCorrect)
                            },
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = backgroundColor
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 4.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(context)
                                    .data(imageSource)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = option.nameCN,
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(8.dp)
                            )
                            if (selectedOptionId != null) {
                                Text(
                                    text = option.nameCN,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else Color(0xFF0277BD)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GameResult(
    result: GameViewModel.GameState.Result,
    onPlayAgain: () -> Unit,
    onBackToMenu: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val correctPercentage = if (result.totalCount > 0) {
        (result.correctCount * 100) / result.totalCount
    } else {
        0
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Lottie 动画展示
        Box(modifier = Modifier.size(200.dp)) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "游戏结束！",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // 结果卡片
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ResultItem(
                    label = "得分",
                    value = "${result.score}",
                    icon = "🏆"
                )

                Spacer(modifier = Modifier.height(16.dp))

                ResultItem(
                    label = "正确率",
                    value = "$correctPercentage%",
                    icon = "🎯"
                )

                Spacer(modifier = Modifier.height(16.dp))

                ResultItem(
                    label = "正确数量",
                    value = "${result.correctCount} / ${result.totalCount}",
                    icon = "✅"
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // 按钮
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = onBackToMenu,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF90A4AE)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "返回菜单",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Button(
                onClick = onPlayAgain,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF81C784)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "再玩一次",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun ResultItem(
    label: String,
    value: String,
    icon: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$icon $label",
            fontSize = 20.sp,
            color = Color(0xFF546E7A)
        )

        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD)
        )
    }
}