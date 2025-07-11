package com.chickiroad.app.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.chickiroad.app.managers.AppSettings
import com.chickiroad.app.managers.AudioManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsState(
    val isMusicEnabled: Boolean = true,
    val isSoundEnabled: Boolean = true,
    val isVibrationEnabled: Boolean = true
)

class SettingsViewModel(
    private val settings: AppSettings,
    private val audioManager: AudioManager
) : ViewModel() {

    private val _state = MutableStateFlow(
        SettingsState(
            isMusicEnabled = settings.isMusicEnabled,
            isSoundEnabled = settings.isSoundEnabled,
            isVibrationEnabled = settings.isVibrationEnabled
        )
    )
    val state = _state.asStateFlow()

    fun toggleMusic() {
        _state.update { currentState ->
            val newValue = !currentState.isMusicEnabled
            audioManager.setMusicEnabled(newValue)

            if (newValue) {
                audioManager.playBackgroundMusic()
            } else {
                audioManager.stopBackgroundMusic()
            }

            currentState.copy(isMusicEnabled = newValue)
        }
    }

    fun toggleSound() {
        _state.update { currentState ->
            val newValue = !currentState.isSoundEnabled
            audioManager.setSoundEnabled(newValue)
            currentState.copy(isSoundEnabled = newValue)
        }
    }

    fun toggleVibration() {
        _state.update { currentState ->
            val newValue = !currentState.isVibrationEnabled
            audioManager.setVibrationEnabled(newValue)
            currentState.copy(isVibrationEnabled = newValue)
        }
    }

    fun playClickSound() {
        audioManager.playClickSound()
    }

    fun vibrate() {
        audioManager.vibrate()
    }

    fun startBackgroundMusic() {
        audioManager.playBackgroundMusic()
    }

    fun stopBackgroundMusic() {
        audioManager.stopBackgroundMusic()
    }

    fun pauseBackgroundMusic() {
        audioManager.pauseBackgroundMusic()
    }

    fun resumeBackgroundMusic() {
        audioManager.resumeBackgroundMusic()
    }

    override fun onCleared() {
        super.onCleared()
        audioManager.release()
    }
}
