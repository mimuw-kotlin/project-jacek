package compose.project.chesstwo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import  chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.bombB
import chesstwo.composeapp.generated.resources.bombW
import chesstwo.composeapp.generated.resources.kingB
import chesstwo.composeapp.generated.resources.kingW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.necromancerB
import chesstwo.composeapp.generated.resources.necromancerW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.pawnW
import chesstwo.composeapp.generated.resources.portalB
import chesstwo.composeapp.generated.resources.portalW
import chesstwo.composeapp.generated.resources.princeB
import chesstwo.composeapp.generated.resources.princeW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import chesstwo.composeapp.generated.resources.shieldB
import chesstwo.composeapp.generated.resources.shieldW
import chesstwo.composeapp.generated.resources.unicornB
import chesstwo.composeapp.generated.resources.unicornW
import compose.project.chesstwo.pieces.Bishop
import compose.project.chesstwo.pieces.Bomb
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Knight
import compose.project.chesstwo.pieces.Necromancer
import compose.project.chesstwo.pieces.Pawn
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.pieces.Portal
import compose.project.chesstwo.pieces.Prince
import compose.project.chesstwo.pieces.Queen
import compose.project.chesstwo.pieces.Rook
import compose.project.chesstwo.pieces.Shield
import compose.project.chesstwo.pieces.Unicorn
import org.jetbrains.compose.resources.DrawableResource

open class Board(val size : Int = 8) {
    //Current positions of all the pieces on the board
    open var piecesPositions = mutableMapOf<Pair<Int, Int>, Piece>()

    var turn = 0

    //All possible moves for the selected piece
    var possibleMoves = mutableListOf<Move>()

    //A need to choice a piece for a pawn to be promoted to
    //var promotionChoice = false
    var promotionChoice by mutableStateOf(false)

    //A pawn to be promoted
    private var promotingPawn: Piece? = null
    var gameEnded = false
    var gameResult: GameResult = GameResult.WHITEWIN
    var lastActiveMove = 0
    var lastCapturedPiece = listOf(ArrayDeque<Piece>(),ArrayDeque<Piece>())

   /* fun copy(): Board{
        val newBoard = Board(size)
        newBoard.piecesPositions=piecesPositions
        newBoard.turn = turn
        newBoard
    }*/

    //Initial positioning of the pieces
    /*
    fun setupPieces() {
        piecesPositions[Pair(0, 0)] = Rook(0, 0, Res.drawable.rookB, 1)
        piecesPositions[Pair(1, 0)] = Knight(1, 0, Res.drawable.knightB, 1)
        piecesPositions[Pair(2, 0)] = Bishop(2, 0, Res.drawable.bishopB, 1)
        piecesPositions[Pair(3, 0)] = Queen(3, 0, Res.drawable.queenB, 1)
        piecesPositions[Pair(4, 0)] = King(4, 0, Res.drawable.kingB, 1)
        piecesPositions[Pair(5, 0)] = Bishop(5, 0, Res.drawable.bishopB, 1)
        piecesPositions[Pair(6, 0)] = Knight(6, 0, Res.drawable.knightB, 1)
        piecesPositions[Pair(7, 0)] = Rook(7, 0, Res.drawable.rookB, 1)

        piecesPositions[Pair(0, 7)] = Rook(0, 7, Res.drawable.rookW, 0)
        piecesPositions[Pair(1, 7)] = Knight(1, 7, Res.drawable.knightW, 0)
        piecesPositions[Pair(2, 7)] = Bishop(2, 7, Res.drawable.bishopW, 0)
        piecesPositions[Pair(3, 7)] = Queen(3, 7, Res.drawable.queenW, 0)
        piecesPositions[Pair(4, 7)] = King(4, 7, Res.drawable.kingW, 0)
        piecesPositions[Pair(5, 7)] = Bishop(5, 7, Res.drawable.bishopW, 0)
        piecesPositions[Pair(6, 7)] = Knight(6, 7, Res.drawable.knightW, 0)
        piecesPositions[Pair(7, 7)] = Rook(7, 7, Res.drawable.rookW, 0)

        for (i in 0 until 8) {
            piecesPositions[Pair(i, 1)] = Pawn(i, 1, Res.drawable.pawnB, 1)
            piecesPositions[Pair(i, 6)] = Pawn(i, 6, Res.drawable.pawnW, 0)
        }

    }*/

