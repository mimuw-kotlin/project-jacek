package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Rook(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    var moved: Boolean = false
) : Piece {
    override fun getPseudoMoves(board: Board): List<Move> {
        //Directions in which rook can move
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        val moves: MutableList<Move> = mutableListOf()
        for (dir in directions) {
            var x = posX + dir.first
            var y = posY + dir.second
            while (x in 0..7 && y in 0..7) {
                if (Pair(x, y) in board.piecesPositions.keys) { //Found a piece in the way
                    if (board.piecesPositions[Pair(x, y)]!!.color != color) {
                        moves.addLast(Move(posX, posY, x, y))
                    }
                    break
                }
                moves.addLast(Move(posX, posY, x, y))
                x += dir.first
                y += dir.second
            }
        }
        return moves
    }

    override fun move(endX: Int, endY: Int, board: Board) {
        super.move(endX, endY, board)
        moved = true
    }
}