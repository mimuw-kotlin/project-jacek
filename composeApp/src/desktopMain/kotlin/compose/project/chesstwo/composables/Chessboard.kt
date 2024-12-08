package compose.project.chesstwo.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.zIndex
import compose.project.chesstwo.Board
import compose.project.chesstwo.GameResult


@Composable
fun OneLineResultScreen(text: String) {
    Text(
        text = text,
        color = Color.Black,
        style = MaterialTheme.typography.h4
    )
}

@Composable
fun TwoLineResultScreen(text1: String, text2: String) {
    Column {
        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
            Text(
                text = text1,
                color = Color.Black,
                modifier = Modifier.align(Alignment.BottomCenter),

                style = MaterialTheme.typography.h4
            )
        }
        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
            Text(
                text = text2,
                color = Color.Black,
                modifier = Modifier.align(Alignment.TopCenter),
                style = MaterialTheme.typography.h4
            )
        }
    }
}

@Composable
fun EndDisplay(board: Board, onExitToMenu: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White.copy(alpha = 0.8f))
            .zIndex(1f)

            .clickable {
                onExitToMenu()
            }
    ) {
        when (board.gameResult) {
            GameResult.STALEMATE -> OneLineResultScreen("Stalemate")
            GameResult.DRAW -> OneLineResultScreen("Draw")
            else -> TwoLineResultScreen(
                "Check Mate",
                (if (board.gameResult == GameResult.WHITEWIN) "White" else "Black") + " won"
            )
        }

    }
}
