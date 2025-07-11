package com.chickiroad.app.ui.navigation

sealed class Screen(val route: String) {
    data object SplashScreen: Screen("splash")
    data object MenuScreen: Screen("menu")
    data object ExitScreen: Screen("exit")
    data object SettingsScreen : Screen("settings")
    data object ShopScreen : Screen("shop")
    data object GameScreen : Screen("game")
}