package com.kuczynski.game_2048

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kuczynski.game_2048.ui.theme.Game_2048Theme

class GameViewModelFactory(private val context: Context, private val gridSize: Int) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            val manager = SettingsManager(context)
            @Suppress("UNCHECKED_CAST")
            return GameViewModel(settingsManager = manager, n = gridSize) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
//==================================================================================================
class MainActivity : ComponentActivity() {
    private val viewModel: GameViewModel by viewModels {
        GameViewModelFactory(gridSize = 4, context = applicationContext)
    }
//==================================================================================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Game_2048Theme {

                val isDarkTheme = viewModel.currentTheme == AppTheme.DARK
                LaunchedEffect(isDarkTheme) { //executed on theme change
                    enableEdgeToEdge(
                        statusBarStyle = if (isDarkTheme) {
                            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                        } else {
                            SystemBarStyle.light(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT
                            )
                        },
                        navigationBarStyle = if (isDarkTheme) {
                            SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
                        } else {
                            SystemBarStyle.light(
                                android.graphics.Color.TRANSPARENT,
                                android.graphics.Color.TRANSPARENT
                            )
                        }
                    )
                }

                val themePalette = resolveThemePalette(viewModel.currentTheme)
                val appGradient = Brush.verticalGradient(colors = themePalette.appBackgroundGradient)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(brush = appGradient),
                    color = Color.Transparent
                ) {
                    GameScreen(viewModel = viewModel, themePalette = themePalette)
                }
            }
        }
    }
}