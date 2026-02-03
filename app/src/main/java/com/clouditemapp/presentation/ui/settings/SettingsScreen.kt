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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.clouditemapp.BuildConfig
import com.clouditemapp.data.local.PreferencesManager
import com.clouditemapp.presentation.ui.common.AudioManager
import com.clouditemapp.presentation.ui.common.EyeProtectionManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val eyeProtectionManager: EyeProtectionManager,
    private val audioManager: AudioManager
) : ViewModel() {
    
    val isSoundEnabled: StateFlow<Boolean> = preferencesManager.isSoundEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
        
    val isMusicEnabled: StateFlow<Boolean> = preferencesManager.isMusicEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)
        
    val language: StateFlow<String> = preferencesManager.language
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "中文")
        
    val fontScale: StateFlow<Float> = preferencesManager.fontScale
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 1.0f)
        
    val isEyeProtectionEnabled: StateFlow<Boolean> = preferencesManager.isEyeProtectionEnabled
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun toggleSound(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setSoundEnabled(enabled)
            audioManager.setSoundEnabled(enabled)
        }
    }

    fun toggleMusic(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setMusicEnabled(enabled)
            // TODO: Implement background music control in AudioManager
        }
    }

    fun setLanguage(newLanguage: String) {
        viewModelScope.launch {
            preferencesManager.setLanguage(newLanguage)
        }
    }

    fun setFontScale(scale: Float) {
        viewModelScope.launch {
            preferencesManager.setFontScale(scale)
        }
    }

    fun toggleEyeProtection(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setEyeProtectionEnabled(enabled)
            eyeProtectionManager.setEnabled(enabled)
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            preferencesManager.resetAll()
            // Reset managers to default states
            audioManager.setSoundEnabled(true)
            eyeProtectionManager.setEnabled(true)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val soundEnabled by viewModel.isSoundEnabled.collectAsState()
    val musicEnabled by viewModel.isMusicEnabled.collectAsState()
    val language by viewModel.language.collectAsState()
    val fontScale by viewModel.fontScale.collectAsState()
    val isEyeProtectionEnabled by viewModel.isEyeProtectionEnabled.collectAsState()

    var showResetConfirm by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var userAnswer by remember { mutableStateOf("") }

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
                        onCheckedChange = { viewModel.toggleMusic(it) }
                    )
                }

                // 语言设置
                SettingsSection(title = "语言设置") {
                    DropdownItem(
                        title = "语言",
                        description = "选择应用语言",
                        value = language,
                        options = listOf("中文", "English", "双语"),
                        onValueChange = { viewModel.setLanguage(it) }
                    )
                }

                // 显示设置
                SettingsSection(title = "显示设置") {
                    SliderItem(
                        title = "字体大小",
                        description = "调整应用字体大小",
                        value = fontScale,
                        onValueChange = { viewModel.setFontScale(it) }
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
                        value = "${BuildConfig.VERSION_NAME} (1.0.1-stable)"
                    )

                    AboutItem(
                        title = "开发者",
                        value = "云朵团队"
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // 重置按钮
                Button(
                    onClick = { 
                        mathProblem = Pair((5..15).random(), (5..15).random())
                        showResetConfirm = true 
                    },
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

    if (showResetConfirm) {
        Dialog(onDismissRequest = { showResetConfirm = false }) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "家长验证",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "重置设置是敏感操作，请输入结果：",
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        color = Color(0xFF546E7A)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "${mathProblem.first} + ${mathProblem.second} = ?",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0277BD)
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = userAnswer,
                        onValueChange = { if (it.length <= 3) userAnswer = it },
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        modifier = Modifier.width(120.dp),
                        singleLine = true,
                        textStyle = androidx.compose.ui.text.TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.Center
                        )
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { 
                                showResetConfirm = false
                                userAnswer = ""
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("取消")
                        }
                        Button(
                            onClick = {
                                if (userAnswer.toIntOrNull() == mathProblem.first + mathProblem.second) {
                                    viewModel.resetAllSettings()
                                    showResetConfirm = false
                                    userAnswer = ""
                                }
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("确认重置")
                        }
                    }
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
