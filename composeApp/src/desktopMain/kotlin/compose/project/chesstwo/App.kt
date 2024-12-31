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
import compose.project.chesstwo.composables.EndGameButton
import compose.project.chesstwo.composables.Logo
import compose.project.chesstwo.composables.MainMenuButton
import compose.project.chesstwo.composables.PieceSelection
import compose.project.chesstwo.composables.RightPanel
import compose.project.chesstwo.composables.SideButton
import compose.project.chesstwo.pieces.Piece
import org.jetbrains.compose.resources.DrawableResource
import kotlin.system.exitProcess

val buttonColor = Color(0xff89d0e8)

@Composable
fun ChessBoard(board: Board, onExitToMenu: () -> Unit) {

    var isPieceSelected by remember { mutableStateOf(false) }

    //Position of the selected piece
    var selectedPieceX by remember { mutableIntStateOf(-1) }
    var selectedPieceY by remember { mutableIntStateOf(-1) }

    //List of squares attacked by the selected piece
    var attackedSquares = remember { mutableStateListOf<Pair<Int, Int>>() }

    //A need to display pieces for promotion
    var promotionDisplay by remember { mutableStateOf(false) }

    //A need to display game result
    var endDisplay by remember { mutableStateOf(false) }



    //Textures of pieces available for promotion


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
                    for (row in 0 until board.size) {
                        Row {
                            for (col in 0 until board.size) {
                                Box(
                                    modifier = Modifier
                                        .size((800/board.size).dp)
                                        .background(
                                            if ((row + col) % 2 == 0) lightSquareColor
                                            else darkSquareColor

                                        )
                                        .clickable {

                                            //Clicked empty square
                                            if (isPieceSelected && !board.promotionChoice) {
                                                //Move if available
                                                if (Pair(col, row) in attackedSquares.toList()) {
                                                    board.move(
                                                        selectedPieceX,
                                                        selectedPieceY,
                                                        col,
                                                        row
                                                    )
                                                    endDisplay = board.gameEnded

                                                }
                                                isPieceSelected = false

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
                                                    if (isPieceSelected && Pair(
                                                            col,
                                                            row
                                                        ) in attackedSquares.toList()
                                                    ) {
                                                        board.move(
                                                            selectedPieceX,
                                                            selectedPieceY,
                                                            col,
                                                            row
                                                        )
                                                        endDisplay = board.gameEnded
                                                        isPieceSelected = false

                                                    }
                                                    //Selected another piece

                                                    else if (
                                                        newPiece.color == (board.turn) % 2) {

                                                        isPieceSelected = false

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
                                                        if (selectedPieceX != col
                                                            || selectedPieceY != row
                                                            || !isPieceSelected
                                                        ) {
                                                            selectedPieceX = col
                                                            selectedPieceY = row
                                                            isPieceSelected = true

                                                        }
                                                    }
                                                }
                                            }
                                        )

                                    }

                                    //Drawing squares attacked by selected piece
                                    if (isPieceSelected && Pair(col, row)
                                        in board.getAttackedSquares()
                                    ) {
                                        Canvas(
                                            modifier = Modifier
                                                .size((400/board.size).dp)
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
            promotionDisplay = board.promotionChoice
            RightPanel(
                promotionDisplay = promotionDisplay,
                onEndDisplayChange = {newValue ->  println("endDisplay change: $endDisplay -> $newValue");endDisplay =newValue},
                onExitToMenu = onExitToMenu,
                board = board,
                textures = textures,
            )
        }
        // Display of the result of the game
        if (endDisplay) {
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

@Composable
fun SettingUpScene(){
    var choosingColor by remember { mutableStateOf(0) }
    var selectedPiece by remember { mutableStateOf(Res.drawable.mmBackground)}
    val board by remember {mutableStateOf(SetUpBoard(8))}
    val points = remember{ mutableStateListOf(39,39)}
    var sideSelectedPiece by remember{ mutableStateOf(false)}
    //val board = SetUpBoard(8)
    println("Recompos $choosingColor")
    //println(numPieces)
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Row {
            Box(modifier = Modifier.width(175.dp)){
                Column{
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        SideButton(choosingColor=choosingColor, setChoosingColor =  {choosingColor=it; sideSelectedPiece = false})
                    }

                }
            }
            //Chessboard

            Box(modifier = Modifier.padding(25.dp)) {

                //Construction of the chessboard
                Column {
                    for (row in 0 until board.size) {
                        Row {
                            for (col in 0 until board.size) {
                                Box(
                                    modifier = Modifier
                                        .size((800/board.size).dp)
                                        .background(
                                            if(board.inAvailableSpace(row,choosingColor)){
                                                if ((row + col) % 2 == 0) lightSquareColor
                                                else darkSquareColor
                                            } else {
                                                if ((row + col) % 2 == 0) fogLightSquareColor
                                                else fogDarkSquareColor
                                            }


                                        )
                                        .clickable {
                                            if(sideSelectedPiece && Pair(col,row) !in board.piecesPositions.keys && board.inAvailableSpace(row,choosingColor)){
                                                board.placePiece(col,row,choosingColor)
                                                points[choosingColor]-=1
                                            }

                                        }

                                ){
                                    if(Pair(col,row) in board.piecesPositions.keys && board.piecesPositions[Pair(col,row)]!!.color==choosingColor){
                                        Image(
                                            painter = painterResource(
                                                board.piecesPositions[Pair(
                                                    col,
                                                    row
                                                )]!!.texture
                                            ),
                                            contentDescription = null,

                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box{
                Column{
                    Text(text = "Points left:\n${points[choosingColor]}")
                    PieceSelection(allTextures[choosingColor]) {
                        selectedPiece = it
                        board.selectedPieceTexture = it
                        sideSelectedPiece = true
                    }
                }

            }
        }

    }
}




sealed class Scene {
    data object MainMenu : Scene()
    data object Gameplay : Scene()
    data object SettingUp : Scene()
}


@Composable
@Preview
fun App() {
    //Scene that is currently displayed
    var currentScene by remember { mutableStateOf<Scene>(Scene.SettingUp) }

    var board = Board(size = 10)
    //board.setupPieces()
    MaterialTheme {
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame =
            { currentScene = Scene.Gameplay; board = Board(size=10); board.setupPieces() })

            is Scene.Gameplay -> ChessBoard(board, onExitToMenu =
            { currentScene = Scene.MainMenu })

            is Scene.SettingUp -> SettingUpScene()
        }
    }
}