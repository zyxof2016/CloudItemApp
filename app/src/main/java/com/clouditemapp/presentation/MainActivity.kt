package com.clouditemapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.clouditemapp.presentation.navigation.CloudItemNavHost
import com.clouditemapp.presentation.ui.common.EyeProtectionDialog
import com.clouditemapp.presentation.ui.common.EyeProtectionManager
import com.clouditemapp.presentation.viewmodel.MainViewModel
import com.airbnb.lottie.compose.*
import com.clouditemapp.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var eyeProtectionManager: EyeProtectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                eyeProtectionManager.startMonitoring()
            }
        }

        setContent {
            val mainViewModel: MainViewModel = hiltViewModel()
            val isEyeProtectionVisible by eyeProtectionManager.isLockVisible.collectAsState()
            val unlockedAchievement by mainViewModel.unlockedAchievement.collectAsState()

            CloudItemAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CloudItemNavHost(navController = navController)

                    EyeProtectionDialog(
                        isVisible = isEyeProtectionVisible,
                        onDismiss = { eyeProtectionManager.dismissLock() }
                    )

                    // 成就解锁弹窗
                    unlockedAchievement?.let { achievement ->
                        Dialog(onDismissRequest = { mainViewModel.dismissAchievement() }) {
                            Card(
                                shape = RoundedCornerShape(32.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.celebration))
                                    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
                                    
                                    Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                                        LottieAnimation(composition = composition, progress = { progress })
                                        Text(text = achievement.iconRes, fontSize = 64.sp)
                                    }
                                    
                                    Text(
                                        text = "解锁新成就！",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF0277BD)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = achievement.name,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFFFFB74D)
                                    )
                                    
                                    Spacer(modifier = Modifier.height(8.dp))
                                    
                                    Text(
                                        text = achievement.description,
                                        fontSize = 16.sp,
                                        color = Color.Gray,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                    
                                    Spacer(modifier = Modifier.height(24.dp))
                                    
                                    Button(
                                        onClick = { mainViewModel.dismissAchievement() },
                                        shape = RoundedCornerShape(16.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF81C784))
                                    ) {
                                        Text("太棒了！", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CloudItemAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MaterialTheme.colorScheme.copy(
            primary = Color(0xFF0277BD),
            secondary = Color(0xFF81C784),
            tertiary = Color(0xFFFFB74D)
        ),
        typography = MaterialTheme.typography
    ) {
        content()
    }
}