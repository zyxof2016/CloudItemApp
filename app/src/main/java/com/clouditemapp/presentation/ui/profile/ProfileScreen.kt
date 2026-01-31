package com.clouditemapp.presentation.ui.profile

import androidx.compose.foundation.background
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
import com.clouditemapp.presentation.viewmodel.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val profileState by viewModel.profileState.collectAsState()
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
                        text = "🌟 Level ${profileState.level}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // 统计卡片
                if (profileState.isLoading) {
                    CircularProgressIndicator(
                        color = Color(0xFF0277BD),
                        modifier = Modifier.size(48.dp)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            icon = "📚",
                            label = "已学习",
                            value = "${profileState.learnedCount}"
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            icon = "🎮",
                            label = "游戏次数",
                            value = "${profileState.gameCount}"
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            icon = "⭐",
                            label = "星星",
                            value = "${profileState.stars}"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 成就展示
                Text(
                    text = "我的成就",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD),
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // 成就列表（来自数据库）
                AchievementGrid(achievements = profileState.achievements)
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
fun AchievementGrid(
    achievements: List<com.clouditemapp.domain.model.Achievement>
) {
    val items = achievements.map { a ->
        AchievementItem(a.name, a.description, a.iconRes, a.unlocked)
    }
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items.chunked(2).forEach { row ->
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
            }
        }
    }
}

@Composable
fun AchievementCard(
    modifier: Modifier = Modifier,
    achievement: AchievementItem
) {
    Card(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (achievement.unlocked) Color.White else Color(0xFFCFD8DC)
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
                text = achievement.icon,
                fontSize = 36.sp,
                modifier = Modifier.scale(if (achievement.unlocked) 1f else 0.8f)
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
                    fontSize = 12.sp,
                    color = if (achievement.unlocked) Color(0xFF546E7A) else Color(0xFF90A4AE)
                )
            }

            if (achievement.unlocked) {
                Text(
                    text = "✅",
                    fontSize = 20.sp
                )
            } else {
                Text(
                    text = "🔒",
                    fontSize = 20.sp
                )
            }
        }
    }
}

data class AchievementItem(
    val name: String,
    val description: String,
    val icon: String,
    val unlocked: Boolean
)