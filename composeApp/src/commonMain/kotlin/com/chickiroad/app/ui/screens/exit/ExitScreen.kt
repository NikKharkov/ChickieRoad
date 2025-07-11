package com.chickiroad.app.ui.screens.exit

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_menu
import chickieroad.composeapp.generated.resources.btn_no
import chickieroad.composeapp.generated.resources.btn_yes
import chickieroad.composeapp.generated.resources.chicken_surprised
import com.chickiroad.app.platform.PlatformBackHandler
import com.chickiroad.app.ui.shared.Background
import com.chickiroad.app.ui.shared.CustomButton
import com.chickiroad.app.ui.shared.TextWithBorder
import com.chickiroad.app.ui.theme.Cyan
import com.chickiroad.app.ui.theme.DarkRed
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ExitScreen(
    onNoClick: () -> Unit,
    onYesClick: () -> Unit
) {
    PlatformBackHandler {
        onNoClick()
    }

    Background(backgroundResImage = Res.drawable.background_menu)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        TextWithBorder(
            text = "Are you sure you want to exit?",
            fontSize = 40.sp,
            borderColor = Cyan,
            textColor = DarkRed
        )

        Image(
            painter = painterResource(Res.drawable.chicken_surprised),
            contentDescription = "Surprised chicken",
            modifier = Modifier
                .width(226.dp)
                .height(339.dp)
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomButton(
                image = painterResource(Res.drawable.btn_yes),
                onClick = onYesClick,
                modifier = Modifier
                    .width(116.dp)
                    .height(74.dp)
            )

            CustomButton(
                image = painterResource(Res.drawable.btn_no),
                onClick = onNoClick,
                modifier = Modifier
                    .width(116.dp)
                    .height(74.dp)
            )
        }
    }
}

@Preview
@Composable
private fun ExitScreenPreview() {
    ExitScreen(
        onNoClick = {},
        onYesClick = {}
    )
}