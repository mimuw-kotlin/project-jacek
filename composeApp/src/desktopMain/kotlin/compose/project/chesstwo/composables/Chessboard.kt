package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import compose.project.chesstwo.Board
import compose.project.chesstwo.GameResult
import compose.project.chesstwo.buttonColor
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource


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
@Composable
fun EndGameButton(onExitToMenu: ()->Unit){
    Button(
        modifier = Modifier.fillMaxWidth().height(60.dp),
        onClick = { onExitToMenu() },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
    ) {
        Text(text = "End game", style = MaterialTheme.typography.h6)
    }

}
@Composable
fun RightPanel(promotionDisplay : Boolean,
               onEndDisplayChange : (Boolean)->Unit,
               onExitToMenu: ()->Unit,
               board : Board, textures : List<List<DrawableResource>>){

    Box {

        Column(modifier = Modifier.fillMaxSize().padding(25.dp)) {

            //Space for displaying pieces for promotion
            Box(modifier = Modifier.fillMaxWidth().weight(5.0f)) {
                //promotionDisplayPanel = board.promotionChoice

                //if (promotionDisplay.value) {
                if(promotionDisplay){
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            "Choose a piece for promotion",
                            modifier = Modifier
                                .align(alignment = Alignment.CenterHorizontally),
                            style = MaterialTheme.typography.h6
                        )
                        for (t in textures[board.turn % 2]) {
                            Image(painter = painterResource(t),
                                contentDescription = null,
                                modifier = Modifier
                                    .align(alignment = Alignment.CenterHorizontally)
                                    .clickable {
                                        board.finishPromotionMove(t)
                                        //promotionDisplayPanel = board.promotionChoice
                                        //onPromotionDisplayChange(board.promotionChoice)
                                        //endDisplayPanel = board.gameEnded
                                        onEndDisplayChange(board.gameEnded)
                                    })
                        }
                    }


                }
            }
            // Button for ending the game
            Box(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                EndGameButton(onExitToMenu)
            }

        }
    }
}
