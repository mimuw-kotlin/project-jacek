package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

interface Piece {
    public var posX : Int
    public var posY : Int
    public var texture : DrawableResource
    public var color : Int
    //public fun getMoves(board : Board) : List<Move>
    public fun getPseudoMoves(board : Board) : List<Move>
    public fun move(endX : Int,endY : Int, board : Board){
        posX=endX
        posY=endY
    }
    fun getMoves(board: Board): List<Move> {
        var pseudoMoves = getPseudoMoves(board)
        var moves = mutableListOf<Move>()
        for ( move in pseudoMoves){
            var startPiece : Piece = board.piecesPositions[Pair(move.startX,move.startY)]!!
            var endPiece :Piece? = board.piecesPositions[Pair(move.endX,move.endY)]
            board.piecesPositions[Pair(move.endX,move.endY)]=startPiece
            board.piecesPositions.remove(Pair(move.startX,move.startY))
            var found = false
            for (square in board.getAllAttackedSquares2(1-color)){
                if(square in board.piecesPositions.keys && board.piecesPositions[square] is King && board.piecesPositions[square]!!.color==color){
                    found=true
                    break
                }
            }
            if(!found){
                moves.addLast(move)

            }
            board.piecesPositions[Pair(move.startX,move.startY)] = startPiece
            if (endPiece != null) {
                board.piecesPositions.replace(Pair(move.endX,move.endY),endPiece)
            }
            else {
                board.piecesPositions.remove(Pair(move.endX,move.endY))
            }
        }
        return moves
    }

}