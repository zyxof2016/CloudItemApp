package com.clouditemapp.presentation.ui.common

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.*
import com.clouditemapp.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EyeProtectionManager @Inject constructor() {
    private val _isLockVisible = MutableStateFlow(false)
    val isLockVisible: StateFlow<Boolean> = _isLockVisible

    private val _isEnabled = MutableStateFlow(true)
    val isEnabled: StateFlow<Boolean> = _isEnabled

    private var sessionStartTime = System.currentTimeMillis()
    private val MAX_SESSION_TIME = 15 * 60 * 1000L // 15分钟
    private val REST_TIME = 2 * 60 * 1000L // 2分钟

    fun setEnabled(enabled: Boolean) {
        _isEnabled.value = enabled
        if (!enabled) {
            hideLock()
        } else {
            // 重置开始时间，防止开启后立即锁定
            sessionStartTime = System.currentTimeMillis()
        }
    }

    suspend fun startMonitoring() {
        while (true) {
            if (_isEnabled.value) {
                val currentTime = System.currentTimeMillis()
                if (currentTime - sessionStartTime > MAX_SESSION_TIME) {
                    showLock()
                    delay(REST_TIME)
                    hideLock()
                    sessionStartTime = System.currentTimeMillis()
                }
            } else {
                sessionStartTime = System.currentTimeMillis()
            }
            delay(10000) // 每10秒检查一次
        }
    }

    private fun showLock() {
        _isLockVisible.value = true
    }

    fun hideLock() {
        _isLockVisible.value = false
    }

    fun dismissLock() {
        hideLock()
        sessionStartTime = System.currentTimeMillis()
    }
}

@Composable
fun EyeProtectionDialog(
    isVisible: Boolean,
    onDismiss: () -> Unit
) {
    var showParentGate by remember { mutableStateOf(false) }
    var mathProblem by remember { mutableStateOf(Pair(0, 0)) }
    var userAnswer by remember { mutableStateOf("") }

    if (isVisible) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFB3E5FC)),
                contentAlignment = Alignment.Center
            ) {
                if (!showParentGate) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
                        LottieAnimation(
                            composition = composition,
                            iterations = LottieConstants.IterateForever,
                            modifier = Modifier.size(250.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "眼睛休息时间",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "云朵也累了，陪眼睛休息2分钟吧！\n远眺一下窗外，或者眨眨眼吧~",
                            fontSize = 18.sp,
                            textAlign = TextAlign.Center,
                            color = Color(0xFF546E7A),
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(48.dp))

                        // 家长退出按钮
                        TextButton(
                            onClick = {
                                mathProblem = Pair((5..15).random(), (5..15).random())
                                showParentGate = true
                            }
                        ) {
                            Text("家长退出", color = Color(0xFF0277BD).copy(alpha = 0.6f))
                        }
                    }
                } else {
                    // 家长验证界面
                    Card(
                        modifier = Modifier
                            .padding(24.dp)
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
                                text = "请输入计算结果以确认您是家长：",
                                fontSize = 16.sp,
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
                                        showParentGate = false
                                        userAnswer = ""
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("返回")
                                }
                                Button(
                                    onClick = {
                                        if (userAnswer.toIntOrNull() == mathProblem.first + mathProblem.second) {
                                            onDismiss()
                                            showParentGate = false
                                            userAnswer = ""
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0277BD)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("确认退出")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
