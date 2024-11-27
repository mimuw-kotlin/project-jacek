package compose.project.chesstwo.pieces

import org.jetbrains.compose.resources.DrawableResource

class Rook(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int
) : Piece {
    override fun getMoves(board: MutableMap<Pair<Int, Int>, Piece>): List<Pair<Int, Int>> {
        val directions = listOf(Pair(0,1),Pair(0,-1),Pair(1,0),Pair(-1,0))
        val moves :MutableList<Pair<Int, Int>> = mutableListOf()
        for (dir in directions){
            var x = posX+dir.first
            var y = posY+dir.second
            while(x in 0..7 && y in 0..7){
                if (Pair(x,y) in board.keys){
                    if(board[Pair(x,y)]!!.color!=color){
                        moves.addLast(Pair(x,y))
                    }
                    break
                }
                moves.addLast(Pair(x,y))
                x+=dir.first
                y+=dir.second
            }
        }
        return moves
    }
}