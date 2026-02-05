package com.clouditemapp.presentation.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.clouditemapp.domain.model.Achievement
import com.clouditemapp.presentation.viewmodel.ProfileViewModel

import com.clouditemapp.presentation.ui.common.WindowSizeClass
import com.clouditemapp.presentation.ui.common.rememberWindowSizeClass

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val windowSize = rememberWindowSizeClass()
    val isTablet = windowSize == WindowSizeClass.Expanded
    val stats by viewModel.stats.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    
    // 简单的等级计算：每 100 颗星升一级
    val level = (stats.totalStars / 100) + 1
    
    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("我的成就", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "返回")
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 用户头像
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF81C784)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "👶",
                        fontSize = 60.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 用户名
                Text(
                    text = "小探险家",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 等级
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFB74D)
                    )
                ) {
                    Text(
                        text = "🌟 Level $level",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 统计卡片
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = "📚",
                        label = "已学习",
                        value = "${stats.learnedCount}"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = "🎮",
                        label = "游戏次数",
                        value = "${stats.gameCount}"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        icon = "⭐",
                        label = "星星",
                        value = "${stats.totalStars}"
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 成就展示
                Text(
                    text = "我的成就 (${stats.unlockedAchievementsCount}/${stats.totalAchievementsCount})",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 成就列表
                AchievementGrid(achievements, isTablet)
            }
        }
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    icon: String,
    label: String,
    value: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = icon,
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                fontSize = 14.sp,
                color = Color(0xFF546E7A)
            )
        }
    }
}

@Composable
fun AchievementGrid(achievements: List<Achievement>, isTablet: Boolean = false) {
    if (achievements.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth().height(100.dp), contentAlignment = Alignment.Center) {
            Text(text = "加油，开始你的探索之旅吧！", color = Color.Gray)
        }
    } else {
        val columns = if (isTablet) 4 else 2
        androidx.compose.foundation.lazy.LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            val rows = achievements.chunked(columns)
            items(rows.size) { rowIndex ->
                val row = rows[rowIndex]
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    row.forEach { achievement ->
                        AchievementCard(
                            modifier = Modifier.weight(1f),
                            achievement = achievement
                        )
                    }
                    repeat(columns - row.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun AchievementCard(
    modifier: Modifier = Modifier,
    achievement: Achievement
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.unlocked) Color.White else Color(0xFFF5F5F5)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (achievement.unlocked) 4.dp else 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = achievement.iconRes,
                fontSize = 32.sp,
                modifier = Modifier.scale(if (achievement.unlocked) 1f else 0.7f)
            )

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = achievement.name,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (achievement.unlocked) Color(0xFF0277BD) else Color(0xFF78909C)
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = achievement.description,
                    fontSize = 10.sp,
                    lineHeight = 14.sp,
                    color = if (achievement.unlocked) Color(0xFF546E7A) else Color(0xFFB0BEC5)
                )
            }

            Text(
                text = if (achievement.unlocked) "✅" else "🔒",
                fontSize = 16.sp
            )
        }
    }
}
