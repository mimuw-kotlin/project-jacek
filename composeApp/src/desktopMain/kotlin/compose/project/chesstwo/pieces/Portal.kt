package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Portal (override var posX: Int,
               override var posY: Int,
               override var texture: DrawableResource,
               override var color: Int,
               private var moved: Boolean = false
) : Piece{
    override val value = 12
    override fun getPseudoMoves(board: Board): List<Move> {
        //Directions in which queen can move
        val directions = listOf(
            Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0),
            Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)
        )

        val moves: MutableList<Move> = mutableListOf()
        for (dir in directions) {
            var x = posX + dir.first
            var y = posY + dir.second
            while (x in 0..<board.size && y in 0..<board.size) {
                if (Pair(x, y) in board.piecesPositions.keys) { //Found a piece in the way
                    if (ableToCapture(board,x,y)) {
                        moves.add(Move(posX, posY, x, y))
                    }
                    break
                }
                moves.add(Move(posX, posY, x, y))
                x += dir.first

                y += dir.second
                if (dir == Pair(1,0) || dir == Pair(-1,0)){
                    x = wrapMovement(x,board)
                    y = wrapMovement(y,board)
                }

            }
        }
        return moves
    }
    private fun wrapMovement(pos : Int, board : Board) : Int{
        if(pos==-1)
            return board.size-1
        if(pos==board.size)
            return 0
        return pos
    }
}