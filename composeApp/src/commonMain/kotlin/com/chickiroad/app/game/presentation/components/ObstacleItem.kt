package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.fire
import com.chickiroad.app.game.model.Obstacle
import org.jetbrains.compose.resources.painterResource

@Composable
fun ObstacleItem(
    obstacle: Obstacle,
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(Res.drawable.fire),
        contentDescription = "Obstacle",
        modifier = modifier
            .size(obstacle.size.dp)
            .offset(x = obstacle.x.dp, y = obstacle.y.dp)
    )
}
