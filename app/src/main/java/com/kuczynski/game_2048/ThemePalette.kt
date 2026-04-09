package com.kuczynski.game_2048

import androidx.compose.ui.graphics.Color

data class ThemePalette(
    val appBackgroundGradient: List<Color>,
    val boardBackground: Color,
    val emptyCell: Color,
    val textLight: Color,
    val textDark: Color,
    val tileColors: Map<Int, Color>,
    val tileFallbackColor: Color,
    val buttonColor: Color,
    val titleColor: Color,
    val dialogColor: Color
)

fun getThemePalette(theme: AppTheme): ThemePalette {
    val light = ThemePalette(
        appBackgroundGradient = listOf(Color(0xFFFAF8EF), Color(0xFFF2EAD6), Color(0xFFEBE0C8)),
        boardBackground = Color(0xFFBBADA0),
        emptyCell = Color(0xFFCDC1B4),
        textLight = Color.White,
        textDark = Color(0xFF776E65),
        tileFallbackColor = Color(0xFF3C3A32),
        tileColors = mapOf(
            2 to Color(0xFFEEE4DA),
            4 to Color(0xFFEDE0C8),
            8 to Color(0xFFF2B179),
            16 to Color(0xFFF59563),
            32 to Color(0xFFF67C5F),
            64 to Color(0xFFF65E3B),
            128 to Color(0xFFEDCF72),
            256 to Color(0xFFEDCC61),
            512 to Color(0xFFEDC850),
            1024 to Color(0xFFEDC53F),
            2048 to Color(0xFFEDC22E)
        ),
        buttonColor = Color(0xFFEDC22E),
        titleColor = Color(0xFF776E65),
        dialogColor = Color(0xFFFAF8EF)

    );

    return when (theme) {
        AppTheme.LIGHT -> light
        AppTheme.DARK -> ThemePalette(
            appBackgroundGradient = listOf(Color(0xFF393956), Color(0xFF2A2A46), Color(0xFF22223B)),
            boardBackground = Color(0xFF494B69),
            emptyCell = Color(0xFF3B3F5B),
            textLight = Color(0xFFF2E9E4),
            textDark = Color(0xFF121212),
            tileFallbackColor = Color(0xFFB71C1C),
            tileColors = mapOf(
                2 to Color(0xFFD7DBF4),
                4 to Color(0xFFAFC0EA),
                8 to Color(0xFF809DE0),
                16 to Color(0xFF5472D3),
                32 to Color(0xFF384ED3),
                64 to Color(0xFF1D2CA1),

                128 to Color(0xFFD1B3FF),
                256 to Color(0xFFAA80FF),
                512 to Color(0xFF7E57C2),
                1024 to Color(0xFF5A3296),
                2048 to Color(0xFF3A116A)
            ),
            buttonColor = Color(0xFF4A4E69),
            titleColor = Color(0xFFF2E9E4),
            dialogColor = Color(0xFF393956)
        )
        else -> {
            light
        }
    }
}