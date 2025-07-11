package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.ic_pause
import com.chickiroad.app.ui.shared.CustomButton
import com.chickiroad.app.ui.shared.TextWithBorder
import com.chickiroad.app.ui.theme.Cyan
import com.chickiroad.app.ui.theme.DarkBlue
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameUI(
    score: Int,
    health: Int,
    onPause: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextWithBorder(
            text = "$score",
            fontSize = 48.sp,
            borderColor = Cyan,
            textColor = DarkBlue
        )

        HealthBar(health = health)

        CustomButton(
            image = painterResource(Res.drawable.ic_pause),
            onClick = onPause,
            modifier = Modifier.size(44.dp)
        )
    }
}