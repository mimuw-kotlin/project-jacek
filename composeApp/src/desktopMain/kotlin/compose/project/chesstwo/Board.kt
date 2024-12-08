package compose.project.chesstwo

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


class Board {
    //Current positions of all the pieces on the board
    val piecesPositions = mutableMapOf<Pair<Int, Int>, Piece>()

    var turn = 0

    //All possible moves for the selected piece
    var possibleMoves = mutableListOf<Move>()

    //A need to choice a piece for a pawn to be promoted to
    var promotionChoice = false

    //A pawn to be promoted
    private var promotingPawn: Piece? = null
    var gameEnded = false
    var gameResult: GameResult = GameResult.WHITEWIN
    private var lastActiveMove = 0

    //Initial positioning of the pieces
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

    }

    //Generating all squares attacked by pseudo-legal moves by 'color' player
    fun getAllPseudoAttackedSquares(color: Int): MutableList<Pair<Int, Int>> {
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
    fun move(startX: Int, startY: Int, endX: Int, endY: Int) {

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
            piecesPositions.remove(Pair(endX, startY))
            lastActiveMove = turn
        } else if (Pair(endX, endY) in piecesPositions.keys) {
            lastActiveMove = turn
        }

        if (playedMove.isCastling) {
            val rookEndX = (endX + startX) / 2
            val rookStartX = if (endX < startX) 0 else 7

            //Handling rook movement during castling
            piecesPositions[Pair(rookEndX, endY)] = piecesPositions[Pair(rookStartX, endY)]!!
            piecesPositions.remove(Pair(rookStartX, endY))
            piecesPositions[Pair(rookEndX, endY)]!!.move(rookEndX, endY, this)
        }

        //Moving piece to its final square
        piecesPositions[Pair(endX, endY)] = piecesPositions[Pair(startX, startY)]!!

        //Deleting it from its starting square
        piecesPositions.remove(Pair(startX, startY))

        //Updating its information
        piecesPositions[Pair(endX, endY)]!!.move(endX, endY, this)

        //Checking for promotion
        if (isPawnOnLastRank(piecesPositions[Pair(endX, endY)]!!)) {
            promotingPawn = piecesPositions[Pair(endX, endY)]!!
            promotionChoice = true

        }
        //Ending turn
        else {
            turn += 1
            checkForEndGame()
        }


    }

    //Function to finish the turn after promotin a pawn
    fun finishPromotionMove(texture: DrawableResource) {
        val pawn = promotingPawn!!
        val endX = pawn.posX
        val endY = pawn.posY

        //Choosing new piece depending on selected texture
        val piece: Piece = when (texture) {
            Res.drawable.queenW, Res.drawable.queenB -> Queen(endX, endY, texture, pawn.color)
            Res.drawable.rookW, Res.drawable.rookB -> Rook(endX, endY, texture, pawn.color)
            Res.drawable.bishopW, Res.drawable.bishopB -> Bishop(endX, endY, texture, pawn.color)
            else -> Knight(endX, endY, texture, pawn.color)
        }

        promotionChoice = false
        piecesPositions[Pair(endX, endY)] = piece
        turn += 1

        checkForEndGame()
    }

    //Function checking if the game should end
    private fun checkForEndGame() {
        if (noSquaresToMove(turn % 2)) {
            if (inCheck(turn % 2)) {
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
        return x in 0..7 && y in 0..7
    }

    // Checking if 'color' king is in check
    private fun inCheck(color: Int): Boolean {

        getAllPseudoAttackedSquares(1 - color).forEach {
            if (it in piecesPositions.keys && piecesPositions[it] is King
                && piecesPositions[it]!!.color == color
            ) {
                return true
            }
        }
        return false
    }

    // Checking if 'color' king has no squares to move
    private fun noSquaresToMove(color: Int): Boolean {
        val pieces = piecesPositions.values.toList()
        for (piece in pieces) {
            if (piece.color == color && piece.getMoves(this).isNotEmpty()) {
                return false

            }
        }
        return true
    }


    private fun isPawnOnLastRank(piece: Piece): Boolean {
        if (piece !is Pawn) {
            return false
        }
        if ((piece.color == 0 && piece.posY == 0) || (piece.color == 1 && piece.posY == 7)) {
            return true
        }
        return false
    }

}