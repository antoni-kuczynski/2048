package com.kuczynski.game_2048

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.lifecycle.ViewModel
import kotlin.math.abs
import kotlin.random.Random

data class Tile(val id: Int, val value: Int, val row: Int, val col: Int)

enum class SwipeDirection { UP, DOWN, LEFT, RIGHT }
//==================================================================================================
fun Modifier.onSwipe(onSwipeComplete: (SwipeDirection) -> Unit): Modifier = this.pointerInput(Unit) {
    var totalDragX = 0f
    var totalDragY = 0f

    detectDragGestures(
        onDragStart = {
            totalDragX = 0f
            totalDragY = 0f
        },
        onDrag = { change, dragAmount ->
            change.consume()
            totalDragX += dragAmount.x
            totalDragY += dragAmount.y
        },
        onDragEnd = {
            if (abs(totalDragX) > 40f || abs(totalDragY) > 40f) {
                if (abs(totalDragX) > abs(totalDragY)) {
                    if (totalDragX > 0) onSwipeComplete(SwipeDirection.RIGHT)
                    else onSwipeComplete(SwipeDirection.LEFT)
                } else {
                    if (totalDragY > 0) onSwipeComplete(SwipeDirection.DOWN)
                    else onSwipeComplete(SwipeDirection.UP)
                }
            }
        }
    )
}
//==================================================================================================
class GameViewModel(private val settingsManager: SettingsManager, var n: Int = 4) : ViewModel() {
    var tiles by mutableStateOf(listOf<Tile>())
        private set

    var score by mutableStateOf(0)
        private set

    var highScore by mutableStateOf(0)
        private set

    private var nextTileId = 0
    //==============================================================================================
    var settingsShown by mutableStateOf(false);
    private var _currentTheme by mutableStateOf(settingsManager.getSavedTheme())
    //==============================================================================================
    var isGameOver by mutableStateOf(false)
        private set

    var isGameWon by mutableStateOf(false)
        private set

