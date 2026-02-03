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
import com.clouditemapp.presentation.navigation.CloudItemNavHost
import com.clouditemapp.presentation.ui.common.EyeProtectionDialog
import com.clouditemapp.presentation.ui.common.EyeProtectionManager
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
            val isEyeProtectionVisible by eyeProtectionManager.isLockVisible.collectAsState()

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
                }
            }
        }
    }
}

@Composable
fun CloudItemAppTheme(content: @Composable () -> Unit) {
    val skyGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFE0F7FA),
            Color(0xFFB3E5FC)
        )
    )

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