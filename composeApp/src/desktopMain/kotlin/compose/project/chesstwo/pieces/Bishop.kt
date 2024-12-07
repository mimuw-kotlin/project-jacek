package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class Bishop(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int
) : Piece {

    override fun getPseudoMoves(board: Board): List<Move> {
        val directions = listOf(Pair(1,1),Pair(1,-1),Pair(-1,1),Pair(-1,-1))
        val moves :MutableList<Move> = mutableListOf()
        for (dir in directions){
            var x = posX+dir.first
            var y = posY+dir.second
            while(x in 0..7 && y in 0..7){
                if (Pair(x,y) in board.piecesPositions.keys){
                    if(board.piecesPositions[Pair(x,y)]!!.color!=color){

                        moves.addLast(Move(posX,posY,x,y))
                    }
                    break
                }
                moves.addLast(Move(posX,posY,x,y))

                x+=dir.first
                y+=dir.second
            }
        }
        return moves
    }
}