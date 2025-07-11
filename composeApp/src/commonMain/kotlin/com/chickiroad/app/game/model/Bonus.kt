package com.chickiroad.app.game.model

data class Bonus(
    val id: String,
    val lane: Lane,
    val x: Float,
    val y: Float,
    val type: BonusType,
    val size: Float = 60f
)

enum class BonusType(val points: Int, val speedBoost: Float) {
    SMALL(1, 0f),
    MEDIUM(2, 0f),
    SPEED(0, 0.2f)
}