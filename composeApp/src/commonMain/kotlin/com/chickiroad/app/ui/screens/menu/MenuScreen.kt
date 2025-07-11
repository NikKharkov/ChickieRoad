package com.chickiroad.app.ui.screens.menu

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_menu
import chickieroad.composeapp.generated.resources.btn_exit
import chickieroad.composeapp.generated.resources.btn_play
import chickieroad.composeapp.generated.resources.btn_settings
import chickieroad.composeapp.generated.resources.btn_shop
import chickieroad.composeapp.generated.resources.logo
import com.chickiroad.app.platform.PlatformBackHandler
import com.chickiroad.app.ui.shared.Background
import com.chickiroad.app.ui.shared.CustomButton
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MenuScreen(
    onShopClick: () -> Unit,
    onPlayClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onExitClick: () -> Unit
) {
    PlatformBackHandler {
        onExitClick()
    }

    Background(backgroundResImage = Res.drawable.background_menu)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(170.dp)
                .height(124.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        CustomButton(
            image = painterResource(Res.drawable.btn_play),
            onClick = onPlayClick,
            modifier = Modifier.size(100.dp)
        )

        CustomButton(
            image = painterResource(Res.drawable.btn_shop),
            onClick = onShopClick,
            modifier = Modifier
                .width(120.dp)
                .height(64.dp)
        )

        CustomButton(
            image = painterResource(Res.drawable.btn_settings),
            onClick = onSettingsClick,
            modifier = Modifier
                .width(120.dp)
                .height(64.dp)
        )

        CustomButton(
            image = painterResource(Res.drawable.btn_exit),
            onClick = onExitClick,
            modifier = Modifier
                .width(120.dp)
                .height(64.dp)
        )
    }
}

@Preview
@Composable
private fun MenuScreenPreview() {
    MenuScreen(
        onShopClick = {},
        onPlayClick = {},
        onSettingsClick = {},
        onExitClick = {}
    )
}
