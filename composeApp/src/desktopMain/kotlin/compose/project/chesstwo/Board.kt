package compose.project.chesstwo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import  chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.kingB
import chesstwo.composeapp.generated.resources.kingW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.pawnW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import compose.project.chesstwo.pieces.Bishop
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Knight
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Rook
import org.jetbrains.compose.resources.DrawableResource
import java.lang.Thread.sleep

class Board {
    public val piecesPositions = mutableMapOf<Pair<Int,Int>, Piece>()
    public var turn = 0
    public var possibleMoves = mutableListOf<Move>()
    public val allAttackedSquares = listOf<MutableList<Pair<Int,Int>>>(mutableListOf(), mutableListOf())
    public var promotionChoice = false
    public var promotingPawn : Piece? = null
    public var gameEnded = false
    public var gameResult : Int = 0
    public fun setupPieces(){
        //piecesPositions[Pair(0,0)] = Rook(0,0, Res.drawable.rookB,1)
        //piecesPositions[Pair(1,0)] = Knight(1,0, Res.drawable.knightB,1)
        //piecesPositions[Pair(2,0)] = Bishop(2,0, Res.drawable.bishopB,1)
        //piecesPositions[Pair(3,0)] = Queen(3,0, Res.drawable.queenB,1)
        piecesPositions[Pair(4,0)] = King(4,0, Res.drawable.kingB,1)
        //piecesPositions[Pair(5,0)] = Bishop(5,0, Res.drawable.bishopB,1)
        //piecesPositions[Pair(6,0)] = Knight(6,0, Res.drawable.knightB,1)
        //piecesPositions[Pair(7,0)] = Rook(7,0, Res.drawable.rookB,1)

        piecesPositions[Pair(0,7)] = Rook(0,7, Res.drawable.rookW,0)
        piecesPositions[Pair(1,7)] = Knight(1,7, Res.drawable.knightW,0)
        piecesPositions[Pair(2,7)] = Bishop(2,7, Res.drawable.bishopW,0)
        piecesPositions[Pair(3,7)] = Queen(3,7, Res.drawable.queenW,0)
        piecesPositions[Pair(4,7)] = King(4,7, Res.drawable.kingW,0)
        piecesPositions[Pair(5,7)] = Bishop(5,7, Res.drawable.bishopW,0)
        piecesPositions[Pair(6,7)] = Knight(6,7, Res.drawable.knightW,0)
        piecesPositions[Pair(7,7)] = Rook(7,7, Res.drawable.rookW,0)

        for (i in 0 until 8){
            //piecesPositions[Pair(i,1)] = Pawn(i,1, Res.drawable.pawnB,1)
            piecesPositions[Pair(i,3)] = Pawn(i,3, Res.drawable.pawnW,0)//6
        }

    }

    fun getAllAttackedSquares2(color : Int) : MutableList<Pair<Int,Int>>{ //Attacked by color
        var ret = mutableListOf<Pair<Int,Int>>()
        piecesPositions.forEach { (pos, piece) ->
            if(piece.color==color) {
                piece.getPseudoMoves(this).forEach {
                    /*if(it.isEnPassant){
                        allAttackedSquares[piece.color].addLast(Pair())
                    }*/
                    if (it.attacking) {
                        ret.addLast(Pair(it.endX, it.endY))
                    }

                }
                if (piece is Pawn) {
                    piece.getPotentialAttacks(this).forEach {
                        ret.addLast(Pair(it.endX, it.endY))
                    }
                }
            }
        }
        return ret
    }

    fun getAllAttackedSquares(){
        allAttackedSquares.forEach{it.clear()}
        piecesPositions.forEach { (pos, piece) ->
            piece.getMoves(this).forEach {
                /*if(it.isEnPassant){
                    allAttackedSquares[piece.color].addLast(Pair())
                }*/
                if(it.attacking){
                    allAttackedSquares[piece.color].addLast(Pair(it.endX,it.endY))
                }

            }
            if (piece is Pawn){
                piece.getPotentialAttacks(this).forEach{
                    allAttackedSquares[piece.color].addLast(Pair(it.endX,it.endY))
                }
            }
        }
    }

