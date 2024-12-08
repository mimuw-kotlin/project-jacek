package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Knight(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int
) : Piece {
    override fun getPseudoMoves(board: Board): List<Move> {
        //All possible knight moves
        val allMoves = listOf(
            Pair(1, 2), Pair(1, -2), Pair(-1, 2),
            Pair(-1, -2), Pair(2, 1), Pair(2, -1), Pair(-2, 1), Pair(-2, -1)
        )

        val moves: MutableList<Move> = mutableListOf()
        for (move in allMoves) {
            val x = posX + move.first
            val y = posY + move.second

            //Square occupied by same color piece
            if (x < 0 || x > 7 || y < 0 || y > 7
                || (Pair(x, y) in board.piecesPositions.keys
                        && board.piecesPositions[Pair(x, y)]!!.color == color)
            ) {
                continue
            }
            moves.add(Move(posX, posY, x, y))

        }
        return moves
    }
}