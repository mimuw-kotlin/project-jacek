package compose.project.chesstwo.composables

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.mmBackground
import compose.project.chesstwo.Board
import compose.project.chesstwo.Computer
import compose.project.chesstwo.GameResult
import compose.project.chesstwo.PlayerColor
import compose.project.chesstwo.buttonColor
import compose.project.chesstwo.darkSquareColor
import compose.project.chesstwo.lightSquareColor
import compose.project.chesstwo.textureToString
import compose.project.chesstwo.textures
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource


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
               board : Board, textures : List<List<DrawableResource>>,
               computerGame : Boolean){

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

@Composable
fun ChessBoard(board: Board, onExitToMenu: () -> Unit, gameWithComputer: Boolean = false, computerColor : PlayerColor = PlayerColor.BLACK) {

    var isPieceSelected by remember { mutableStateOf(false) }

    //Position of the selected piece
    var selectedPieceX by remember { mutableStateOf(0) }
    var selectedPieceY by remember { mutableStateOf(0) }

    //List of squares attacked by the selected piece
    val attackedSquares = remember { mutableStateListOf<Pair<Int, Int>>() }

    //A need to display pieces for promotion
    var promotionDisplay by remember { mutableStateOf(false) }

    //A need to display game result
    var endDisplay by remember { mutableStateOf(false) }

    var computerToPlay by remember {mutableStateOf(gameWithComputer && computerColor==PlayerColor.WHITE)}//(!gameWithComputer || !computerToPlay
    val computer = Computer(computerColor)
    println("EWALUACJA: ${computer.evaluate(board)}")


    if(gameWithComputer && computerToPlay && !board.promotionChoice){
        //println("SEARCH: ${computer.search(board,3, -1001,1001)}")
        val move = computer.makeMove(board)
        if(move!=null){
            board.possibleMoves.clear()
            board.possibleMoves.addAll(board.piecesPositions[Pair(move.startX,move.startY)]!!.getMoves(board))
            board.move(move.startX,move.startY,move.endX,move.endY, computerMove = true)

        }

        endDisplay = board.gameEnded
        computerToPlay = false
    }

    //Textures of pieces available for promotion


    //Background image
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Chessboard background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Row {

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
                                            if (isPieceSelected && !board.promotionChoice && !(gameWithComputer && computerToPlay)) {
                                                //Move if available
                                                if (Pair(col, row) in attackedSquares.toList()) {
                                                    board.move(
                                                        selectedPieceX,
                                                        selectedPieceY,
                                                        col,
                                                        row
                                                    )
                                                    endDisplay = board.gameEnded
                                                    computerToPlay = true

                                                }
                                                isPieceSelected = false

                                            }
                                        }

                                ) {
                                    if (Pair(col, row) in board.piecesPositions.keys) {
                                        // Image of a piece on that square
                                        val currentTexture = board.piecesPositions[Pair(
                                            col,
                                            row
                                        )]!!.texture
                                        Image(
                                            painter = painterResource(
                                                currentTexture
                                            ),
                                            contentDescription = if(textureToString(currentTexture) !=null) stringResource(
                                                textureToString(currentTexture)!!.first) else null,
                                            modifier = Modifier.clickable {
                                                if (!board.promotionChoice && !(gameWithComputer && computerToPlay)) {
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
                                                        computerToPlay = true

                                                    }
                                                    //Selected another piece

                                                    else if (
                                                        newPiece.color == PlayerColor.turnToColor(board.turn)) {

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
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                if(!gameWithComputer){
                    Spacer(modifier = Modifier.height(25.dp))
                    val backgroundColor = if(PlayerColor.turnToColor(board.turn) == PlayerColor.WHITE)Color.White else Color.Black
                    Box(modifier = Modifier.background(backgroundColor).width(300.dp).height(60.dp), contentAlignment = Alignment.Center) {
                        if (PlayerColor.turnToColor(board.turn) == PlayerColor.WHITE) {
                            Text(
                                text = "White to move",
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                style = MaterialTheme.typography.h6
                            )
                        } else {
                            Text(
                                text = "Black to move",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                }
                promotionDisplay = board.promotionChoice
                RightPanel(
                    promotionDisplay = promotionDisplay,
                    onEndDisplayChange = {newValue ->  println("endDisplay change: $endDisplay -> $newValue");endDisplay =newValue},
                    onExitToMenu = onExitToMenu,
                    board = board,
                    textures = textures,
                    computerGame = gameWithComputer
                )

            }

        }
        // Display of the result of the game
        if (endDisplay) {
            EndDisplay(board, onExitToMenu)

        }
    }

}
