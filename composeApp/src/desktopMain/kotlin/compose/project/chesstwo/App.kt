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
import androidx.compose.foundation.onClick
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import compose.project.chesstwo.Board
import chesstwo.composeapp.generated.resources.Res

import chesstwo.composeapp.generated.resources.kingW
import chesstwo.composeapp.generated.resources.kingB
import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.pawnW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.mmBackground
import compose.project.chesstwo.pieces.Bishop
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Knight
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Rook
import java.lang.Thread.sleep
import kotlin.system.exitProcess


@Composable
fun ChessBoard(board : Board, onExitToMenu : () -> Unit) {
    //var size by remember { mutableStateOf(IntSize(400,400)) }
    var attackedSquares = remember {mutableStateListOf<Pair<Int,Int>>()}
    var selectedPieceX = remember { mutableIntStateOf(-1) }
    var selectedPieceY = remember { mutableIntStateOf(-1) }
    var isPieceSelected = remember { mutableStateOf(false) }
    var promotionDisplay = remember { mutableStateOf(false) }
    var endDisplay = remember { mutableStateOf(false) }

    Image(painter = painterResource((Res.drawable.mmBackground)), contentDescription = "Background",alpha = 0.4f)
    Box( modifier = Modifier
        //.height(size.height.dp)
        //.width(size.width.dp)
        //.padding(10.dp)
        //.aspectRatio(1.0f)
        .background(Color(0x9989d0e8)).fillMaxSize()
        //.onSizeChanged {
        //    size = it
        //}
    ){
        Row {
            Box(modifier = Modifier.padding(25.dp)) {//modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 50.dp)


                Column {
                    for (row in 0 until 8) {
                        Row {
                            for (col in 0 until 8) {
                                Box(
                                    modifier = Modifier
                                        //.size(width = (size.width/8).dp,height = (size.height/8).dp)
                                        .size(100.dp)
                                        .background(
                                            if ((row + col) % 2 == 0) Color(0xffe0e0e0) else Color(
                                                0xffc7a25a
                                            )
                                        )
                                        .clickable {
                                            if (isPieceSelected.value) {
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
                                        Image(
                                            painter = painterResource(
                                                board.piecesPositions[Pair(
                                                    col,
                                                    row
                                                )]!!.texture
                                            ),
                                            contentDescription = null,
                                            modifier = Modifier.clickable {

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

                                                } else if (board.piecesPositions[Pair(
                                                        col,
                                                        row
                                                    )]!!.color == (board.turn) % 2
                                                ) {

                                                    isPieceSelected.value = false
                                                    board.possibleMoves.clear()
                                                    board.possibleMoves.addAll(
                                                        board.piecesPositions[Pair(col, row)]!!
                                                            .getMoves(board)
                                                    )
                                                    attackedSquares.clear()
                                                    attackedSquares.addAll(board.getAttackedSquares())
                                                    if (selectedPieceX.value != col || selectedPieceY.value != row || !isPieceSelected.value) {
                                                        selectedPieceX.value = col;
                                                        selectedPieceY.value = row;
                                                        isPieceSelected.value = true;

                                                    }
                                                }
                                            }

                                        )
                                    }
                                    if (isPieceSelected.value && Pair(
                                            col,
                                            row
                                        ) in board.getAttackedSquares()
                                    ) {
                                        Canvas(
                                            modifier = Modifier.size(50.dp).align(Alignment.Center),
                                            onDraw = {
                                                drawCircle(color = Color.Green, alpha = 0.5f)
                                            })
                                    }
                                    /*if (board.turn>0 && isPieceSelected.value && Pair(col,row) in board.allAttackedSquares[board.turn%2]){
                                        Canvas(modifier = Modifier.size(50.dp).align(Alignment.Center), onDraw = {
                                            drawCircle(color = Color.Red, alpha = 0.5f)
                                        })
                                    }*/

                                }
                            }
                        }
                    }
                }
            }
            Box() {
                Column(modifier = Modifier.fillMaxSize().padding(25.dp)){
                    Box(modifier = Modifier.fillMaxWidth().weight(1.0f).background(color=Color.Green)){

                    }
                    //promotionDisplay.value = board.promotionChoice
                    println("oooooooooooooooooooooooooo")
                    print(promotionDisplay.value)
                    print("   ")
                    println(board.promotionChoice)
                    println("oooooooooooooooooooooooooo")
                    Box(modifier = Modifier.fillMaxWidth().weight(5.0f)){
                        if(board.promotionChoice){
                            promotionDisplay.value=true
                            val textures = listOf(listOf(Res.drawable.queenW,Res.drawable.rookW,Res.drawable.bishopW,Res.drawable.knightW),
                                listOf(Res.drawable.queenB,Res.drawable.rookB,Res.drawable.bishopB,Res.drawable.knightB))
                            Column(modifier = Modifier.fillMaxSize()) {
                                Text("Choose a piece for promotion", modifier = Modifier.align(alignment = Alignment.CenterHorizontally))
                                for (t in textures[board.turn % 2]) {
                                    Image(painter = painterResource(t), contentDescription = null, modifier = Modifier.align(alignment = Alignment.CenterHorizontally).clickable {
                                        board.finishPromotionMove(t)
                                        promotionDisplay.value=false
                                        endDisplay.value = board.gameEnded
                                    })
                                }
                            }


                        }
                    }
                    Box(modifier = Modifier.fillMaxWidth().weight(1.0f).background(color=Color.Red)){

                    }
                }
            }
        }
        if(endDisplay.value){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.8f))
                    .zIndex(1f)
                    .align(Alignment.Center)
                    .clickable {
                        onExitToMenu()
                    }
            ) {
                if(board.gameResult==2){
                    Text(
                        text = "Stalemate",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h6
                    )
                }
                else if(board.gameResult==3){
                    Text(
                        text = "Draw",
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.h6
                    )
                }
                else{
                    Column() {
                        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
                            Text(
                                text = "Check Mate",
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Center),

                                style = MaterialTheme.typography.h6
                            )
                        }
                        Box(modifier = Modifier.weight(1.0f).fillMaxWidth()) {
                            Text(
                                text = (if (board.gameResult == 0) "White" else "Black") + " won",
                                color = Color.Black,
                                modifier = Modifier.align(Alignment.Center),
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                }

            }

        }
    }

}


