package com.chickiroad.app.managers

import com.russhwolf.settings.Settings

class AppSettings {
    private val settings = Settings()

    var isMusicEnabled: Boolean
        get() = settings.getBoolean(KEY_MUSIC_ENABLED, true)
        set(value) = settings.putBoolean(KEY_MUSIC_ENABLED, value)

    var isSoundEnabled: Boolean
        get() = settings.getBoolean(KEY_SOUND_ENABLED, true)
        set(value) = settings.putBoolean(KEY_SOUND_ENABLED, value)

    var isVibrationEnabled: Boolean
        get() = settings.getBoolean(KEY_VIBRATION_ENABLED, true)
        set(value) = settings.putBoolean(KEY_VIBRATION_ENABLED, value)

    var bestScore: Int
        get() = settings.getInt(KEY_BEST_SCORE, 0)
        set(value) = settings.putInt(KEY_BEST_SCORE, value)

    var doubleX2Purchased: Boolean
        get() = settings.getBoolean(KEY_DOUBLE_X2_PURCHASED, false)
        set(value) = settings.putBoolean(KEY_DOUBLE_X2_PURCHASED, value)

    var healthBoostPurchased: Boolean
        get() = settings.getBoolean(KEY_HEALTH_BOOST_PURCHASED, false)
        set(value) = settings.putBoolean(KEY_HEALTH_BOOST_PURCHASED, value)

    var speedX3Purchased: Boolean
        get() = settings.getBoolean(KEY_SPEED_X3_PURCHASED, false)
        set(value) = settings.putBoolean(KEY_SPEED_X3_PURCHASED, value)

    var doubleX5Purchased: Boolean
        get() = settings.getBoolean(KEY_DOUBLE_X5_PURCHASED, false)
        set(value) = settings.putBoolean(KEY_DOUBLE_X5_PURCHASED, value)

    companion object {
        private const val KEY_MUSIC_ENABLED = "music_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_BEST_SCORE = "best_score"
        private const val KEY_DOUBLE_X2_PURCHASED = "double_x2_purchased"
        private const val KEY_HEALTH_BOOST_PURCHASED = "health_boost_purchased"
        private const val KEY_SPEED_X3_PURCHASED = "speed_x3_purchased"
        private const val KEY_DOUBLE_X5_PURCHASED = "double_x5_purchased"
    }
}
