package com.chickiroad.app.managers

import android.content.Context
import android.media.MediaPlayer
import android.media.SoundPool
import android.media.AudioAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Build
import androidx.core.content.ContextCompat
import com.chickiroad.app.R
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class AudioManager actual constructor() : KoinComponent {
    private val context: Context by inject()
    private val settings: AppSettings by inject()

    private var mediaPlayer: MediaPlayer? = null
    private var soundPool: SoundPool? = null
    private var clickSoundId: Int = -1
    private var vibrator: Vibrator? = null

    init {
        setupAudio()
        setupVibrator()
    }

    private fun setupAudio() {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        soundPool = SoundPool.Builder()
            .setMaxStreams(5)
            .setAudioAttributes(audioAttributes)
            .build()

        clickSoundId = soundPool?.load(context, R.raw.click_sound, 1) ?: -1
    }

    private fun setupVibrator() {
        vibrator = ContextCompat.getSystemService(context, Vibrator::class.java)
    }

    actual fun playBackgroundMusic() {
        if (!settings.isMusicEnabled) return

        try {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(context, R.raw.background_music)
                mediaPlayer?.isLooping = true
                mediaPlayer?.setVolume(0.7f, 0.7f)
            }

            if (mediaPlayer?.isPlaying != true) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun stopBackgroundMusic() {
        try {
            mediaPlayer?.stop()
            mediaPlayer?.release()
            mediaPlayer = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun pauseBackgroundMusic() {
        try {
            if (mediaPlayer?.isPlaying == true) {
                mediaPlayer?.pause()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun resumeBackgroundMusic() {
        if (!settings.isMusicEnabled) return

        try {
            if (mediaPlayer != null && !mediaPlayer!!.isPlaying) {
                mediaPlayer?.start()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun playClickSound() {
        if (!settings.isSoundEnabled || clickSoundId == -1) return

        try {
            soundPool?.play(clickSoundId, 1.0f, 1.0f, 1, 0, 1.0f)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun vibrate() {
        if (!settings.isVibrationEnabled) return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val vibrationEffect = VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator?.vibrate(vibrationEffect)
            } else {
                @Suppress("DEPRECATION")
                vibrator?.vibrate(100)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    actual fun setMusicEnabled(enabled: Boolean) {
        settings.isMusicEnabled = enabled
        if (!enabled) {
            pauseBackgroundMusic()
        }
    }

    actual fun setSoundEnabled(enabled: Boolean) {
        settings.isSoundEnabled = enabled
    }

    actual fun setVibrationEnabled(enabled: Boolean) {
        settings.isVibrationEnabled = enabled
    }

    actual fun release() {
        stopBackgroundMusic()
        soundPool?.release()
        soundPool = null
    }
}
