package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Bomb(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    var moved: Boolean = false
) : Piece {
    override val value = 3
    override fun getPseudoMoves(board: Board): List<Move> {
        //Directions in which rook can move
        val directions = listOf(Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0))
        val moves: MutableList<Move> = mutableListOf()
        for (dir in directions) {
            var x = posX + dir.first
            var y = posY + dir.second
            while (x in 0..<board.size && y in 0..<board.size) {
                if (Pair(x, y) in board.piecesPositions.keys) { //Found a piece in the way
                    if (board.piecesPositions[Pair(x, y)]!!.color != color) {
                        moves.add(Move(posX, posY, x, y, bombAttack = true))
                    }
                    break
                }
                moves.add(Move(posX, posY, x, y))
                x += dir.first
                y += dir.second
            }
        }
        return moves
    }
}