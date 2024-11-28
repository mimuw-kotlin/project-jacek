package compose.project.chesstwo.pieces

import org.jetbrains.compose.resources.DrawableResource

class Pawn(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    var moved: Boolean = false
) : Piece {
    override fun getMoves(board: MutableMap<Pair<Int, Int>, Piece>): List<Pair<Int, Int>> {
        val moves :MutableList<Pair<Int, Int>> = mutableListOf()
        val potentialMoves:MutableList<Pair<Int, Int>> = mutableListOf(if (color==1) Pair(posX,posY+1) else Pair(posX,posY-1))
        if(!moved){
            potentialMoves.add(if (color==1) Pair(posX,posY+2) else Pair(posX,posY-2))
        }
        // TODO implement en passant
        for (move in potentialMoves){
            val x = move.first
            val y = move.second

            if (x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.keys && board[Pair(x,y)]!!.color==color)){
                continue
            }
            moves.addLast(Pair(x,y))

        }
        return moves
    }
}