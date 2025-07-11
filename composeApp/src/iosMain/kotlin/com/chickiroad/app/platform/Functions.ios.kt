package com.chickiroad.app.platform

import androidx.compose.runtime.Composable
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSSelectorFromString
import platform.UIKit.UIApplication

@OptIn(ExperimentalForeignApi::class)
actual fun exitApp() {
    val app = UIApplication.sharedApplication

    val suspendSelector = NSSelectorFromString("suspend")
    app.performSelector(suspendSelector)
}

@Composable
actual fun PlatformBackHandler(onBack: () -> Unit) {
}