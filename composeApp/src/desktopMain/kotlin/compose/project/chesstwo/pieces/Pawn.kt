package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource
import kotlin.math.abs

class Pawn(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    var moved: Boolean = false,
    var turnOfDoubleMove : Int = -1
) : Piece {
    override fun getPseudoMoves(board: Board): List<Move> {
        val moves:MutableList<Move> = mutableListOf()
        /*if(!moved){
            moves.add(if (color==1) Move(posX,posY,posX,posY+2) else Move(posX,posY,posX,posY-2))
        }*/
        var nextRow = if (color==0) posY-1 else posY+1
        for (d in listOf(-1,1)){
            //Attacking on diagonal
            if (board.inBounds(posX+d,nextRow) && Pair(posX+d,nextRow) in board.piecesPositions.keys && board.piecesPositions[Pair(posX+d,nextRow)]!!.color!=color){
                moves.addLast(Move(posX,posY,posX+d,nextRow))
            }
            //En passant
            if(board.inBounds(posX+d,nextRow) && Pair(posX+d,posY) in board.piecesPositions.keys && board.piecesPositions[Pair(posX+d,posY)]!!.color!=color &&
                board.piecesPositions[Pair(posX+d,posY)]!! is Pawn && (board.piecesPositions[Pair(posX+d,posY)]!! as Pawn).turnOfDoubleMove==board.turn-1){
                moves.addLast(Move(posX,posY,posX+d,nextRow,true))
            }
        }

        //Move one square
        if(nextRow in 0..7 && Pair(posX,nextRow) !in board.piecesPositions.keys){
            moves.addLast(Move(posX,posY,posX,nextRow,attacking = false))
        }

        //Move two squares
        nextRow = if (color==0) posY-2 else posY+2
        if(!moved && nextRow in 0..7 && Pair(posX,nextRow) !in board.piecesPositions.keys){
            moves.addLast(Move(posX,posY,posX,nextRow, attacking = false))
        }

        // TODO implement en passant
       /* for (move in potentialMoves){


            if (x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.piecesPositions.keys && board.piecesPositions[Pair(x,y)]!!.color==color)){
                continue
            }
            moves.addLast(Pair(x,y))

        }
        moves.removeIf{
            val x = it.endX
            val y = it.endY
            x<0 || x>7 || y<0 || y>7 || (Pair(x,y) in board.piecesPositions.keys && board.piecesPositions[Pair(x,y)]!!.color==color)

        }*/
        return moves
    }

    fun getPotentialAttacks(board : Board) : List<Move> {
        val moves:MutableList<Move> = mutableListOf()
        val nextRow = if (color==0) posY-1 else posY+1
        for (d in listOf(-1,1)){
            //Attacking on diagonal
            if (board.inBounds(posX+d,nextRow) && Pair(posX+d,nextRow) !in board.piecesPositions.keys){
                moves.addLast(Move(posX,posY,posX+d,nextRow))
            }

        }
        return moves
    }

    override fun move(endX : Int,endY : Int, board : Board){
        moved = true
        //madeDoubleMove = abs(endY-posY)==2
        if(abs(endY-posY)==2){
            turnOfDoubleMove = board.turn
        }
        posX=endX
        posY=endY
    }
}