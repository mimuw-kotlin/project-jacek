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
            if (x < 0 || x > 7 || y < 0 || y > 7 ||
                (Pair(x, y) in board.piecesPositions.keys
                        && board.piecesPositions[Pair(x, y)]!!.color == color)
            ) {
                continue
            }
            moves.addLast(Move(posX, posY, x, y))

        }
        if (!moved) {
            //Long castle
            if (Pair(0, posY) in board.piecesPositions.keys
                && board.piecesPositions[Pair(0, posY)] is Rook
                && !(board.piecesPositions[Pair(0, posY)] as Rook).moved
                && isLineFree(board, false) && isLineNotAttacked(board, false)
            ) {

                moves.addLast(Move(posX, posY, 2, posY, isCastling = true, attacking = false))
            }

            //Short castle
            if (Pair(7, posY) in board.piecesPositions.keys
                && board.piecesPositions[Pair(7, posY)] is Rook
                && !(board.piecesPositions[Pair(7, posY)] as Rook).moved
                && isLineFree(board) && isLineNotAttacked(board)
            ) {

                moves.addLast(Move(posX, posY, 6, posY, isCastling = true, attacking = false))
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
            return Pair(5, posY) !in board.piecesPositions.keys
                    && Pair(6, posY) !in board.piecesPositions.keys
        }
        return Pair(1, posY) !in board.piecesPositions.keys
                && Pair(2, posY) !in board.piecesPositions.keys
                && Pair(3, posY) !in board.piecesPositions.keys
    }

    //Check if the squares that king passes during castling are not attacked
    private fun isLineNotAttacked(board: Board, shortCastle: Boolean = true): Boolean {
        if (shortCastle) {
            for (x in 4..6) {
                if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                    return false
                }
            }
            return true
        }
        for (x in 2..4) {
            if (Pair(x, posY) in board.getAllPseudoAttackedSquares(1 - color)) {
                return false
            }
        }
        return true
    }
}