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
    override fun getMoves(board: Board): List<Move> {
        val directions = listOf(Pair(1,2),Pair(1,-2),Pair(-1,2),Pair(-1,-2),Pair(2,1),Pair(2,-1),Pair(-2,1),Pair(-2,-1))
        val moves :MutableList<Move> = mutableListOf()
        for (dir in directions){
            val x = posX+dir.first
            val y = posY+dir.second

            if (x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.piecesPositions.keys && board.piecesPositions[Pair(x,y)]!!.color==color)){
                continue
            }
            moves.addLast(Move(posX,posY,x,y, attacking = (Pair(x,y) in board.piecesPositions.keys && board.piecesPositions[Pair(x,y)]!!.color!=color)))

        }
        return moves
    }
}