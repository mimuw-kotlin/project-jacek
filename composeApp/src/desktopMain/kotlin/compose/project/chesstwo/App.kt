package compose.project.chesstwo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column

import androidx.compose.material.MaterialTheme

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res

import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.mmBackground
import compose.project.chesstwo.composables.EndDisplay
import compose.project.chesstwo.composables.Logo
import compose.project.chesstwo.composables.MainMenuButton
import kotlin.system.exitProcess

val buttonColor = Color(0xff89d0e8)

@Composable
fun ChessBoard(board: Board, onExitToMenu: () -> Unit) {

    val isPieceSelected = remember { mutableStateOf(false) }

    //Position of the selected piece
    val selectedPieceX = remember { mutableIntStateOf(-1) }
    val selectedPieceY = remember { mutableIntStateOf(-1) }

    //List of squares attacked by the selected piece
    val attackedSquares = remember { mutableStateListOf<Pair<Int, Int>>() }

    //A need to display pieces for promotion
    val promotionDisplay = remember { mutableStateOf(false) }

    //A need to display game result
    val endDisplay = remember { mutableStateOf(false) }

    val lightSquareColor = Color(0xffe0e0e0)
    val darkSquareColor = Color(0xffc7a25a)

    //Textures of pieces available for promotion
    val textures = listOf(
        listOf(
            Res.drawable.queenW, Res.drawable.rookW,
            Res.drawable.bishopW, Res.drawable.knightW
        ),
        listOf(
            Res.drawable.queenB, Res.drawable.rookB,
            Res.drawable.bishopB, Res.drawable.knightB
        )
    )

    //Background image
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Row {

            //Chessboard
            Box(modifier = Modifier.padding(25.dp)) {

                //Construction of the chessboard
                Column {
                    for (row in 0 until 8) {
                        Row {
                            for (col in 0 until 8) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .background(
                                            if ((row + col) % 2 == 0) lightSquareColor
                                            else darkSquareColor

                                        )
                                        .clickable {

                                            //Clicked empty square
                                            if (isPieceSelected.value && !board.promotionChoice) {
                                                //Move if available
                                                if (Pair(col, row) in attackedSquares.toList()) {
                                                    board.move(
                                                        selectedPieceX.value,
                                                        selectedPieceY.value,
                                                        col,
                                                        row
                                                    )
                                                    endDisplay.value = board.gameEnded

                                                }
                                                isPieceSelected.value = false

                                            }
                                        }

                                ) {
                                    if (Pair(col, row) in board.piecesPositions.keys) {
                                        // Image of a piece on that square
                                        Image(
                                            painter = painterResource(
                                                board.piecesPositions[Pair(
                                                    col,
                                                    row
                                                )]!!.texture
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.clickable {
                                                if (!board.promotionChoice) {
                                                    val newPiece = board
                                                        .piecesPositions[Pair(col, row)]!!
                                                    //Attacked another piece
                                                    if (isPieceSelected.value && Pair(
                                                            col,
                                                            row
                                                        ) in attackedSquares.toList()
                                                    ) {
                                                        board.move(
                                                            selectedPieceX.value,
                                                            selectedPieceY.value,
                                                            col,
                                                            row
                                                        )
                                                        endDisplay.value = board.gameEnded
                                                        isPieceSelected.value = false

                                                    }
                                                    //Selected another piece

                                                    else if (
                                                        newPiece.color == (board.turn) % 2) {

                                                        isPieceSelected.value = false

                                                        //Getting all possible moves
                                                        //for newly selected piece
                                                        board.possibleMoves.clear()
                                                        board.possibleMoves.addAll(
                                                            newPiece.getMoves(board)
                                                        )

                                                        //Getting all attacked squares
                                                        //for newly selected piece
                                                        attackedSquares.clear()
                                                        attackedSquares
                                                            .addAll(board.getAttackedSquares())

                                                        //Update selected piece information
                                                        if (selectedPieceX.value != col
                                                            || selectedPieceY.value != row
                                                            || !isPieceSelected.value
                                                        ) {
                                                            selectedPieceX.value = col
                                                            selectedPieceY.value = row
                                                            isPieceSelected.value = true

                                                        }
                                                    }
                                                }
                                            }
                                        )

                                    }

                                    //Drawing squares attacked by selected piece
                                    if (isPieceSelected.value && Pair(col, row)
                                        in board.getAttackedSquares()
                                    ) {
                                        Canvas(
                                            modifier = Modifier
                                                .size(50.dp)
                                                .align(Alignment.Center),
                                            onDraw = {
                                                drawCircle(color = Color.Green, alpha = 0.5f)
                                            })
                                    }

                                }
                            }
                        }
                    }
                }
            }
            //Right panel
            Box {
                Column(modifier = Modifier.fillMaxSize().padding(25.dp)) {

                    //Space for displaying pieces for promotion
                    Box(modifier = Modifier.fillMaxWidth().weight(5.0f)) {
                        promotionDisplay.value = board.promotionChoice
                        if (promotionDisplay.value) {
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
                                                promotionDisplay.value = board.promotionChoice
                                                endDisplay.value = board.gameEnded
                                            })
                                }
                            }


                        }
                    }
                    // Button for ending the game
                    Box(modifier = Modifier.fillMaxWidth().weight(1.0f)) {
                        Button(
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            onClick = { onExitToMenu() },
                            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)

                        ) {
                            Text(text = "End game", style = MaterialTheme.typography.h6)
                        }
                    }
                }
            }
        }
        // Display of the result of the game
        if (endDisplay.value) {
            EndDisplay(board, onExitToMenu)

        }
    }

}


@Composable
fun MainMenuScene(onStartGame: () -> Unit) {

    //Background
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Background",
        alpha = 0.7f
    )

    //Buttons
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x7689d0e8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Logo()
        Spacer(modifier = Modifier.height(100.dp))
        MainMenuButton(
            onStartGame, "Start game", Color.Black,
            ButtonDefaults.buttonColors(backgroundColor = buttonColor)
        )

        MainMenuButton(
            {}, "Play the computer W.I.P.", Color.Black,
            ButtonDefaults.buttonColors(backgroundColor = buttonColor)
        )

        MainMenuButton(
            { exitProcess(0) }, "Exit", Color.White,
            ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        )
    }
}


sealed class Scene {
    data object MainMenu : Scene()
    data object Gameplay : Scene()
}


@Composable
@Preview
fun App() {
    //Scene that is currently displayed
    var currentScene by remember { mutableStateOf<Scene>(Scene.MainMenu) }

    var board = Board()
    board.setupPieces()
    MaterialTheme {
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame =
            { currentScene = Scene.Gameplay; board = Board(); board.setupPieces() })

            is Scene.Gameplay -> ChessBoard(board, onExitToMenu =
            { currentScene = Scene.MainMenu })
        }
    }
}