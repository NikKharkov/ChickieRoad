package com.chickiroad.app.game.model

data class Player(
    val lane: Lane = Lane.LEFT,
    val x: Float = 0f,
    val y: Float = 0f,
    val size: Float = 80f,
    val height: Float = 100f
)
