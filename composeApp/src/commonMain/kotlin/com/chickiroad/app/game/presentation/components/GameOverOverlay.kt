package com.chickiroad.app.game.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_game_over
import chickieroad.composeapp.generated.resources.best_score_logo
import chickieroad.composeapp.generated.resources.btn_home
import chickieroad.composeapp.generated.resources.btn_retry2
import chickieroad.composeapp.generated.resources.chicken_sad
import chickieroad.composeapp.generated.resources.double_fire
import chickieroad.composeapp.generated.resources.final_score_logo
import chickieroad.composeapp.generated.resources.game_over_logo
import com.chickiroad.app.platform.PlatformBackHandler
import com.chickiroad.app.ui.shared.Background
import com.chickiroad.app.ui.shared.CustomButton
import com.chickiroad.app.ui.shared.TextWithBorder
import com.chickiroad.app.ui.theme.Cyan
import com.chickiroad.app.ui.theme.DarkBlue
import org.jetbrains.compose.resources.painterResource

@Composable
fun GameOverOverlay(
    finalScore: Int,
    bestScore: Int,
    onRestartClick: () -> Unit,
    onHomeClick: () -> Unit
) {
    PlatformBackHandler {
        onHomeClick()
    }

    Background(backgroundResImage = Res.drawable.background_game_over)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(Res.drawable.double_fire),
                contentDescription = "Fire",
                modifier = Modifier
                    .width(80.dp)
                    .height(145.dp)
            )

            Image(
                painter = painterResource(Res.drawable.game_over_logo),
                contentDescription = "Game Over",
                modifier = Modifier
                    .width(180.dp)
                    .height(140.dp)
            )

            Image(
                painter = painterResource(Res.drawable.double_fire),
                contentDescription = "Fire",
                modifier = Modifier
                    .width(80.dp)
                    .height(145.dp)
            )
        }

        Image(
            painter = painterResource(Res.drawable.chicken_sad),
            contentDescription = "Chicken sad :(",
            modifier = Modifier
                .width(140.dp)
                .height(210.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.final_score_logo),
                    contentDescription = "Final score",
                    modifier = Modifier
                        .width(100.dp)
                        .height(79.dp)
                )

                TextWithBorder(
                    text = "$finalScore",
                    fontSize = 48.sp,
                    borderColor = Cyan,
                    textColor = DarkBlue
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.best_score_logo),
                    contentDescription = "Best score",
                    modifier = Modifier
                        .width(100.dp)
                        .height(79.dp)
                )

                TextWithBorder(
                    text = "$bestScore",
                    fontSize = 48.sp,
                    borderColor = Cyan,
                    textColor = DarkBlue
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(Res.drawable.double_fire),
                contentDescription = "Fire",
                modifier = Modifier
                    .width(80.dp)
                    .height(145.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CustomButton(
                    image = painterResource(Res.drawable.btn_retry2),
                    onClick = onRestartClick,
                    modifier = Modifier
                        .width(80.dp)
                        .height(64.dp)
                )

                CustomButton(
                    image = painterResource(Res.drawable.btn_home),
                    onClick = onHomeClick,
                    modifier = Modifier
                        .width(80.dp)
                        .height(64.dp)
                )
            }

            Image(
                painter = painterResource(Res.drawable.double_fire),
                contentDescription = "Fire",
                modifier = Modifier
                    .width(80.dp)
                    .height(145.dp)
            )
        }

    }
}