    //Generating all squares attacked by pseudo-legal moves by 'color' player
    fun getAllPseudoAttackedSquares(color: PlayerColor): MutableList<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        piecesPositions.forEach { (_, piece) ->
            if (piece.color == color) {
                piece.getPseudoMoves(this).forEach {
                    if (it.attacking) {
                        moves.add(Pair(it.endX, it.endY))
                    }

                }
                //Check for a potential pawn diagonal attack
                if (piece is Pawn) {
                    piece.getPotentialAttacks(this).forEach {
                        moves.add(Pair(it.endX, it.endY))
                    }
                }
            }
        }
        return moves
    }

    //Function registering move on the board
    fun move(startX: Int, startY: Int, endX: Int, endY: Int, computerMove: Boolean = false) {

        //Checking for the played move
        var playedMove: Move = possibleMoves[0]
        for (m in possibleMoves) {
            if (m.endX == endX && m.endY == endY) {
                playedMove = m
                break
            }
        }

        if (piecesPositions[Pair(startX, startY)] is Pawn) {
            lastActiveMove = turn
        }

        if (playedMove.isEnPassant) {
            val captured = piecesPositions[Pair(endX,startY)]!!
            lastCapturedPiece[captured.color.value].addFirst(captured)
            piecesPositions.remove(Pair(endX, startY))
            lastActiveMove = turn
        } else if (Pair(endX, endY) in piecesPositions.keys) {
            val captured = piecesPositions[Pair(endX,endY)]!!
            lastCapturedPiece[captured.color.value].addFirst(captured)
            lastActiveMove = turn
        }

        if (playedMove.isCastling) {
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
            promotingPawn = piecesPositions[Pair(endX, endY)]!!
            if(!computerMove){
                promotionChoice = true
            }
            else{
                finishPromotionMove(if (turn%2==0)Res.drawable.queenW else Res.drawable.queenB)
            }


        }
        //Bomb explodes
        else if(playedMove.bombAttack){
            piecesPositions.remove(Pair(endX, endY))
            turn += 1
            checkForEndGame()
        }
        //Ending turn
        else {
            turn += 1
            checkForEndGame()
        }


    }

    fun textureToPiece(texture: DrawableResource, posX : Int, posY : Int, color : PlayerColor) : Piece{
        val piece: Piece = when (texture) {
            Res.drawable.queenW, Res.drawable.queenB -> Queen(posX, posY, texture, color)
            Res.drawable.rookW, Res.drawable.rookB -> Rook(posX, posY, texture, color)
            Res.drawable.bishopW, Res.drawable.bishopB -> Bishop(posX, posY, texture, color)
            Res.drawable.knightW, Res.drawable.knightB -> Knight(posX, posY, texture, color)
            Res.drawable.princeW, Res.drawable.princeB -> Prince(posX, posY, texture, color)
            Res.drawable.unicornW, Res.drawable.unicornB -> Unicorn(posX, posY, texture, color)
            Res.drawable.portalW, Res.drawable.portalB -> Portal(posX, posY, texture, color)
            Res.drawable.bombW, Res.drawable.bombB -> Bomb(posX, posY, texture, color)
            Res.drawable.shieldW, Res.drawable.shieldB -> Shield(posX, posY, texture, color)
            Res.drawable.necromancerW, Res.drawable.necromancerB -> Necromancer(posX, posY, texture, color)
            else -> Pawn(posX, posY, texture, color)
        }
        return piece
    }

    //Function to finish the turn after promotin a pawn
    fun finishPromotionMove(texture: DrawableResource) {
        val pawn = promotingPawn!!
        val endX = pawn.posX
        val endY = pawn.posY

        //Choosing new piece depending on selected texture
        val piece = textureToPiece(texture, endX,endY, pawn.color)

        promotionChoice = false
        piecesPositions[Pair(endX, endY)] = piece
        turn += 1

        checkForEndGame()
    }

    //Function checking if the game should end
    private fun checkForEndGame() {
        if (noSquaresToMove(PlayerColor.turnToColor(turn))) {
            if (inCheck(PlayerColor.turnToColor(turn))) {
                gameEnded = true
                gameResult = if ((turn + 1) % 2 == 0) GameResult.WHITEWIN else GameResult.BLACKWIN
            } else {
                gameEnded = true
                gameResult = GameResult.STALEMATE
            }

        }
        if (turn - lastActiveMove >= 150) {
            gameEnded = true
            gameResult = GameResult.DRAW
        }
    }

    //Generating all attacked squares by currently selected piece
    fun getAttackedSquares(): List<Pair<Int, Int>> {
        val ret = mutableListOf<Pair<Int, Int>>()
        possibleMoves.forEach {
            ret.add(Pair(it.endX, it.endY))
        }
        return ret
    }

    // Checking if a piece is inside of the chessboard bounds
    fun inBounds(x: Int, y: Int): Boolean {
        return x in 0..<size && y in 0..<size
    }

    // Checking if 'color' king is in check
    fun inCheck(color: PlayerColor): Boolean {

        getAllPseudoAttackedSquares(PlayerColor.negateColor(color)).forEach {
            if (it in piecesPositions.keys && piecesPositions[it] is King
                && piecesPositions[it]!!.color == color
            ) {
                return true
            }
        }
        return false
    }

    // Checking if 'color' king has no squares to move
    private fun noSquaresToMove(color: PlayerColor): Boolean {
        val pieces = piecesPositions.values.toList()
        for (piece in pieces) {
            if (piece.color == color && piece.getMoves(this).isNotEmpty()) {
                return false

            }
        }
        return true
    }


    fun isPawnOnLastRank(piece: Piece): Boolean {
        if (piece !is Pawn) {
            return false
        }
        if ((piece.color == PlayerColor.WHITE && piece.posY == 0) || (piece.color == PlayerColor.BLACK && piece.posY == size-1)) {
            return true
        }
        return false
    }

}