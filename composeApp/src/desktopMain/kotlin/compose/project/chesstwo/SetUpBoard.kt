package compose.project.chesstwo

import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.kingB
import chesstwo.composeapp.generated.resources.kingW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.pawnW
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Pawn
import org.jetbrains.compose.resources.DrawableResource

class SetUpBoard(size: Int = 8) : Board(size) {
    init {
        piecesPositions[Pair(size / 2, 0)] =
            King(size / 2, 0, Res.drawable.kingB, PlayerColor.BLACK)
        piecesPositions[Pair(size / 2, 1)] =
            Pawn(size / 2, 1, Res.drawable.pawnB, PlayerColor.BLACK)
        piecesPositions[Pair(size / 2, size - 1)] =
            King(size / 2, size - 1, Res.drawable.kingW, PlayerColor.WHITE)
        piecesPositions[Pair(size / 2, size - 2)] =
            Pawn(size / 2, size - 2, Res.drawable.pawnW, PlayerColor.WHITE)
    }

    lateinit var selectedPieceTexture: DrawableResource
    fun placePiece(posX: Int, posY: Int, color: PlayerColor) {
        piecesPositions[Pair(posX, posY)] = textureToPiece(selectedPieceTexture, posX, posY, color)
    }

    fun inAvailableSpace(posY: Int, color: PlayerColor): Boolean {
        if (color == PlayerColor.WHITE) return posY >= size / 2 + 1
        return posY < size / 2 - 1
    }
}