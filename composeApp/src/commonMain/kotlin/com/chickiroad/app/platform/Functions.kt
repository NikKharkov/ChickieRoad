package com.chickiroad.app.platform

import androidx.compose.runtime.Composable

expect fun exitApp()

@Composable
expect fun PlatformBackHandler(onBack: () -> Unit)