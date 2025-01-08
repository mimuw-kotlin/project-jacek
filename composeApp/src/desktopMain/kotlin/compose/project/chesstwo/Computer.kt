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
}