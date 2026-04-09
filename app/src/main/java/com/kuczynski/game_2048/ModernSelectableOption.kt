package com.kuczynski.game_2048

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ModernSelectableOption(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    themePalette: ThemePalette,
    modifier: Modifier = Modifier
) {
    //==============================================================================================
    // animations
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) themePalette.titleColor else Color.Transparent,
        label = "bg_anim"
    )
    val textColor by animateColorAsState(
        targetValue = if (isSelected) themePalette.dialogColor else themePalette.titleColor,
        label = "text_anim"
    )
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) themePalette.titleColor else themePalette.titleColor.copy(alpha = 0.3f),
        label = "border_anim"
    )
    //==============================================================================================
    // button
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
    //==============================================================================================
}