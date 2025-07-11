package com.chickiroad.app.ui.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_menu
import chickieroad.composeapp.generated.resources.btn_home
import chickieroad.composeapp.generated.resources.btn_restore_purchases
import chickieroad.composeapp.generated.resources.music_text
import chickieroad.composeapp.generated.resources.settings_logo
import chickieroad.composeapp.generated.resources.sound_text
import chickieroad.composeapp.generated.resources.vibration_text
import com.chickiroad.app.shop.presentation.ShopViewModel
import com.chickiroad.app.ui.screens.settings.components.SettingItem
import com.chickiroad.app.ui.shared.Background
import com.chickiroad.app.ui.shared.CustomButton
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsScreen(
    onHomeClick: () -> Unit,
    settingsViewModel: SettingsViewModel = koinViewModel(),
    shopViewModel: ShopViewModel = koinViewModel()
) {
    val state by settingsViewModel.state.collectAsState()

    Background(backgroundResImage = Res.drawable.background_menu)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.settings_logo),
            contentDescription = "Settings",
            modifier = Modifier
                .width(290.dp)
                .height(140.dp)
        )

        SettingItem(
            titleImage = painterResource(Res.drawable.sound_text),
            isEnabled = state.isSoundEnabled,
            onToggle = { settingsViewModel.toggleSound() }
        )

        SettingItem(
            titleImage = painterResource(Res.drawable.music_text),
            isEnabled = state.isMusicEnabled,
            onToggle = { settingsViewModel.toggleMusic() }
        )

        SettingItem(
            titleImage = painterResource(Res.drawable.vibration_text),
            isEnabled = state.isVibrationEnabled,
            onToggle = { settingsViewModel.toggleVibration() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomButton(
                image = painterResource(Res.drawable.btn_restore_purchases),
                onClick = {
                    shopViewModel.restorePurchases()
                },
                modifier = Modifier
                    .width(116.dp)
                    .height(74.dp)
            )

            CustomButton(
                image = painterResource(Res.drawable.btn_home),
                onClick = {
                    settingsViewModel.playClickSound()
                    onHomeClick()
                },
                modifier = Modifier
                    .width(80.dp)
                    .height(64.dp)
            )
        }
    }
}