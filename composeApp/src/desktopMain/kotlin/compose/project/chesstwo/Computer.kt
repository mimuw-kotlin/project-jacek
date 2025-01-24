package compose.project.chesstwo

import compose.project.chesstwo.pieces.Pawn
import kotlin.math.max
import kotlin.random.Random

class Computer(val color : Int) {

    private fun genAllMovesForColor(board : Board, color : Int) : List<Move>{
        val pieces = board.piecesPositions.toMap()
        val moves = mutableListOf<Move>()
        pieces.forEach{(_,piece)->
            if(piece.color==color){
                moves.addAll(piece.getMoves(board))
            }
        }
        return moves
    }
    private fun calculateDepth(numMoves : Int, numPieces : Int) : Int{
        //if(numPieces<5) return 5
        if(numMoves>15) return 3
        if(numMoves>7) return 4
        if(numPieces>4) return 4
        return -numMoves + 12
    }

    private fun getMovePriority(move : Move,board : Board) : Int{
        var score = 0
        val attackingPiece = board.piecesPositions[Pair(move.startX,move.startY)]!!
        if(Pair(move.endX,move.endY) in board.piecesPositions.keys){
            score += 10*board.piecesPositions[Pair(move.endX,move.endY)]!!.value - 10*attackingPiece.value
        }
        if(attackingPiece is Pawn &&
            (attackingPiece.color == 0 && attackingPiece.posY == 1) ||
            (attackingPiece.color == 1 && attackingPiece.posY == board.size-2)){
            score+=9
        }
        if(Pair(move.endX,move.endY) in board.getAllPseudoAttackedSquares(1-board.turn%2)){
            score -= attackingPiece.value
        }
        return score
    }

    private fun getOrderedMoves(board : Board) : List<Move>{
        val unorderedMoves = genAllMovesForColor(board,color)
        var movesToSort = mutableListOf<Pair<Int,Move>>()
        unorderedMoves.forEach{move->
            movesToSort.add(Pair(getMovePriority(move,board),move))
        }
        movesToSort = movesToSort.sortedBy { it.first }.toMutableList()
        val moves = mutableListOf<Move>()
        movesToSort.forEach{
            moves.addLast(it.second)
        }
        return moves
    }

    fun makeMove(board : Board) : Move?{
        val moves = getOrderedMoves(board)
        if(moves.isEmpty()) return null
        val searchBoard = SearchBoard(board)
        var bestEval = -1000
        var bestMove = moves[0]
        val depth = calculateDepth(moves.size,board.piecesPositions.size)
        moves.forEach{move->
            searchBoard.move(move)
            val eval = -searchRec(searchBoard,depth,-1000,1000)
            if(eval>bestEval){
                bestEval = eval
                bestMove = move
            }
            searchBoard.unmakeMove()
        }
        return bestMove
    }
    private fun countMaterial(board: Board, color : Int) : Int{
        var sum = 0
        board.piecesPositions.forEach{(_,piece)->
            if(piece.color==color){
                sum += piece.value
            }
        }
        return sum
    }
    fun evaluate(board: Board) : Int{
        var evaluation = countMaterial(board,0)-countMaterial(board,1)
        if(board.inCheck(1-(board.turn%2))){
            evaluation+=3
        }
        if (board.turn%2==1){
            evaluation *= -1
        }
        return evaluation
    }
    fun search(board: Board, depth : Int, alpha : Int, beta : Int) : Int{
        return searchRec(SearchBoard(board),depth,alpha,beta)
    }
    private fun searchRec(board: SearchBoard, depth : Int, alpha : Int, beta : Int) : Int{
        if(depth==0){
            return evaluate(board)
        }
        var newAlpha = alpha
        val moves = genAllMovesForColor(board,board.turn%2)
        if(moves.isEmpty()){
            if(board.inCheck(board.turn%2)){
                return -1000
            }
            return 0
        }
        for (move in moves){
            board.move(move)
            val evaluation = -searchRec(board,depth-1,-beta,-newAlpha)
            board.unmakeMove()
            if(evaluation >= beta){
                return beta
            }
            newAlpha = max(alpha,evaluation)
        }
        return newAlpha
    }
}