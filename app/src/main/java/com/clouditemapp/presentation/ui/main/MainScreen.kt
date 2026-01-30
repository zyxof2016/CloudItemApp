package com.clouditemapp.presentation.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.clouditemapp.presentation.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    viewModel: MainViewModel = hiltViewModel()
) {
    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

    Scaffold(
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // 标题
                Text(
                    text = "☁️ 云朵识物乐园",
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0277BD),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // 功能按钮
                MenuButton(
                    text = "📚 认物学习",
                    description = "认识各种有趣的物品",
                    color = Color(0xFFFFB74D),
                    onClick = { navController.navigate("learning") }
                )

                MenuButton(
                    text = "🎮 趣味游戏",
                    description = "边玩边学更开心",
                    color = Color(0xFF81C784),
                    onClick = { navController.navigate("game") }
                )

                MenuButton(
                    text = "🏆 我的成就",
                    description = "看看你获得了什么成就",
                    color = Color(0xFFBA68C8),
                    onClick = { navController.navigate("profile") }
                )

                MenuButton(
                    text = "⚙️ 设置",
                    description = "调整应用设置",
                    color = Color(0xFF90A4AE),
                    onClick = { navController.navigate("settings") }
                )

                Spacer(modifier = Modifier.weight(1f))

                // 底部装饰
                Text(
                    text = "快乐学习，快乐成长！",
                    fontSize = 16.sp,
                    color = Color(0xFF546E7A),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuButton(
    text: String,
    description: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
        shape = RoundedCornerShape(24.dp),
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
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = text,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}