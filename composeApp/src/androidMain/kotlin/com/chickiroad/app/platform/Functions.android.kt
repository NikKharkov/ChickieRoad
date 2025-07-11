package com.chickiroad.app.platform

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

actual fun exitApp() {
    kotlin.system.exitProcess(0)
}

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
}