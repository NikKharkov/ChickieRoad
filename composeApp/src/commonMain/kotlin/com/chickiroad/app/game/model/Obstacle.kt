package com.chickiroad.app.game.model

data class Obstacle(
    val id: String,
    val lane: Lane,
    val x: Float,
    val y: Float,
    val size: Float = 80f
)