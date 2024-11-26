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


val piecesPositions = mutableMapOf<Pair<Int,Int>, Piece>()



fun setupPieces(){
    piecesPositions[Pair(0,0)] = Rook(0,0,Res.drawable.rookB,1)
    piecesPositions[Pair(1,0)] = Knight(1,0,Res.drawable.knightB,1)
    piecesPositions[Pair(2,0)] = Bishop(2,0,Res.drawable.bishopB,1)
    piecesPositions[Pair(3,0)] = Queen(3,0,Res.drawable.queenB,1)
    piecesPositions[Pair(4,0)] = King(4,0,Res.drawable.kingB,1)
    piecesPositions[Pair(5,0)] = Bishop(5,0,Res.drawable.bishopB,1)
    piecesPositions[Pair(6,0)] = Knight(6,0,Res.drawable.knightB,1)
    piecesPositions[Pair(7,0)] = Rook(7,0,Res.drawable.rookB,1)

    piecesPositions[Pair(0,7)] = Rook(0,7,Res.drawable.rookW,0)
    piecesPositions[Pair(1,7)] = Knight(1,7,Res.drawable.knightW,0)
    piecesPositions[Pair(2,7)] = Bishop(2,7,Res.drawable.bishopW,0)
    piecesPositions[Pair(3,7)] = Queen(3,7,Res.drawable.queenW,0)
    piecesPositions[Pair(4,7)] = King(4,7,Res.drawable.kingW,0)
    piecesPositions[Pair(5,7)] = Bishop(5,7,Res.drawable.bishopW,0)
    piecesPositions[Pair(6,7)] = Knight(6,7,Res.drawable.knightW,0)
    piecesPositions[Pair(7,7)] = Rook(7,7,Res.drawable.rookW,0)

    for (i in 0 until 8){
        //piecesPositions[Pair(i,1)] = Pawn(i,1,Res.drawable.pawnB,1)
        //piecesPositions[Pair(i,6)] = Pawn(i,6,Res.drawable.pawnW,0)
    }

}

fun displayAttackedSquares(moves : List<Pair<Int,Int>>){

}


@Composable
fun ChessBoard() {
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

                            ) {
                                if (Pair(col, row) in piecesPositions.keys) {
                                    Image(
                                        painter = painterResource(
                                            piecesPositions[Pair(
                                                col,
                                                row
                                            )]!!.texture
                                        ),
                                        contentDescription = null,
                                        modifier = Modifier.clickable { attackedSquares.clear(); attackedSquares.addAll(piecesPositions[Pair(
                                            col,
                                            row
                                        )]!!.getMoves(piecesPositions));
                                            if(selectedPieceX.value==col && selectedPieceY.value==row && isPieceSelected.value){
                                                isPieceSelected.value=false
                                            }
                                            else{
                                                selectedPieceX.value=col;selectedPieceY.value=row;isPieceSelected.value=true;
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
    setupPieces()
    MaterialTheme {
        ChessBoard()
    }
}