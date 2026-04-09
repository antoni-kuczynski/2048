package com.kuczynski.game_2048

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//==================================================================================================
@Composable
fun GameOverOverlay(viewModel: GameViewModel, themePalette: ThemePalette) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99B71C1C)) //red - lost
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(themePalette.dialogColor, RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.no_more_moves),
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = themePalette.titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.your_score) + " " + viewModel.score,
                fontSize = 20.sp,
                color = themePalette.titleColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { viewModel.startNewGame() },
                colors = ButtonDefaults.buttonColors(containerColor = themePalette.buttonColor)
            ) {
                Text(stringResource(id = R.string.try_again), color = themePalette.textLight, fontSize = 18.sp)
            }
        }
    }
}
//==================================================================================================
@Composable
fun GameWonOverlay(viewModel: GameViewModel, themePalette: ThemePalette) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x99FFC107)) //yellow - won
            .pointerInput(Unit) { detectTapGestures { } },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(themePalette.dialogColor, RoundedCornerShape(16.dp))
                .padding(32.dp)
        ) {
            Text(
                text = stringResource(id = R.string.you_won),
                fontSize = 36.sp,
                fontWeight = FontWeight.Black,
                color = themePalette.titleColor
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.got_2048),
                fontSize = 18.sp,
                color = themePalette.titleColor.copy(alpha = 0.7f)
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.keepPlaying() },
                colors = ButtonDefaults.buttonColors(containerColor = themePalette.buttonColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.keep_playing), color = themePalette.textLight, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedButton(
                onClick = { viewModel.startNewGame() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.new_game), color = themePalette.titleColor)
            }
        }
    }
}