    public fun move(startX : Int, startY : Int, endX : Int, endY : Int){

        var playedMove : Move = possibleMoves[0]
        for (m in possibleMoves){
            if(m.endX==endX && m.endY==endY){
                playedMove = m
                break
            }
        }

        if(playedMove.isEnPassant){ //En passant
            piecesPositions.remove(Pair(endX,startY))
        }
        if(playedMove.isCastling){
            val rookEndX = (endX+startX)/2
            val rookStartX = if (endX < startX) 0 else 7
            piecesPositions[Pair(rookEndX,endY)] = piecesPositions[Pair(rookStartX,endY)]!!
            piecesPositions.remove(Pair(rookStartX,endY))
            piecesPositions[Pair(rookEndX,endY)]!!.move(rookEndX,endY,this)
        }
        piecesPositions[Pair(endX,endY)] = piecesPositions[Pair(startX,startY)]!!
        piecesPositions.remove(Pair(startX,startY))



        //if(piecesPositions)

        piecesPositions[Pair(endX,endY)]!!.move(endX,endY,this)

        if(isPawnOnLastRank(piecesPositions[Pair(endX,endY)]!!)){
            promotingPawn = piecesPositions[Pair(endX,endY)]!!
            promotionChoice = true

        }

        //getAllAttackedSquares()
        else{
            turn += 1

            if(isCheckMate(turn%2)){
                if(inCheck(turn%2)){
                    println("Szach-mat: Wygrał $turn")
                    gameEnded = true
                    gameResult = (turn+1)%2
                }
                else{
                    gameEnded = true
                    gameResult = 2
                }

            }
        }


    }
    public fun finishPromotionMove(texture : DrawableResource){
        val pawn = promotingPawn!!
        val endX  = pawn.posX
        val endY = pawn.posY
        //val pawn = promotingPawn
        //piecesPositions[Pair(endX,endY)] = Queen(pawn.posX,pawn.posY,if (pawn.color==0) Res.drawable.queenW else Res.drawable.queenB,pawn.color)
        val piece : Piece = when(texture){
            Res.drawable.queenW, Res.drawable.queenB -> Queen(endX,endY,texture,pawn.color)
            Res.drawable.rookW, Res.drawable.rookB -> Rook(endX,endY,texture,pawn.color)
            Res.drawable.bishopW, Res.drawable.bishopB -> Bishop(endX,endY,texture,pawn.color)
            else -> Knight(endX,endY,texture,pawn.color)
        }
        promotionChoice = false
        piecesPositions[Pair(endX,endY)] = piece
        turn += 1

        if(isCheckMate(turn%2)){
            if(inCheck(turn%2)){
                println("Szach-mat: Wygrał $turn")
                gameEnded = true
                gameResult = (turn+1)%2
            }
            else{
                gameEnded = true
                gameResult = 2
            }

        }
    }
    /*public fun addNewMoves(attackedSquares : List<Pair<Int,Int>>, startX : Int, startY : Int){
        possibleMoves.clear()
        attackedSquares.forEach {
            possibleMoves.addLast(Move(startX,startY,it.first,it.second))
        }
    }*/
    fun getAttackedSquares() : List<Pair<Int,Int>>{
        val ret = mutableListOf<Pair<Int,Int>>()
        possibleMoves.forEach{
            ret.addLast(Pair(it.endX,it.endY))
        }
        return ret
    }
    fun inBounds(x:Int,y:Int):Boolean{
        return x in 0..7 && y in 0..7
    }

    public fun inCheck(color : Int) : Boolean{
        //getAllAttackedSquares()
        getAllAttackedSquares2(1-color).forEach{
            if(it in piecesPositions.keys && piecesPositions[it] is King && piecesPositions[it]!!.color==color){
                return true
            }
        }
        return false
    }

    public fun isCheckMate(color : Int) : Boolean{
        println("--------------")
        val pieces = piecesPositions.values.toList()
        for(piece in pieces) {

            if(piece.color==color && piece.getMoves(this).isNotEmpty()){
                return false

            }
        }
        println("MAT")
        return true
    }
    private fun isPawnOnLastRank(piece : Piece) : Boolean{
        if(piece !is Pawn){
            return false
        }
        if((piece.color==0 && piece.posY==0) || (piece.color==1 && piece.posY==7)){
            return true
        }
        return false
    }

}