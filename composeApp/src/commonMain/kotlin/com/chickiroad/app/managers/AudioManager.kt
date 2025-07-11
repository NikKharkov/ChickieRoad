package com.chickiroad.app.managers

expect class AudioManager() {
    fun playBackgroundMusic()
    fun stopBackgroundMusic()
    fun pauseBackgroundMusic()
    fun resumeBackgroundMusic()
    fun playClickSound()
    fun vibrate()
    fun setMusicEnabled(enabled: Boolean)
    fun setSoundEnabled(enabled: Boolean)
    fun setVibrationEnabled(enabled: Boolean)
    fun release()
}