    private var hasContinuedAfterWin = false
    //==============================================================================================
    var currentTheme: AppTheme
        get() = _currentTheme
        set(value) {
            _currentTheme = value
            settingsManager.saveTheme(value)
        }
    //==============================================================================================
    init {
        startNewGame()
    }
    //==============================================================================================
    fun changeBoardSize(size: Int) {
        n = size
        settingsShown = false
        startNewGame()
    }
    //==============================================================================================
    fun startNewGame() {
        tiles = emptyList()
        score = 0
        isGameOver = false
        isGameWon = false
        hasContinuedAfterWin = false
        highScore = settingsManager.getHighScore(n)

        spawnRandomTile()
        spawnRandomTile()
    }
    //==============================================================================================
    fun keepPlaying() {
        isGameWon = false
        hasContinuedAfterWin = true
        checkGameState()
    }
    //==============================================================================================
    private fun addScore(points: Int) {
        score += points
        if (score > highScore) {
            highScore = score
            settingsManager.saveHighScore(n, highScore)
        }
    }
    //==============================================================================================
    fun handleSwipe(direction: SwipeDirection) {
        val grid = Array(n) { arrayOfNulls<Tile>(n) }
        tiles.forEach { grid[it.row][it.col] = it }

        var moved = false
        when (direction) {
            SwipeDirection.LEFT -> {
                for (row in 0 until n) {
                    var targetCol = 0;
                    var lastMergedCol = -1;
                    for (col in 0 until n) {
                        if (grid[row][col] == null) {
                            continue;
                        }

                        val tile = grid[row][col];
                        grid[row][col] = null

                        val prev = if (targetCol > 0) {
                            grid[row][targetCol - 1]
                        } else {
                            null
                        }

                        val canMerge: Boolean = prev != null && prev.value == tile?.value && lastMergedCol != targetCol - 1;
                        val canMoveWithoutMerging: Boolean = !canMerge;

                        if (canMerge) {
                            grid[row][targetCol - 1] = prev.copy(value = prev.value * 2)
                            addScore(prev.value * 2)
                            lastMergedCol = targetCol - 1
                            moved = true
                        } else {
                            grid[row][targetCol] = tile?.copy(col = targetCol)
                            if (col != targetCol) moved = true
                            targetCol++
                        }
                    }
                }
            }
            //======================================================================================
            SwipeDirection.RIGHT -> {
                for (row in 0 until n) {
                    var targetCol = n - 1
                    var lastMergedCol = -1
                    for (col in n - 1 downTo 0) {
                        val tile = grid[row][col] ?: continue
                        grid[row][col] = null

                        val prev = if (targetCol < n - 1) grid[row][targetCol + 1] else null
                        if (prev != null && prev.value == tile.value && lastMergedCol != targetCol + 1) {
                            grid[row][targetCol + 1] = prev.copy(value = prev.value * 2)
                            addScore(prev.value * 2)
                            lastMergedCol = targetCol + 1
                            moved = true
                        } else {
                            grid[row][targetCol] = tile.copy(col = targetCol)
                            if (col != targetCol) moved = true
                            targetCol--
                        }
                    }
                }
            }
            //======================================================================================
            SwipeDirection.UP -> {
                for (col in 0 until n) {
                    var targetRow = 0
                    var lastMergedRow = -1
                    for (row in 0 until n) {
                        val tile = grid[row][col] ?: continue
                        grid[row][col] = null

                        val prev = if (targetRow > 0) grid[targetRow - 1][col] else null
                        if (prev != null && prev.value == tile.value && lastMergedRow != targetRow - 1) {
                            grid[targetRow - 1][col] = prev.copy(value = prev.value * 2)
                            addScore(prev.value * 2)
                            lastMergedRow = targetRow - 1
                            moved = true
                        } else {
                            grid[targetRow][col] = tile.copy(row = targetRow)
                            if (row != targetRow) moved = true
                            targetRow++
                        }
                    }
                }
            }
            //======================================================================================
            SwipeDirection.DOWN -> {
                for (col in 0 until n) {
                    var targetRow = n - 1
                    var lastMergedRow = -1
                    for (row in n - 1 downTo 0) {
                        val tile = grid[row][col] ?: continue
                        grid[row][col] = null

                        val prev = if (targetRow < n - 1) grid[targetRow + 1][col] else null
                        if (prev != null && prev.value == tile.value && lastMergedRow != targetRow + 1) {
                            grid[targetRow + 1][col] = prev.copy(value = prev.value * 2)
                            addScore(prev.value * 2)
                            lastMergedRow = targetRow + 1
                            moved = true
                        } else {
                            grid[targetRow][col] = tile.copy(row = targetRow)
                            if (row != targetRow) moved = true
                            targetRow--
                        }
                    }
                }
            }
        }
        //==========================================================================================
        if (moved) {
            tiles = grid.flatten().filterNotNull()
            spawnRandomTile()
            checkGameState()
        }
    }
    //==============================================================================================
    private fun checkGameState() {
        if (!hasContinuedAfterWin && tiles.any { it.value == 2048 }) {
            isGameWon = true
            return
        }

        if (tiles.size == n * n) {
            val grid = Array(n) { IntArray(n) { 0 } }
            tiles.forEach { grid[it.row][it.col] = it.value }

            var canMove = false
            for (row in 0 until n) {
                for (col in 0 until n) {
                    val current = grid[row][col]
                    if (row < n - 1 && current == grid[row + 1][col]) canMove = true
                    if (col < n - 1 && current == grid[row][col + 1]) canMove = true
                }
            }

            if (!canMove) {
                isGameOver = true
            }
        }
    }
    //==============================================================================================
    private fun spawnRandomTile() {
        val emptySpots = mutableListOf<Pair<Int, Int>>()
        val currentOccupied = tiles.map { it.row to it.col }.toSet()

        for (row in 0 until n) {
            for (col in 0 until n) {
                if (!currentOccupied.contains(row to col)) {
                    emptySpots.add(row to col)
                }
            }
        }

        if (emptySpots.isNotEmpty()) {
            val spot = emptySpots.random()
            val value = if (Random.nextFloat() < 0.9f) 2 else 4

            tiles = tiles + Tile(id = nextTileId++, value = value, row = spot.first, col = spot.second)
        }
    }
}