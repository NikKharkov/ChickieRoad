package com.chickiroad.app.game.presentation.components

import androidx.compose.animation.core.EaseInOutSine
import androidx.compose.animation.core.EaseOutQuart
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.chicken1_running
import com.chickiroad.app.game.model.GameState
import com.chickiroad.app.game.model.Player
import org.jetbrains.compose.resources.painterResource

@Composable
fun PlayerCharacter(
    player: Player,
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    val swayAnimation by rememberInfiniteTransition().animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        )
    )

    val animatedX by animateFloatAsState(
        targetValue = player.x,
        animationSpec = tween(200, easing = EaseOutQuart)
    )

    Box(
        modifier = modifier
            .size(player.size.dp, player.height.dp)
            .offset(x = animatedX.dp, y = player.y.dp)
            .graphicsLayer {
                rotationZ = if (gameState == GameState.PLAYING) swayAnimation else 0f
            }
    ) {
        Image(
            painter = painterResource(Res.drawable.chicken1_running),
            contentDescription = "Player",
            modifier = Modifier.fillMaxSize()
        )
    }
}