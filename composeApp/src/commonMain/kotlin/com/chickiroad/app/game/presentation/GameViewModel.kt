package com.chickiroad.app.game.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chickiroad.app.game.model.Bonus
import com.chickiroad.app.game.model.BonusType
import com.chickiroad.app.game.model.GameState
import com.chickiroad.app.game.model.GameUiState
import com.chickiroad.app.game.model.Lane
import com.chickiroad.app.game.model.Obstacle
import com.chickiroad.app.game.model.Player
import com.chickiroad.app.managers.AppSettings
import com.chickiroad.app.shop.model.GameBonuses
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock

class GameViewModel(private val appSettings: AppSettings) : ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState(bestScore = appSettings.bestScore))
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private var gameJob: Job? = null
    private var lastBonusTime = 0L
    private var lastObstacleTime = 0L

    fun startGame() {
        applyShopBonuses()

        gameJob?.cancel()
        gameJob = viewModelScope.launch {
            while (_uiState.value.gameState == GameState.PLAYING) {
                updateGame()
                delay(16)
            }
        }
    }

    private fun applyShopBonuses() {
        val currentState = _uiState.value
        val gameBonuses = getGameBonusesFromSettings()

        _uiState.value = currentState.copy(
            scoreMultiplier = gameBonuses.scoreMultiplier,
            maxHealth = gameBonuses.maxHealth,
            baseSpeed = gameBonuses.speedMultiplier,
            health = gameBonuses.maxHealth
        )
    }

    private fun updateGame() {
        val currentState = _uiState.value

        val allBonuses = generateBonuses(currentState)
        val allObstacles = generateObstacles(currentState.copy(bonuses = allBonuses))

        val movedBonuses = moveBonuses(allBonuses)
        val movedObstacles = moveObstacles(allObstacles)

        val (collectedBonuses, remainingBonuses) = checkBonusCollisions(movedBonuses, currentState.player)
        val hasObstacleCollision = checkObstacleCollisions(movedObstacles, currentState.player)

        var newScore = currentState.score
        var newSpeed = currentState.speed

        collectedBonuses.forEach { bonus ->
            newScore += (bonus.type.points * currentState.scoreMultiplier).toInt()
            newSpeed += bonus.type.speedBoost * currentState.baseSpeed
        }

        if (hasObstacleCollision) {
            val newHealth = currentState.health - 1

            val filteredObstacles = movedObstacles.filter { obstacle ->
                val distanceX = kotlin.math.abs(obstacle.x - currentState.player.x)
                val distanceY = kotlin.math.abs(obstacle.y - currentState.player.y)
                val collisionDistance = 45f
                !(distanceX < collisionDistance && distanceY < collisionDistance)
            }

            if (newHealth <= 0) {
                gameOver()
                return
            } else {
                _uiState.value = currentState.copy(
                    bonuses = remainingBonuses,
                    obstacles = filteredObstacles,
                    score = newScore,
                    speed = newSpeed,
                    health = newHealth
                )
                return
            }
        }

        _uiState.value = currentState.copy(
            bonuses = remainingBonuses,
            obstacles = movedObstacles,
            score = newScore,
            speed = newSpeed
        )
    }

    private fun generateObstacles(state: GameUiState): List<Obstacle> {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val newObstacles = state.obstacles.toMutableList()

        if (currentTime - lastObstacleTime > (2000..4000).random()) {
            val lane = Lane.entries.toTypedArray().random()
            val x = if (lane == Lane.LEFT) state.leftLaneX else state.rightLaneX

            val obstacle = Obstacle(
                id = "obstacle_$currentTime",
                lane = lane,
                x = x,
                y = -100f
            )

            newObstacles.add(obstacle)
            lastObstacleTime = currentTime
        }

        return newObstacles
    }

    private fun moveObstacles(obstacles: List<Obstacle>): List<Obstacle> {
        return obstacles.map { obstacle ->
            obstacle.copy(y = obstacle.y + 3f)
        }.filter { it.y < _uiState.value.screenHeight + 100f }
    }

    private fun checkObstacleCollisions(obstacles: List<Obstacle>, player: Player): Boolean {
        obstacles.forEach { obstacle ->
            val distanceX = kotlin.math.abs(obstacle.x - player.x)
            val distanceY = kotlin.math.abs(obstacle.y - player.y)

            val collisionDistance = 45f
            if (distanceX < collisionDistance && distanceY < collisionDistance) {
                return true
            }
        }
        return false
    }

    private fun generateBonuses(state: GameUiState): List<Bonus> {
        val currentTime = Clock.System.now().toEpochMilliseconds()
        val newBonuses = state.bonuses.toMutableList()

        if (currentTime - lastBonusTime > (4500..6000).random()) {
            val bonusType = BonusType.entries.toTypedArray().random()
            val lane = Lane.entries.toTypedArray().random()
            val x = if (lane == Lane.LEFT) state.leftLaneX else state.rightLaneX

            val bonus = Bonus(
                id = "bonus_$currentTime",
                lane = lane,
                x = x,
                y = -100f,
                type = bonusType
            )

            newBonuses.add(bonus)
            lastBonusTime = currentTime
        }

        return newBonuses
    }

    private fun moveBonuses(bonuses: List<Bonus>): List<Bonus> {
        return bonuses.map { bonus ->
            bonus.copy(y = bonus.y + 3f)
        }.filter { it.y < _uiState.value.screenHeight + 100f }
    }

    private fun checkBonusCollisions(bonuses: List<Bonus>, player: Player): Pair<List<Bonus>, List<Bonus>> {
        val collected = mutableListOf<Bonus>()
        val remaining = mutableListOf<Bonus>()

        bonuses.forEach { bonus ->
            val distanceX = kotlin.math.abs(bonus.x - player.x)
            val distanceY = kotlin.math.abs(bonus.y - player.y)

            val collisionDistance = 50f
            val isColliding = distanceX < collisionDistance && distanceY < collisionDistance

            if (isColliding) {
                collected.add(bonus)
            } else {
                remaining.add(bonus)
            }
        }

        return Pair(collected, remaining)
    }

    fun onScreenSizeChanged(width: Float, height: Float) {
        val newState = _uiState.value.copy(
            screenWidth = width,
            screenHeight = height
        )

        val leftLaneX = (width / 2f) - (GameUiState.ROAD_WIDTH / 2f) + (GameUiState.LANE_WIDTH / 2f) - (GameUiState.PLAYER_SIZE / 2f) - 30f
        val playerY = height - GameUiState.PLAYER_HEIGHT - 50f

        _uiState.value = newState.copy(
            player = newState.player.copy(
                x = leftLaneX,
                y = playerY
            )
        )

        if (_uiState.value.gameState == GameState.PLAYING) {
            startGame()
        }
    }

    fun onSwipeLeft() {
        val currentState = _uiState.value
        if (currentState.gameState != GameState.PLAYING) return

        if (currentState.player.lane == Lane.RIGHT) {
            switchToLane(Lane.LEFT)
        }
    }

    fun onSwipeRight() {
        val currentState = _uiState.value
        if (currentState.gameState != GameState.PLAYING) return

        if (currentState.player.lane == Lane.LEFT) {
            switchToLane(Lane.RIGHT)
        }
    }

    private fun switchToLane(newLane: Lane) {
        val currentState = _uiState.value
        val newX = if (newLane == Lane.LEFT) currentState.leftLaneX else currentState.rightLaneX

        _uiState.value = currentState.copy(
            player = currentState.player.copy(
                lane = newLane,
                x = newX
            )
        )
    }

    fun pauseGame() {
        gameJob?.cancel()
        _uiState.value = _uiState.value.copy(gameState = GameState.PAUSED)
    }

    fun resumeGame() {
        _uiState.value = _uiState.value.copy(gameState = GameState.PLAYING)
        startGame()
    }

    fun gameOver() {
        val currentScore = _uiState.value.score
        val currentBest = _uiState.value.bestScore

        if (currentScore > currentBest) {
            appSettings.bestScore = currentScore
            _uiState.value = _uiState.value.copy(
                gameState = GameState.GAME_OVER,
                bestScore = currentScore
            )
        } else {
            _uiState.value = _uiState.value.copy(gameState = GameState.GAME_OVER)
        }
    }

    fun restartGame() {
        gameJob?.cancel()
        lastBonusTime = 0L
        lastObstacleTime = 0L

        val currentState = _uiState.value
        val gameBonuses = getGameBonusesFromSettings()

        val newState = GameUiState(
            screenWidth = currentState.screenWidth,
            screenHeight = currentState.screenHeight,
            bestScore = currentState.bestScore,
            scoreMultiplier = gameBonuses.scoreMultiplier,
            maxHealth = gameBonuses.maxHealth,
            baseSpeed = gameBonuses.speedMultiplier,
            health = gameBonuses.maxHealth
        )

        val leftLaneX = newState.leftLaneX
        val playerY = newState.screenHeight - GameUiState.PLAYER_HEIGHT - 50f

        _uiState.value = newState.copy(
            player = newState.player.copy(
                x = leftLaneX,
                y = playerY
            )
        )
        startGame()
    }

    private fun getGameBonusesFromSettings(): GameBonuses {
        var scoreMultiplier = 1f
        if (appSettings.doubleX2Purchased) scoreMultiplier *= 2f
        if (appSettings.doubleX5Purchased) scoreMultiplier *= 5f

        val maxHealth = if (appSettings.healthBoostPurchased) 10 else 3
        val speedMultiplier = if (appSettings.speedX3Purchased) 3f else 1f

        return GameBonuses(
            scoreMultiplier = scoreMultiplier,
            maxHealth = maxHealth,
            speedMultiplier = speedMultiplier
        )
    }
}
