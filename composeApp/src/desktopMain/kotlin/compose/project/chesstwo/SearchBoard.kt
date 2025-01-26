package compose.project.chesstwo

import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import compose.project.chesstwo.pieces.Necromancer
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Shield

data class BoardState(val positions : MutableMap<Pair<Int,Int>,Piece>, val lastCapturedPiece : List<ArrayDeque<Piece>>, val lastActiveMove : Int)

class SearchBoard(board : Board) : Board() {
    val boardStates = ArrayDeque<BoardState>()
    init{

        //piecesPositions=board.piecesPositions.toMutableMap()
        piecesPositions = copyPositions(board.piecesPositions)

        lastCapturedPiece = copyLastCapturedPiece(board.lastCapturedPiece)
        turn = board.turn
        //possibleMoves = board.possibleMoves

        lastActiveMove = board.lastActiveMove


    }

    private fun copyPositions(src : MutableMap<Pair<Int,Int>,Piece>) : MutableMap<Pair<Int,Int>,Piece>{
        val dst = mutableMapOf<Pair<Int, Int>, Piece>()
        src.forEach{(pos, piece)->
            dst[pos] = piece.copy()
        }
        return dst
    }
    private fun copyLastCapturedPiece(src : List<ArrayDeque<Piece>>) : List<ArrayDeque<Piece>>{
        val dst = listOf(ArrayDeque<Piece>(),ArrayDeque<Piece>())
        for (i in 0..1){
            src[i].forEach{
                dst[i].add(it.copy())
            }
            //lastCapturedPiece[i].addAll(board.lastCapturedPiece[i])
        }
        return dst
    }

    private var oldPiecesPositions = mutableMapOf<Pair<Int, Int>, Piece>()
    private var oldLastCapturedPiece = listOf(ArrayDeque<Piece>(),ArrayDeque<Piece>())
    private var oldLastActiveMove = 0
    private fun saveParameters(){
        //oldPiecesPositions = piecesPositions.toMutableMap()
        oldPiecesPositions = copyPositions(piecesPositions)
        /*for (i in 0..1){
            oldLastCapturedPiece[i].addAll(lastCapturedPiece[i])
        }*/
        oldLastCapturedPiece = copyLastCapturedPiece(lastCapturedPiece)
        oldLastActiveMove = lastActiveMove
        boardStates.add(BoardState(oldPiecesPositions,oldLastCapturedPiece,oldLastActiveMove))
    }
    fun unmakeMove(){
        //piecesPositions = oldPiecesPositions.toMutableMap()
        val state = boardStates.removeLast()


        piecesPositions = copyPositions(state.positions)
        /*for(i in 0..1){
            lastCapturedPiece[i].addAll(lastCapturedPiece[i])
        }*/
        lastCapturedPiece = copyLastCapturedPiece(state.lastCapturedPiece)
        turn-=1
        lastActiveMove = state.lastActiveMove
        //println("Po unmake: ${piecesPositions[Pair(6,0)]}")
    }
    fun move(move : Move) {
        saveParameters()

        val startX = move.startX
        val startY = move.startY
        val endX = move.endX
        val endY = move.endY
        //println("${startX}, $startY, $endX, $endY")


        if (piecesPositions[Pair(startX, startY)] is Pawn) {
            lastActiveMove = turn
        }

        if (move.isEnPassant) {
            val captured = piecesPositions[Pair(endX,startY)]!!
            lastCapturedPiece[captured.color.value].addFirst(captured)
            piecesPositions.remove(Pair(endX, startY))
            lastActiveMove = turn
        } else if (Pair(endX, endY) in piecesPositions.keys) {
            val captured = piecesPositions[Pair(endX,endY)]!!
            lastCapturedPiece[captured.color.value].addFirst(captured)
            lastActiveMove = turn
        }

        if (move.isCastling) {
            val rookEndX = (endX + startX) / 2
            val rookStartX = if (endX < startX) 0 else size-1

            //Handling rook movement during castling
            piecesPositions[Pair(rookEndX, endY)] = piecesPositions[Pair(rookStartX, endY)]!!
            piecesPositions.remove(Pair(rookStartX, endY))
            piecesPositions[Pair(rookEndX, endY)]!!.move(rookEndX, endY, this)
        }

        if (Pair(endX,endY) in piecesPositions.keys && piecesPositions[Pair(startX,startY)] is Shield){
            (piecesPositions[Pair(startX,startY)] as Shield).attacked = true
        }

        //Moving piece to its final square
        piecesPositions[Pair(endX, endY)] = piecesPositions[Pair(startX, startY)]!!

        //Deleting it from its starting square
        piecesPositions.remove(Pair(startX, startY))

        val movedPiece = piecesPositions[Pair(endX, endY)]!!
        //Updating its information
        movedPiece.move(endX, endY, this)

        if(movedPiece is Necromancer && lastActiveMove==turn && lastCapturedPiece[movedPiece.color.value].isNotEmpty()){
            piecesPositions[Pair(endX, endY)] = lastCapturedPiece[movedPiece.color.value].first()
            lastCapturedPiece[movedPiece.color.value].removeFirst()
            piecesPositions[Pair(endX, endY)]!!.move(endX,endY,this)
        }

        //Checking for promotion
        if (isPawnOnLastRank(piecesPositions[Pair(endX, endY)]!!)) {
            piecesPositions[Pair(endX, endY)] = Queen(endX,endY, if(turn%2==0) Res.drawable.queenW else Res.drawable.queenB,
                PlayerColor.turnToColor(turn))

        }
        //Bomb explodes
        else if(move.bombAttack){
            piecesPositions.remove(Pair(endX, endY))
        }
        //Ending turn
        turn+=1
        //println("Po ruchu: ${piecesPositions[Pair(6,0)]}")
    }
}