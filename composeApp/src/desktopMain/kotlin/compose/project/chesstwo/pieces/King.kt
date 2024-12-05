package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class King(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    var moved: Boolean = false
) : Piece {
    override fun getMoves(board: Board): List<Move> {
        val directions = listOf(Pair(0,1),Pair(0,-1),Pair(1,0),Pair(-1,0),Pair(1,1),Pair(1,-1),Pair(-1,1),Pair(-1,-1))
        val moves :MutableList<Move> = mutableListOf()
        for (dir in directions){
            val x = posX+dir.first
            val y = posY+dir.second

            if (x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.piecesPositions.keys && board.piecesPositions[Pair(x,y)]!!.color==color)){
                continue
            }
            moves.addLast(Move(posX,posY,x,y))
            // TODO implement not moving into attacking square and castling
        }
        if(!moved){
            if(Pair(0,posY) in board.piecesPositions.keys && board.piecesPositions[Pair(0,posY)] is Rook && !(board.piecesPositions[Pair(0,posY)] as Rook).moved &&
                Pair(1,posY) !in board.piecesPositions.keys && Pair(2,posY) !in board.piecesPositions.keys && Pair(3,posY) !in board.piecesPositions.keys){
                moves.addLast(Move(posX,posY,2,posY, isCastling = true))
            }
            if(Pair(7,posY) in board.piecesPositions.keys && board.piecesPositions[Pair(7,posY)] is Rook && !(board.piecesPositions[Pair(7,posY)] as Rook).moved &&
                Pair(5,posY) !in board.piecesPositions.keys && Pair(5,posY) !in board.piecesPositions.keys){
                moves.addLast(Move(posX,posY,6,posY, isCastling = true))
            }
        }
        return moves
    }
    override fun move(endX: Int, endY: Int, board: Board) {
        super.move(endX, endY, board)
        moved=true
    }
}