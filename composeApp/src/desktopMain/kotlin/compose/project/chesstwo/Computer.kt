package compose.project.chesstwo

import kotlin.math.max

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

    fun makeMove(board : Board) : Move?{
        val moves = genAllMovesForColor(board,color)
        if(moves.isEmpty()) return null
        val searchBoard = SearchBoard(board)
        var bestEval = -1001
        var bestMove = moves[0]
        moves.forEach{move->
            searchBoard.move(move)
            val eval = -searchRec(searchBoard,3,-1001,1001)
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
                return Int.MIN_VALUE
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