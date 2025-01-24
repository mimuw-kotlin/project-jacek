package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

class King(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int,
    private var moved: Boolean = false
) : Piece {
    override val value = Int.MAX_VALUE
    override fun getPseudoMoves(board: Board): List<Move> {
        //All possible knight moves
        val allMoves = listOf(
            Pair(0, 1), Pair(0, -1), Pair(1, 0), Pair(-1, 0),
            Pair(1, 1), Pair(1, -1), Pair(-1, 1), Pair(-1, -1)
        )
        val moves: MutableList<Move> = mutableListOf()
        for (move in allMoves) {
            val x = posX + move.first
            val y = posY + move.second

            //Square occupied by same color piece
            if (x < 0 || x >=board.size || y < 0 || y >= board.size ||
                (Pair(x, y) in board.piecesPositions.keys
                        && board.piecesPositions[Pair(x, y)]!!.color == color)
            ) {
                continue
            }
            moves.add(Move(posX, posY, x, y))

        }
        moves.removeIf{
            Pair(it.endX,it.endY) in board.piecesPositions.keys && !ableToCapture(board,it.endX,it.endY)
        }
        if (!moved) {
            //Long castle
            if (Pair(0, posY) in board.piecesPositions.keys
                && board.piecesPositions[Pair(0, posY)] is Rook
                && !(board.piecesPositions[Pair(0, posY)] as Rook).moved
                && isLineFree(board, false) && isLineNotAttacked(board, false)
            ) {

                moves.add(Move(posX, posY, posX-2, posY, isCastling = true, attacking = false))
            }

            //Short castle
            if (Pair(board.size-1, posY) in board.piecesPositions.keys
                && board.piecesPositions[Pair(board.size-1, posY)] is Rook
                && !(board.piecesPositions[Pair(board.size-1, posY)] as Rook).moved
                && isLineFree(board) && isLineNotAttacked(board)
            ) {

                moves.add(Move(posX, posY, posX+2, posY, isCastling = true, attacking = false))
            }
        }

        return moves
    }

    override fun move(endX: Int, endY: Int, board: Board) {
        super.move(endX, endY, board)
        moved = true
    }

    //Check if the space between king and rook is empty
    private fun isLineFree(board: Board, shortCastle: Boolean = true): Boolean {
        if (shortCastle) {
            //return Pair(5, posY) !in board.piecesPositions.keys
                 //   && Pair(6, posY) !in board.piecesPositions.keys
            for(x in posX+1..<board.size-1){
                if(Pair(x,posY) in board.piecesPositions.keys) return false
            }
            return true
        }
        //return Pair(1, posY) !in board.piecesPositions.keys
        //        && Pair(2, posY) !in board.piecesPositions.keys
        //        && Pair(3, posY) !in board.piecesPositions.keys
        for(x in 1..<posX){
            if(Pair(x,posY) in board.piecesPositions.keys) return false
        }
        return true
    }

    //Check if the squares that king passes during castling are not attacked
    private fun isLineNotAttacked(board: Board, shortCastle: Boolean = true): Boolean {
        if (shortCastle) {
            /*for (x in 4..6) {
                if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                    return false
                }
            }
            return true*/
            for (x in posX..posX+2){
                if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                    return false
                }
            }
            return true
        }
        /*for (x in 2..4) {
            if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                return false
            }
        }
        return true*/
        for (x in posX-2..posX){
            if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                return false
            }
        }
        return true
    }
    override fun copy() : Piece{
        return King(posX,posY,texture,color,moved)
    }
}