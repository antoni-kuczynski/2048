package com.kuczynski.game_2048

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameSettings(viewModel: GameViewModel) {
    val themePalette = resolveThemePalette(viewModel.currentTheme)
    val appGradient = Brush.verticalGradient(colors = themePalette.appBackgroundGradient)

    BackHandler(onBack = { viewModel.settingsShown = false })
//==================================================================================================
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = appGradient)
            .systemBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //==========================================================================================
        //          TITLE
        //==========================================================================================
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { viewModel.settingsShown = false }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Close",
                    tint = themePalette.titleColor
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = stringResource(id = R.string.settings),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = themePalette.titleColor
            )
        }
        //==========================================================================================
        //          THEME
        //==========================================================================================
        TextSeparator(text = stringResource(id = R.string.theme_str), color = themePalette.titleColor)
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AppTheme.entries.forEach { themeOption ->
                ModernSelectableOption(
                    text = getThemeDisplayName(themeOption),
                    isSelected = (themeOption == viewModel.currentTheme),
                    onClick = { viewModel.currentTheme = themeOption },
                    themePalette = themePalette,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        //==========================================================================================
        //          BOARD SIZE
        //==========================================================================================
        TextSeparator(text = stringResource(id = R.string.board_size), color = themePalette.titleColor)
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sizes = listOf(3, 4, 5, 6)

            sizes.forEach { size ->
                ModernSelectableOption(
                    text = "${size}x${size}",
                    isSelected = (viewModel.n == size),
                    onClick = {
                        viewModel.changeBoardSize(size)
                    },
                    themePalette = themePalette,
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                )
            }
        }
        //==========================================================================================
        Text(
            text = stringResource(id = R.string.size_change_note),
            modifier = Modifier.padding(horizontal = 16.dp),
            color = themePalette.titleColor.copy(alpha = 0.5F),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )
        //==========================================================================================
        Spacer(modifier = Modifier.weight(1f))
    }
}