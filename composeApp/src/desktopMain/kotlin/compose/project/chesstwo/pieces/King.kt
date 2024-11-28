package compose.project.chesstwo.pieces

import org.jetbrains.compose.resources.DrawableResource

class King(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int
) : Piece {
    override fun getMoves(board: MutableMap<Pair<Int, Int>, Piece>): List<Pair<Int, Int>> {
        val directions = listOf(Pair(0,1),Pair(0,-1),Pair(1,0),Pair(-1,0),Pair(1,1),Pair(1,-1),Pair(-1,1),Pair(-1,-1))
        val moves :MutableList<Pair<Int, Int>> = mutableListOf()
        for (dir in directions){
            val x = posX+dir.first
            val y = posY+dir.second

            if (x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.keys && board[Pair(x,y)]!!.color==color)){
                continue
            }
            moves.addLast(Pair(x,y))
            // TODO implement not moving into attacking square and castling
        }
        return moves
    }
}