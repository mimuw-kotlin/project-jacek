package compose.project.chesstwo

class Computer(val color : Int) {
    fun makeMove(board : Board) : Move?{
        val pieces = board.piecesPositions.toMap()
        val moves = mutableListOf<Move>()
        pieces.forEach{(_,piece)->
            if(piece.color==color){
                moves.addAll(piece.getMoves(board))
            }
        }
        return moves.randomOrNull()
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
}