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
    private var moved: Boolean = false,
    private var turnOfDoubleMove : Int = -1
) : Piece {
    override fun getPseudoMoves(board: Board): List<Move> {
        val moves:MutableList<Move> = mutableListOf()

        // Row that the pawn could go into
        var nextRow = if (color==0) posY-1 else posY+1

        for (d in listOf(-1,1)){
            //Attacking on diagonal
            if (board.inBounds(posX+d,nextRow)
                && Pair(posX+d,nextRow) in board.piecesPositions.keys
                && board.piecesPositions[Pair(posX+d,nextRow)]!!.color!=color){

                moves.add(Move(posX,posY,posX+d,nextRow))
            }
            //En passant

            if(board.inBounds(posX+d,nextRow)
                && Pair(posX+d,posY) in board.piecesPositions.keys){
                val potentialEnPassantPawn = board.piecesPositions[Pair(posX+d,posY)]!!

                if(potentialEnPassantPawn.color!=color
                    && potentialEnPassantPawn is Pawn
                    && potentialEnPassantPawn.turnOfDoubleMove==board.turn-1){
                    moves.add(Move(posX,posY,posX+d,nextRow,true))
                }
            }

        }

        //Move one square
        if(nextRow in 0..7 && Pair(posX,nextRow) !in board.piecesPositions.keys){
            moves.add(Move(posX,posY,posX,nextRow,attacking = false))
        }

        //Move two squares
        nextRow = if (color==0) posY-2 else posY+2
        if(!moved && nextRow in 0..7 && Pair(posX,nextRow) !in board.piecesPositions.keys){
            moves.add(Move(posX,posY,posX,nextRow, attacking = false))
        }
        return moves
    }

    //Generate pawn attacking moves that could check the king
    fun getPotentialAttacks(board : Board) : List<Move> {
        val moves:MutableList<Move> = mutableListOf()

        // Row that the pawn could go into
        val nextRow = if (color==0) posY-1 else posY+1
        for (d in listOf(-1,1)){
            //Attacking on diagonal
            if (board.inBounds(posX+d,nextRow)
                && Pair(posX+d,nextRow) !in board.piecesPositions.keys){
                moves.add(Move(posX,posY,posX+d,nextRow))
            }

        }
        return moves
    }

    override fun move(endX : Int,endY : Int, board : Board){
        moved = true
        if(abs(endY-posY)==2){
            turnOfDoubleMove = board.turn
        }
        posX=endX
        posY=endY
    }
}