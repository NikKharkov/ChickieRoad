package com.chickiroad.app

import androidx.compose.ui.window.ComposeUIViewController
import com.chickiroad.app.di.initKoin

fun MainViewController() = ComposeUIViewController {
    initKoin()
    App()
}