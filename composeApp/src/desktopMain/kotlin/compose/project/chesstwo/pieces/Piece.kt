package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

interface Piece {
    var posX: Int
    var posY: Int
    var texture: DrawableResource
    var color: Int

    // Generate all pseudo-legal moves for a piece
    fun getPseudoMoves(board: Board): List<Move>

    // Update piece info after a move
    fun move(endX: Int, endY: Int, board: Board) {
        posX = endX
        posY = endY
    }

    // Generates all legal moves for a piece
    fun getMoves(board: Board): List<Move> {

        val pseudoMoves = getPseudoMoves(board)
        val moves = mutableListOf<Move>()
        for (move in pseudoMoves) {
            val startPiece: Piece = board.piecesPositions[Pair(move.startX, move.startY)]!!
            val endPiece: Piece? = board.piecesPositions[Pair(move.endX, move.endY)]

            //Move attacking piece to its destination
            board.piecesPositions[Pair(move.endX, move.endY)] = startPiece

            //Remove piece from its former position
            board.piecesPositions.remove(Pair(move.startX, move.startY))

            //Check if the move didn't put its king in check
            var found = false
            for (square in board.getAllPseudoAttackedSquares(1 - color)) {
                if (square in board.piecesPositions.keys && board.piecesPositions[square] is King
                    && board.piecesPositions[square]!!.color == color
                ) {
                    found = true
                    break
                }
            }

            // King not in check - legal move
            if (!found) {
                moves.add(move)

            }

            //Undo the move
            //Put the attacking piece on its original square
            board.piecesPositions[Pair(move.startX, move.startY)] = startPiece

            //Place the taken piece on its place if ther was one
            if (endPiece != null) {
                board.piecesPositions.replace(Pair(move.endX, move.endY), endPiece)
            }
            //Otherwise remove the attacking piece
            else {
                board.piecesPositions.remove(Pair(move.endX, move.endY))
            }
        }
        return moves
    }

}