package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.ic_heart
import com.chickiroad.app.ui.theme.DarkRed
import org.jetbrains.compose.resources.painterResource

@Composable
fun HealthBar(
    health: Int,
    maxHealth: Int = 3,
    modifier: Modifier = Modifier
) {
    val healthPercentage = health.toFloat() / maxHealth.toFloat()

    Column(
        modifier = modifier.size(50.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_heart),
            contentDescription = "Health",
            tint = Color.Unspecified,
            modifier = Modifier.size(30.dp),
        )

        Box(
            modifier = Modifier
                .width((50f * healthPercentage).dp)
                .height(12.dp)
                .background(
                    color = DarkRed,
                    shape = RectangleShape
                )
        )
    }
}