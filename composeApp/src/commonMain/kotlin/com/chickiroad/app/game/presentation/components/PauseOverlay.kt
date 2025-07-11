package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.btn_home
import chickieroad.composeapp.generated.resources.btn_restart
import chickieroad.composeapp.generated.resources.btn_resume
import chickieroad.composeapp.generated.resources.game_paused_logo
import com.chickiroad.app.ui.shared.CustomButton
import org.jetbrains.compose.resources.painterResource

@Composable
fun PauseOverlay(
    onResumeClick: () -> Unit,
    onRestartClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(Res.drawable.game_paused_logo),
                contentDescription = "Game paused",
                modifier = Modifier
                    .width(290.dp)
                    .height(140.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                image = painterResource(Res.drawable.btn_resume),
                onClick = onResumeClick,
                modifier = Modifier
                    .width(100.dp)
                    .height(82.dp)
            )

            CustomButton(
                image = painterResource(Res.drawable.btn_restart),
                onClick = onRestartClick,
                modifier = Modifier
                    .width(100.dp)
                    .height(82.dp)
            )

            CustomButton(
                image = painterResource(Res.drawable.btn_home),
                onClick = onHomeClick,
                modifier = Modifier
                    .width(100.dp)
                    .height(82.dp)
            )
        }
    }
}