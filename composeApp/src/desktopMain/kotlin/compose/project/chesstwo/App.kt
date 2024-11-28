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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize

import androidx.compose.ui.unit.dp
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
import compose.project.chesstwo.pieces.Bishop
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Knight
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Rook




@Composable
fun ChessBoard(board : Board) {
    //var size by remember { mutableStateOf(IntSize(400,400)) }
    var attackedSquares = remember {mutableStateListOf<Pair<Int,Int>>()}
    var selectedPieceX = remember { mutableIntStateOf(-1) }
    var selectedPieceY = remember { mutableIntStateOf(-1) }
    var isPieceSelected = remember { mutableStateOf(false) }
    Box( modifier = Modifier
        //.height(size.height.dp)
        //.width(size.width.dp)
        //.padding(10.dp)
        //.aspectRatio(1.0f)
        .background(Color(0xff89d0e8)).fillMaxSize()
        //.onSizeChanged {
        //    size = it
        //}
    ){
        Box(modifier = Modifier.align(Alignment.CenterStart).padding(horizontal = 50.dp)) {


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
                                    .clickable { if(isPieceSelected.value && Pair(col,row) in attackedSquares.toList()){
                                        board.move(selectedPieceX.value,selectedPieceY.value,col,row)
                                        isPieceSelected.value=false

                                    } }

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
                                            if(isPieceSelected.value && Pair(col,row) in attackedSquares.toList()){
                                                board.move(selectedPieceX.value,selectedPieceY.value,col,row)
                                                isPieceSelected.value=false

                                            }
                                            else {
                                                attackedSquares.clear();
                                                attackedSquares.addAll(
                                                    board.piecesPositions[Pair(col, row)]!!
                                                        .getMoves(board.piecesPositions)
                                                );
                                                if (selectedPieceX.value == col && selectedPieceY.value == row && isPieceSelected.value) {
                                                    isPieceSelected.value = false
                                                } else {
                                                    selectedPieceX.value =
                                                        col;selectedPieceY.value =
                                                        row;isPieceSelected.value = true;
                                                }
                                            }
                                        }

                                    )
                                }
                                if (isPieceSelected.value && Pair(col,row) in attackedSquares.toList()){
                                    Canvas(modifier = Modifier.size(50.dp).align(Alignment.Center), onDraw = {
                                        drawCircle(color = Color.Green, alpha = 0.5f)
                                    })
                                }

                            }
                        }
                    }
                }
            }
        }
    }

}

@Composable
@Preview
fun App() {
    var board = Board()
    board.setupPieces()
    MaterialTheme {
        ChessBoard(board)
    }
}