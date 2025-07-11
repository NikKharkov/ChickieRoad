package com.chickiroad.app.game.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import chickieroad.composeapp.generated.resources.Res
import chickieroad.composeapp.generated.resources.background_game
import com.chickiroad.app.game.model.GameState
import com.chickiroad.app.game.presentation.components.BonusItem
import com.chickiroad.app.game.presentation.components.GameOverOverlay
import com.chickiroad.app.game.presentation.components.GameUI
import com.chickiroad.app.game.presentation.components.ObstacleItem
import com.chickiroad.app.game.presentation.components.PauseOverlay
import com.chickiroad.app.game.presentation.components.PlayerCharacter
import com.chickiroad.app.game.presentation.components.SwipeDetector
import com.chickiroad.app.platform.PlatformBackHandler
import com.chickiroad.app.ui.screens.settings.SettingsViewModel
import com.chickiroad.app.ui.shared.Background
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun GameScreen(
    onHomeClick: () -> Unit,
    gameViewModel: GameViewModel = koinViewModel(),
    settingsViewModel: SettingsViewModel = koinViewModel()
) {
    val gameUiState by gameViewModel.uiState.collectAsState()
    val density = LocalDensity.current

    PlatformBackHandler {
        settingsViewModel.vibrate()
        gameViewModel.pauseGame()
    }

    SwipeDetector(
        onSwipeLeft = { gameViewModel.onSwipeLeft() },
        onSwipeRight = { gameViewModel.onSwipeRight() },
    ) {
        Background(backgroundResImage = Res.drawable.background_game)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { size ->
                    with(density) {
                        gameViewModel.onScreenSizeChanged(
                            width = size.width.toDp().value,
                            height = size.height.toDp().value
                        )
                    }
                }
        ) {
            gameUiState.bonuses.forEach { bonus ->
                BonusItem(
                    bonus = bonus,
                    modifier = Modifier
                )
            }

            gameUiState.obstacles.forEach { obstacle ->
                ObstacleItem(
                    obstacle = obstacle,
                    modifier = Modifier
                )
            }

            PlayerCharacter(
                player = gameUiState.player,
                gameState = gameUiState.gameState,
                modifier = Modifier
            )

            GameUI(
                score = gameUiState.score,
                health = gameUiState.health,
                onPause = { gameViewModel.pauseGame() }
            )
        }
    }

    when (gameUiState.gameState) {
        GameState.PAUSED -> {
            PauseOverlay(
                onResumeClick = { gameViewModel.resumeGame() },
                onRestartClick = { gameViewModel.restartGame() },
                onHomeClick = onHomeClick
            )
        }
        GameState.GAME_OVER -> {
            GameOverOverlay(
                finalScore = gameUiState.score,
                bestScore = gameUiState.bestScore,
                onRestartClick = { gameViewModel.restartGame() },
                onHomeClick = onHomeClick
            )
        }
        GameState.PLAYING -> {  }
    }
}

