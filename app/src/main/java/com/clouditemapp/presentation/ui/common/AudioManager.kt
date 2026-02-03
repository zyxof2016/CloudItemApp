package com.clouditemapp.presentation.ui.common

import android.content.Context
import android.media.MediaPlayer
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AudioManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private var mediaPlayer: MediaPlayer? = null
    private var isSoundEnabled = true

    fun setSoundEnabled(enabled: Boolean) {
        isSoundEnabled = enabled
        if (!enabled) {
            stopSound()
        }
    }

    fun playSound(resName: String, onComplete: (() -> Unit)? = null) {
        if (!isSoundEnabled) return
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId != 0) {
            play(resId, onComplete)
        } else {
            // 如果找不到资源，可以在这里播放一个默认的“提示音”或者记录日志
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
            if (isPlaying) {
                stop()
            }
            release()
        }
        mediaPlayer = null
    }
}
