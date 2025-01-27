package compose.project.chesstwo

import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Pawn
import kotlin.math.max

class Computer(val color: PlayerColor) {

    private fun genAllMovesForColor(board: Board, color: PlayerColor): List<Move> {
        val pieces = board.piecesPositions.toMap()
        val moves = mutableListOf<Move>()
        pieces.forEach { (_, piece) ->
            if (piece.color == color) {
                moves.addAll(piece.getMoves(board))
            }
        }
        return moves
    }

    private fun calculateDepth(numMoves: Int, numPieces: Int): Int {
        if (numMoves > 15) return 3
        if (numMoves > 7) return 4
        if (numPieces > 4) return 5
        return -numMoves + 12
    }

    // Calculates how good a move is
    private fun getMovePriority(move: Move, board: Board): Int {
        var score = 0
        val attackingPiece = board.piecesPositions[Pair(move.startX, move.startY)]!!
        if (Pair(move.endX, move.endY) in board.piecesPositions.keys) {
            score += 10 * board.piecesPositions[Pair(
                move.endX,
                move.endY
            )]!!.value - 10 * attackingPiece.value
        }
        if (attackingPiece is Pawn &&
            (attackingPiece.color == PlayerColor.WHITE && attackingPiece.posY == 1) ||
            (attackingPiece.color == PlayerColor.BLACK && attackingPiece.posY == board.size - 2)
        ) {
            score += 9
        }
        if (Pair(move.endX, move.endY) in board.getAllPseudoAttackedSquares(
                PlayerColor.negateColor(
                    PlayerColor.turnToColor(board.turn)
                )
            )
        ) {
            score -= attackingPiece.value
        }
        return score
    }

    // Sorts available moves based on how good they are
    private fun getOrderedMoves(board: Board): List<Move> {
        val unorderedMoves = genAllMovesForColor(board, color)
        var movesToSort = mutableListOf<Pair<Int, Move>>()
        unorderedMoves.forEach { move ->
            movesToSort.add(Pair(getMovePriority(move, board), move))
        }
        movesToSort = movesToSort.sortedBy { it.first }.toMutableList()
        val moves = mutableListOf<Move>()
        movesToSort.forEach {
            moves.add(it.second)
        }
        return moves
    }

    fun makeMove(board: Board): Move? {
        val moves = getOrderedMoves(board)
        if (moves.isEmpty()) return null
        val searchBoard = SearchBoard(board)
        var bestEval = -1000
        var bestMove = moves[0]
        val depth = calculateDepth(moves.size, board.piecesPositions.size)
        moves.shuffled().forEach { move ->
            searchBoard.move(move)
            val eval = -searchRec(searchBoard, depth, -1000, 1000)
            if (eval > bestEval) {
                bestEval = eval
                bestMove = move
            }
            searchBoard.unmakeMove()
        }
        return bestMove
    }

    private fun countMaterial(board: Board, color: PlayerColor): Int {
        var sum = 0
        board.piecesPositions.forEach { (_, piece) ->
            if (piece.color == color && piece !is King) {
                sum += piece.value
            }
        }
        return sum
    }

    private fun evaluate(board: Board): Int {
        var evaluation =
            countMaterial(board, PlayerColor.WHITE) - countMaterial(board, PlayerColor.BLACK)

        // Reward for putting the opposing king in check
        if (board.inCheck(
                PlayerColor.negateColor(
                    PlayerColor.turnToColor(board.turn)
                )
            )
        ) {
            evaluation += 3
        }
        if (board.turn % 2 == 1) {
            evaluation *= -1
        }
        return evaluation
    }

    private fun searchRec(board: SearchBoard, depth: Int, alpha: Int, beta: Int): Int {
        if (depth == 0) {
            return evaluate(board)
        }
        var newAlpha = alpha
        val moves = genAllMovesForColor(board, PlayerColor.turnToColor(board.turn))
        // Found check mate
        if (moves.isEmpty()) {
            if (board.inCheck(PlayerColor.turnToColor(board.turn))) {
                return -1000
            }
            return 0
        }
        // Checking all of the possible moves
        for (move in moves) {
            board.move(move)
            val evaluation = -searchRec(board, depth - 1, -beta, -newAlpha)
            board.unmakeMove()
            if (evaluation >= beta) {
                return beta
            }
            newAlpha = max(alpha, evaluation)
        }
        return newAlpha
    }
}