@Composable
fun MainMenuScene(onStartGame: () -> Unit) {
    Image(painter = painterResource((Res.drawable.mmBackground)), contentDescription = "Background",alpha = 0.7f)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x7689d0e8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(Res.drawable.pawnW),
            contentDescription = "Game Logo",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(100.dp))
        // Button: Start Game
        Button(
            onClick = onStartGame,
            modifier = Modifier
                .width(300.dp)
                .height(100.dp)
                .padding(vertical = 20.dp)
        ) {
            Text(text = "Start Game")
        }


        Button(
            onClick = { },
            modifier = Modifier
                .width(300.dp)
                .height(100.dp)
                .padding(vertical = 20.dp)
        ) {
            Text(text = "Play with the computer W.I.P.")
        }


        Button(
            onClick = {exitProcess(0)},
            modifier = Modifier
                .width(300.dp)
                .height(100.dp)
                .padding(vertical = 20.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        ) {
            Text(text = "Exit", color = Color.White)
        }
    }
}


sealed class Scene {
    object MainMenu : Scene()
    object Gameplay : Scene()
}


@Composable
@Preview
fun App() {
    var currentScene by remember { mutableStateOf<Scene>(Scene.MainMenu) }
    var board = Board()
    board.setupPieces()
    MaterialTheme {
        //ChessBoard(board,currentScene)
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame = { currentScene = Scene.Gameplay; board = Board(); board.setupPieces()})
            is Scene.Gameplay -> ChessBoard(board, onExitToMenu = { currentScene = Scene.MainMenu })
        }
    }
}