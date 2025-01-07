package compose.project.chesstwo

class Computer(val color : Int) {
    fun makeMove(board : Board) : Move{
        val moves = mutableListOf<Move>()
        board.piecesPositions.forEach{(_,piece)->
            if(piece.color==color){
                moves.addAll(piece.getMoves(board))
            }
        }
        return moves.random()
    }
}