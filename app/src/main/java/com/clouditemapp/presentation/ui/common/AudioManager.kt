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

    fun playSound(resName: String) {
        if (!isSoundEnabled) return
        val resId = context.resources.getIdentifier(resName, "raw", context.packageName)
        if (resId != 0) {
            play(resId)
        }
    }

    private fun play(resId: Int) {
        try {
            stopSound()
            mediaPlayer = MediaPlayer.create(context, resId).apply {
                setOnCompletionListener {
                    stopSound()
                }
                start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
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
