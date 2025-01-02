package compose.project.chesstwo

import chesstwo.composeapp.generated.resources.Res
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

fun classicScenario() : MutableMap<Pair<Int,Int>, Piece> {
    val piecesPositions = mutableMapOf<Pair<Int,Int>,Piece>()
    piecesPositions[Pair(0, 0)] = Rook(0, 0, Res.drawable.rookB, 1)
    piecesPositions[Pair(1, 0)] = Knight(1, 0, Res.drawable.knightB, 1)
    piecesPositions[Pair(2, 0)] = Bishop(2, 0, Res.drawable.bishopB, 1)
    piecesPositions[Pair(3, 0)] = Queen(3, 0, Res.drawable.queenB, 1)
    piecesPositions[Pair(4, 0)] = King(4, 0, Res.drawable.kingB, 1)
    piecesPositions[Pair(5, 0)] = Bishop(5, 0, Res.drawable.bishopB, 1)
    piecesPositions[Pair(6, 0)] = Knight(6, 0, Res.drawable.knightB, 1)
    piecesPositions[Pair(7, 0)] = Rook(7, 0, Res.drawable.rookB, 1)

    piecesPositions[Pair(0, 7)] = Rook(0, 7, Res.drawable.rookW, 0)
    piecesPositions[Pair(1, 7)] = Knight(1, 7, Res.drawable.knightW, 0)
    piecesPositions[Pair(2, 7)] = Bishop(2, 7, Res.drawable.bishopW, 0)
    piecesPositions[Pair(3, 7)] = Queen(3, 7, Res.drawable.queenW, 0)
    piecesPositions[Pair(4, 7)] = King(4, 7, Res.drawable.kingW, 0)
    piecesPositions[Pair(5, 7)] = Bishop(5, 7, Res.drawable.bishopW, 0)
    piecesPositions[Pair(6, 7)] = Knight(6, 7, Res.drawable.knightW, 0)
    piecesPositions[Pair(7, 7)] = Rook(7, 7, Res.drawable.rookW, 0)

    for (i in 0 until 8) {
        piecesPositions[Pair(i, 1)] = Pawn(i, 1, Res.drawable.pawnB, 1)
        piecesPositions[Pair(i, 6)] = Pawn(i, 6, Res.drawable.pawnW, 0)
    }
    return piecesPositions
}

fun sandomierzScenario() : MutableMap<Pair<Int,Int>, Piece> {
    val piecesPositions = mutableMapOf<Pair<Int,Int>,Piece>()
    piecesPositions[Pair(0, 0)] = Rook(0, 0, Res.drawable.rookB, 1)
    piecesPositions[Pair(1, 0)] = Knight(1, 0, Res.drawable.knightB, 1)
    piecesPositions[Pair(2, 0)] = Bishop(2, 0, Res.drawable.bishopB, 1)
    piecesPositions[Pair(3, 0)] = Queen(3, 0, Res.drawable.queenB, 1)
    piecesPositions[Pair(4, 0)] = King(4, 0, Res.drawable.kingB, 1)
    piecesPositions[Pair(5, 0)] = Bishop(5, 0, Res.drawable.bishopB, 1)
    piecesPositions[Pair(6, 0)] = Knight(6, 0, Res.drawable.knightB, 1)
    piecesPositions[Pair(7, 0)] = Rook(7, 0, Res.drawable.rookB, 1)

    piecesPositions[Pair(0, 7)] = Rook(0, 7, Res.drawable.rookW, 0)
    piecesPositions[Pair(1, 7)] = Knight(1, 7, Res.drawable.knightW, 0)
    piecesPositions[Pair(2, 7)] = Bishop(2, 7, Res.drawable.bishopW, 0)
    piecesPositions[Pair(3, 7)] = Queen(3, 7, Res.drawable.queenW, 0)
    piecesPositions[Pair(4, 7)] = King(4, 7, Res.drawable.kingW, 0)

    for (i in 0 until 8) {
        piecesPositions[Pair(i, 1)] = Pawn(i, 1, Res.drawable.pawnB, 1)
        piecesPositions[Pair(i, 6)] = Pawn(i, 6, Res.drawable.pawnW, 0)
    }
    return piecesPositions
}

fun lotrScenario() : MutableMap<Pair<Int,Int>, Piece> {
    val piecesPositions = mutableMapOf<Pair<Int,Int>,Piece>()
    piecesPositions[Pair(0, 0)] = Rook(0, 0, Res.drawable.rookB, 1)
    piecesPositions[Pair(6, 0)] = King(6, 0, Res.drawable.kingB, 1)
    piecesPositions[Pair(11, 0)] = Rook(11, 0, Res.drawable.rookB, 1)

    piecesPositions[Pair(6, 11)] = King(6, 11, Res.drawable.kingW, 0)

    for (i in 0 until 12) {
        piecesPositions[Pair(i, 1)] = Pawn(i, 1, Res.drawable.pawnB, 1)
        piecesPositions[Pair(i, 2)] = Pawn(i, 2, Res.drawable.pawnB, 1)
        piecesPositions[Pair(i, 3)] = Pawn(i, 3, Res.drawable.pawnB, 1)


        if(i!=6)piecesPositions[Pair(i, 11)] = Knight(i, 11, Res.drawable.knightW, 0)
        piecesPositions[Pair(i, 10)] = Knight(i, 10, Res.drawable.knightW, 0)

    }
    return piecesPositions
}