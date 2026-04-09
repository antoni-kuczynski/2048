package com.kuczynski.game_2048

import android.annotation.SuppressLint
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.Animatable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable

//==================================================================================================
enum class AppTheme {
    LIGHT,
    DARK,
    SYSTEM
}
//==================================================================================================
@Composable
fun getThemeDisplayName(theme: AppTheme): String {
    if (theme == AppTheme.DARK) {
        return stringResource(id = R.string.theme_name_dark);
    } else if (theme == AppTheme.LIGHT) {
        return stringResource(id = R.string.theme_name_light);
    } else if (theme == AppTheme.SYSTEM) {
        return stringResource(id = R.string.theme_name_system);
    }
    return stringResource(id = R.string.theme_name_invalid)
}
//==================================================================================================
/**
 * System mode -> dark/light mode
 */
@Composable
fun resolveThemePalette(selectedTheme: AppTheme): ThemePalette {
    val actualTheme = if (selectedTheme == AppTheme.SYSTEM) {
        if (isSystemInDarkTheme()) AppTheme.DARK else AppTheme.LIGHT
    } else {
        selectedTheme
    }

    return getThemePalette(actualTheme)
}
//==================================================================================================
@Composable
fun GameScreen(viewModel: GameViewModel, themePalette: ThemePalette) {
    Column(
        modifier = Modifier.fillMaxSize()
            .onSwipe { direction ->
                if (!viewModel.settingsShown && !viewModel.isGameOver && !viewModel.isGameWon) {
                    viewModel.handleSwipe(direction)
                }
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val themePalette = resolveThemePalette(viewModel.currentTheme)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(id = R.string.game_name),
                fontSize = 52.sp,
                fontWeight = FontWeight.Black,
                color = themePalette.titleColor
            )
            //======================================================================================
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(themePalette.boardBackground, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(stringResource(id = R.string.score_CAPS), color = themePalette.tileColors.getValue(2), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("${viewModel.score}", color = themePalette.textLight, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
                //======================================================================================
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .background(themePalette.boardBackground, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(stringResource(id = R.string.best_score_CAPS), color = themePalette.tileColors.getValue(2), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("${viewModel.highScore}", color = themePalette.textLight, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        //==========================================================================================
        GameBoard(
            n = viewModel.n,
            tiles = viewModel.tiles,
            viewModel = viewModel
        )
        Spacer(modifier = Modifier.height(32.dp))
        //==========================================================================================
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button (
                onClick = { viewModel.startNewGame() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themePalette.buttonColor,
                    contentColor = themePalette.textLight
                ),
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = stringResource(id = R.string.new_game),
                    tint = themePalette.textLight
                )

                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

                Text(text = stringResource(id = R.string.new_game))
            }
            //======================================================================================
            Button (
                onClick = {viewModel.settingsShown = true},
                colors = ButtonDefaults.buttonColors(
                    containerColor = themePalette.buttonColor,
                    contentColor = themePalette.textLight
                ),

                ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(id = R.string.settings),
                    tint = themePalette.textLight
                )

                Spacer(modifier = Modifier.width(ButtonDefaults.IconSpacing))

                Text(text = stringResource(id = R.string.settings))
            }
        }
    }
    //==============================================================================================
    if (viewModel.isGameOver) {
        GameOverOverlay(viewModel = viewModel, themePalette = themePalette)
    }

    if (viewModel.isGameWon) {
        GameWonOverlay(viewModel = viewModel, themePalette = themePalette)
    }

    if (viewModel.settingsShown) {
        GameSettings(viewModel = viewModel)
    }
}
//==================================================================================================
@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun GameBoard(n: Int, viewModel: GameViewModel, tiles: List<Tile>) {
    val spacing = 8.dp
    val themePalette = resolveThemePalette(viewModel.currentTheme)

    BoxWithConstraints(
        modifier = Modifier
            .padding(16.dp)
            .aspectRatio(1f)
            .background(themePalette.boardBackground, RoundedCornerShape(8.dp))
            .padding(spacing)
    ) {
        val availableWidth = maxWidth - (spacing * (n - 1))
        val tileSize = availableWidth / n

        for (row in 0 until n) {
            for (col in 0 until n) {
                Box(
                    modifier = Modifier
                        .offset(
                            x = (tileSize + spacing) * col,
                            y = (tileSize + spacing) * row
                        )
                        .size(tileSize)
                        .background(themePalette.emptyCell, RoundedCornerShape(4.dp))
                )
            }
        }

        for (tile in tiles) {
            key(tile.id) {
                AnimatedTile(tile = tile, tileSize = tileSize, spacing = spacing, palette = themePalette)
            }
        }
    }
}
//==================================================================================================
@Composable
fun AnimatedTile(tile: Tile, tileSize: Dp, spacing: Dp, palette: ThemePalette) {
    val scale = remember { Animatable(0f) }

    LaunchedEffect(key1 = tile.id) {
        scale.animateTo(
            targetValue = 1f,
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }

    LaunchedEffect(key1 = tile.value) {
        if (scale.value >= 1f) {
            scale.animateTo(
                targetValue = 1.2f,
                animationSpec = tween(durationMillis = 100)
            )
            scale.animateTo(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 100)
            )
        }
    }

    val offsetX by animateDpAsState(
        targetValue = (tileSize + spacing) * tile.col,
        animationSpec = tween(durationMillis = 150),
        label = "TileX"
    )
    val offsetY by animateDpAsState(
        targetValue = (tileSize + spacing) * tile.row,
        animationSpec = tween(durationMillis = 150),
        label = "TileY"
    )

    val textValue = tile.value.toString()
    val textLength = textValue.length

    val fontSize = when (textLength) {
        1, 2 -> (tileSize.value * 0.4f).sp
        3 -> (tileSize.value * 0.33f).sp
        4 -> (tileSize.value * 0.25f).sp
        5 -> (tileSize.value * 0.2f).sp
        else -> (tileSize.value * 0.15f).sp
    }

    val textColor = if (tile.value <= 4) palette.textDark else palette.textLight

    var color = palette.tileColors.get(tile.value)
    if (color == null) {
        color = palette.tileColors.getValue(2048)
    }

    Box(
        modifier = Modifier
            .offset(x = offsetX, y = offsetY)
            .size(tileSize)
            .zIndex(if (scale.value > 1f) 1f else 0f)
            .scale(scale.value)
            .background(color, RoundedCornerShape(4.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = textValue,
            fontSize = fontSize,
            fontWeight = FontWeight.Bold,
            color = textColor,
            maxLines = 1,
            softWrap = false
        )
    }
}