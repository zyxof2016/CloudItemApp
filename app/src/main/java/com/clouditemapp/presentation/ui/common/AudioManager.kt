package com.clouditemapp.presentation.ui.common

import android.content.Context
import android.media.MediaPlayer
import com.clouditemapp.data.local.PreferencesManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferencesManager: PreferencesManager
) {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundEnabled = true
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    init {
        scope.launch {
            // 初始化声音设置
            isSoundEnabled = preferencesManager.isSoundEnabled.first()
            // 监听后续变化
            preferencesManager.isSoundEnabled.collect { enabled ->
                isSoundEnabled = enabled
                if (!enabled) stopSound()
            }
        }
    }

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
        if (!enabled) {
            stopSound()
        }
    }

    fun playSound(resName: String, onComplete: (() -> Unit)? = null) {
        if (!isSoundEnabled) {
            onComplete?.invoke()
            return
        }
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId != 0) {
            play(resId, onComplete)
        } else {
            android.util.Log.w("AudioManager", "Resource not found: $resName")
            onComplete?.invoke()
        }
    }

    private fun play(resId: Int, onComplete: (() -> Unit)? = null) {
        try {
            stopSound()
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                setOnCompletionListener {
                    stopSound()
                    onComplete?.invoke()
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            onComplete?.invoke()
        }
    }

    fun stopSound() {
        mediaPlayer?.apply {
            try {
                if (isPlaying) {
                    stop()
                }
            } catch (e: Exception) {
                // Ignore
            }
            release()
        }
        mediaPlayer = null
    }
}
