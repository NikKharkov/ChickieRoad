package com.chickiroad.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chickiroad.app.game.presentation.GameScreen
import com.chickiroad.app.platform.exitApp
import com.chickiroad.app.shop.presentation.ShopScreen
import com.chickiroad.app.ui.navigation.Screen
import com.chickiroad.app.ui.screens.exit.ExitScreen
import com.chickiroad.app.ui.screens.menu.MenuScreen
import com.chickiroad.app.ui.screens.settings.SettingsScreen
import com.chickiroad.app.ui.screens.settings.SettingsViewModel
import com.chickiroad.app.ui.screens.splash.SplashScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    val navController = rememberNavController()
    val settingsViewModel: SettingsViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.MenuScreen.route) {
                        popUpTo(Screen.SplashScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.MenuScreen.route) {
            MenuScreen(
                onShopClick = { navController.navigate(Screen.ShopScreen.route) },
                onPlayClick = {
                    settingsViewModel.playClickSound()
                    navController.navigate(Screen.GameScreen.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
                onSettingsClick = {
                    navController.navigate(Screen.SettingsScreen.route)
                },
                onExitClick = {
                    navController.navigate(Screen.ExitScreen.route) {
                        popUpTo(Screen.MenuScreen.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable(Screen.ExitScreen.route) {
            ExitScreen(
                onNoClick = { navController.navigate(Screen.MenuScreen.route) },
                onYesClick = {
                    settingsViewModel.stopBackgroundMusic()
                    exitApp()
                }
            )
        }

        composable(Screen.SettingsScreen.route) {
            SettingsScreen(onHomeClick = { navController.navigate(Screen.MenuScreen.route) })
        }

        composable(Screen.GameScreen.route) {
            GameScreen(onHomeClick = { navController.navigate(Screen.MenuScreen.route) })
        }

        composable(Screen.ShopScreen.route) {
            ShopScreen {
                navController.navigate(Screen.MenuScreen.route)
            }
        }
    }

    LaunchedEffect(Unit) {
        settingsViewModel.startBackgroundMusic()
    }
}