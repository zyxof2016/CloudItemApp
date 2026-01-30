package com.clouditemapp.presentation.ui.game

import androidx.compose.animation.core.*
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

import com.airbnb.lottie.compose.*
import com.clouditemapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameScreen(
    navController: NavController,
    viewModel: GameViewModel = hiltViewModel()
) {
    val gameState by viewModel.gameState.collectAsState()
    val score by viewModel.score.collectAsState()
    val correctCount by viewModel.correctCount.collectAsState()
    val totalQuestions by viewModel.totalQuestions.collectAsState()
    val currentIndex by viewModel.currentIndex.collectAsState()
    val currentItem by remember { derivedStateOf { viewModel.getCurrentItem() } }

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
                        "🎮 趣味游戏",
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
                    onStartGame = { viewModel.startGame() },
                    onShowLeaderboard = { viewModel.showLeaderboard() }
                )
                is GameViewModel.GameState.Playing -> GamePlay(
                    currentItem = currentItem,
                    currentIndex = currentIndex,
                    totalQuestions = totalQuestions,
                    onAnswer = { isCorrect -> viewModel.answer(isCorrect) },
                    viewModel = viewModel
                )
                is GameViewModel.GameState.Leaderboard -> LeaderboardView(
                    topScores = viewModel.topScores.collectAsState().value,
                    onBack = { viewModel.goToMenu() }
                )
                is GameViewModel.GameState.Result -> GameResult(
                    result = gameState as GameViewModel.GameState.Result,
                    onPlayAgain = { viewModel.startGame() },
                    onBackToMenu = { viewModel.goToMenu() }
                )
            }
        }
    }
}

@Composable
fun GameMenu(
    onStartGame: () -> Unit,
    onShowLeaderboard: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎯 猜猜看游戏",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0277BD),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "根据物品特征，猜猜是什么",
            fontSize = 18.sp,
            color = Color(0xFF546E7A),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clickable { onStartGame() },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF81C784)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🚀 开始游戏",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clickable { onShowLeaderboard() },
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFB74D)
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 8.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "🏆 排行榜",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
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
    onAnswer: (Boolean) -> Unit,
    viewModel: GameViewModel
) {
    val options by viewModel.options.collectAsState()
    var selectedOption by remember(currentIndex) { mutableStateOf<String?>(null) }

    val answerScale by animateFloatAsState(
        targetValue = if (selectedOption != null) 1.05f else 1f,
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

        Spacer(modifier = Modifier.height(32.dp))

        currentItem?.let { item ->
            // 问题卡片
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 12.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "这是什么？",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // 提示
                    Text(
                        text = "提示: ${item.descriptionCN}",
                        fontSize = 20.sp,
                        color = Color(0xFF546E7A),
                        textAlign = TextAlign.Center,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // 答案选项
                    options.forEach { option ->
                        val isCorrect = option == item.nameCN
                        val isSelected = selectedOption == option

                        val backgroundColor = when {
                            selectedOption == null -> Color(0xFFE3F2FD)
                            isSelected && isCorrect -> Color(0xFF81C784)
                            isSelected && !isCorrect -> Color(0xFFEF5350)
                            !isSelected && isCorrect -> Color(0xFF81C784).copy(alpha = 0.5f)
                            else -> Color(0xFFE3F2FD)
                        }

                        val textColor = when {
                            isSelected && isCorrect -> Color.White
                            isSelected && !isCorrect -> Color.White
                            else -> Color(0xFF0277BD)
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .scale(if (isSelected) answerScale else 1f)
                                .clickable(enabled = selectedOption == null) {
                                    selectedOption = option
                                    onAnswer(isCorrect)
                                },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = backgroundColor
                            )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = option,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = textColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
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