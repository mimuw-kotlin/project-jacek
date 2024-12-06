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
    override fun getPseudoMoves(board: Board): List<Move> {
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
                isLineFree(board,false) && isLineNotAttacked(board,false)){
                moves.addLast(Move(posX,posY,2,posY, isCastling = true, attacking = false))
            }
            if(Pair(7,posY) in board.piecesPositions.keys && board.piecesPositions[Pair(7,posY)] is Rook && !(board.piecesPositions[Pair(7,posY)] as Rook).moved &&
                isLineFree(board) && isLineNotAttacked(board)){
                moves.addLast(Move(posX,posY,6,posY, isCastling = true, attacking = false))
            }
        }

       /* moves.removeIf {
            //Pair(it.endX,it.endY) in board.allAttackedSquares[1-color]
            inCheckAfterMove(board,it)
        }*/

        return moves
    }
    override fun move(endX: Int, endY: Int, board: Board) {
        super.move(endX, endY, board)
        moved=true
    }
    private fun isLineFree(board : Board, shortCastle: Boolean = true):Boolean{
        if(shortCastle){
            return Pair(5,posY) !in board.piecesPositions.keys && Pair(6,posY) !in board.piecesPositions.keys
        }
        return Pair(1,posY) !in board.piecesPositions.keys && Pair(2,posY) !in board.piecesPositions.keys && Pair(3,posY) !in board.piecesPositions.keys
    }
    private fun isLineNotAttacked(board : Board, shortCastle: Boolean = true):Boolean{
        if(shortCastle){
            for (x in 4..6){
                if (Pair(x,posY) in board.allAttackedSquares[1-color]){
                    return false
                }
            }
            return true
        }
        for (x in 2..4){
            if (Pair(x,posY) in board.allAttackedSquares[1-color]){
                return false
            }
        }
        return true
    }
    public fun inCheckAfterMove(board : Board, move : Move) : Boolean{
        val copiedList = board.allAttackedSquares.map { it.toMutableList() }
        var startPiece : Piece = board.piecesPositions[Pair(move.startX,move.startY)]!!
        var endPiece :Piece? = board.piecesPositions[Pair(move.endX,move.endY)]
        println(startPiece)
        println(endPiece)

        board.piecesPositions[Pair(move.endX,move.endY)]=startPiece
        println(1)
        board.piecesPositions.remove(Pair(move.startX,move.startY))
        println(2)
        val ret : Boolean = board.inCheck(board.turn%2)
        println(ret)
        board.piecesPositions[Pair(move.startX,move.startY)] = startPiece
        println(3)
        if (endPiece != null) {
            board.piecesPositions.replace(Pair(move.endX,move.endY),endPiece)
        }
        else {
            board.piecesPositions.remove(Pair(move.endX,move.endY))
        }
        board.piecesPositions.forEach{
            println(it)
        }

        return ret
    }
}