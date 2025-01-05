package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Prince (override var posX: Int,
              override var posY: Int,
              override var texture: DrawableResource,
              override var color: Int,
              private var moved: Boolean = false
) : Piece{
    override val value = 2
    override fun getPseudoMoves(board: Board): List<Move> {
        val allMoves = listOf(
            Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0),
            Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)
        )
        val moves: MutableList<Move> = mutableListOf()
        for (move in allMoves) {
            val x = posX + move.first
            val y = posY + move.second

            //Square occupied by same color piece
            if (x < 0 || x >=board.size || y < 0 || y >= board.size ||
                (Pair(x, y) in board.piecesPositions.keys
                        && board.piecesPositions[Pair(x, y)]!!.color == color)
            ) {
                continue
            }
            moves.add(Move(posX, posY, x, y))

        }
        return moves
    }
}