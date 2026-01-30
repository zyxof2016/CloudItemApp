package com.clouditemapp.presentation.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.clouditemapp.presentation.ui.common.AudioManager
import com.clouditemapp.presentation.ui.common.EyeProtectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val eyeProtectionManager: EyeProtectionManager,
    private val audioManager: AudioManager
) : ViewModel() {
    val isEyeProtectionEnabled: StateFlow<Boolean> = eyeProtectionManager.isEnabled

    private val _isSoundEnabled = MutableStateFlow(true)
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    fun toggleEyeProtection(enabled: Boolean) {
        eyeProtectionManager.setEnabled(enabled)
    }

    fun toggleSound(enabled: Boolean) {
        _isSoundEnabled.value = enabled
        audioManager.setSoundEnabled(enabled)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val soundEnabled by viewModel.isSoundEnabled.collectAsState()
    var musicEnabled by remember { mutableStateOf(true) }
    var language by remember { mutableStateOf("中文") }
    val isEyeProtectionEnabled by viewModel.isEyeProtectionEnabled.collectAsState()

    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("设置", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // 声音设置
                SettingsSection(title = "声音设置") {
                    SwitchItem(
                        title = "音效",
                        description = "开启/关闭点击和反馈音效",
                        checked = soundEnabled,
                        onCheckedChange = { viewModel.toggleSound(it) }
                    )

                    SwitchItem(
                        title = "背景音乐",
                        description = "开启/关闭背景音乐",
                        checked = musicEnabled,
                        onCheckedChange = { musicEnabled = it }
                    )
                }

                // 语言设置
                SettingsSection(title = "语言设置") {
                    DropdownItem(
                        title = "语言",
                        description = "选择应用语言",
                        value = language,
                        options = listOf("中文", "English", "双语"),
                        onValueChange = { language = it }
                    )
                }

                // 显示设置
                SettingsSection(title = "显示设置") {
                    SliderItem(
                        title = "字体大小",
                        description = "调整应用字体大小",
                        value = 0.5f,
                        onValueChange = { }
                    )

                    SwitchItem(
                        title = "护眼模式",
                        description = "强制学习15分钟后休息2分钟",
                        checked = isEyeProtectionEnabled,
                        onCheckedChange = { viewModel.toggleEyeProtection(it) }
                    )
                }

                // 关于
                SettingsSection(title = "关于") {
                    AboutItem(
                        title = "版本",
                        value = "1.0.0"
                    )

                    AboutItem(
                        title = "开发者",
                        value = "云朵团队"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 重置按钮
                Button(
                    onClick = { },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "重置所有设置",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )

            content()
        }
    }
}

@Composable
fun SwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF263238)
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF78909C)
            )
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF81C784),
                checkedTrackColor = Color(0xFF81C784).copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
fun DropdownItem(
    title: String,
    description: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF263238)
            )

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color(0xFF78909C)
            )
        }

        Box {
            Button(
                onClick = { expanded = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE3F2FD)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = value,
                    fontSize = 16.sp,
                    color = Color(0xFF0277BD)
                )
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SliderItem(
    title: String,
    description: String,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF263238)
                )

                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color(0xFF78909C)
                )
            }

            Text(
                text = "${(value * 100).toInt()}%",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0277BD)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                activeTrackColor = Color(0xFF81C784),
                thumbColor = Color(0xFF81C784)
            )
        )
    }
}

@Composable
fun AboutItem(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF263238)
        )

        Text(
            text = value,
            fontSize = 16.sp,
            color = Color(0xFF546E7A)
        )
    }
}