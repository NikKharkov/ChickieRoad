package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs

@Composable
fun SwipeDetector(
    onSwipeLeft: () -> Unit,
    onSwipeRight: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragEnd = {}
                ) { change, dragAmount ->
                    val deltaX = dragAmount.x
                    val deltaY = dragAmount.y

                    if (abs(deltaX) > abs(deltaY) && abs(deltaX) > 20f) {
                        when {
                            deltaX > 20f -> onSwipeRight()
                            deltaX < -20f -> onSwipeLeft()
                        }
                    }
                }
            }
    ) {
        content()
    }
}