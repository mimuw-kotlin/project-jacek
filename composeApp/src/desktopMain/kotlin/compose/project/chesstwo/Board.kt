package compose.project.chesstwo

import  chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.kingB
import chesstwo.composeapp.generated.resources.kingW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.pawnW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import compose.project.chesstwo.pieces.Bishop
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Knight
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Rook

class Board {
    public val piecesPositions = mutableMapOf<Pair<Int,Int>, Piece>()
    public var turn = 0
    public fun setupPieces(){
        piecesPositions[Pair(0,0)] = Rook(0,0, Res.drawable.rookB,1)
        piecesPositions[Pair(1,0)] = Knight(1,0, Res.drawable.knightB,1)
        piecesPositions[Pair(2,0)] = Bishop(2,0, Res.drawable.bishopB,1)
        piecesPositions[Pair(3,0)] = Queen(3,0, Res.drawable.queenB,1)
        piecesPositions[Pair(4,0)] = King(4,0, Res.drawable.kingB,1)
        piecesPositions[Pair(5,0)] = Bishop(5,0, Res.drawable.bishopB,1)
        piecesPositions[Pair(6,0)] = Knight(6,0, Res.drawable.knightB,1)
        piecesPositions[Pair(7,0)] = Rook(7,0, Res.drawable.rookB,1)

        piecesPositions[Pair(0,7)] = Rook(0,7, Res.drawable.rookW,0)
        piecesPositions[Pair(1,7)] = Knight(1,7, Res.drawable.knightW,0)
        piecesPositions[Pair(2,7)] = Bishop(2,7, Res.drawable.bishopW,0)
        piecesPositions[Pair(3,7)] = Queen(3,7, Res.drawable.queenW,0)
        piecesPositions[Pair(4,7)] = King(4,7, Res.drawable.kingW,0)
        piecesPositions[Pair(5,7)] = Bishop(5,7, Res.drawable.bishopW,0)
        piecesPositions[Pair(6,7)] = Knight(6,7, Res.drawable.knightW,0)
        piecesPositions[Pair(7,7)] = Rook(7,7, Res.drawable.rookW,0)

        for (i in 0 until 8){
            piecesPositions[Pair(i,1)] = Pawn(i,1, Res.drawable.pawnB,1)
            piecesPositions[Pair(i,6)] = Pawn(i,6, Res.drawable.pawnW,0)
        }

    }

    public fun move(startX : Int, startY : Int, endX : Int, endY : Int){
        piecesPositions[Pair(endX,endY)] = piecesPositions[Pair(startX,startY)]!!
        piecesPositions.remove(Pair(startX,startY))
        piecesPositions[Pair(endX,endY)]!!.posX  = endX
        piecesPositions[Pair(endX,endY)]!!.posY  = endY
        turn = (turn+1)%2
    }
}