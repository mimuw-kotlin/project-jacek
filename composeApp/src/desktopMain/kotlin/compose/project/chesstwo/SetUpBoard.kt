package compose.project.chesstwo

import org.jetbrains.compose.resources.DrawableResource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
class SetUpBoard(size : Int = 8) : Board(size) {
    //override val piecesPositions = mutableMapOf<Pair<Int, Int>, Piece>()
    init{
        piecesPositions[Pair(size/2,0)]=King(size/2,0,Res.drawable.kingB,PlayerColor.BLACK)
        piecesPositions[Pair(size/2,1)]=Pawn(size/2,1,Res.drawable.pawnB,PlayerColor.BLACK)
        piecesPositions[Pair(size/2,size-1)]=King(size/2,size-1,Res.drawable.kingW,PlayerColor.WHITE)
        piecesPositions[Pair(size/2,size-2)]=Pawn(size/2,size-2,Res.drawable.pawnW,PlayerColor.WHITE)
    }
    lateinit var selectedPieceTexture : DrawableResource
    fun placePiece(posX : Int, posY : Int, color : PlayerColor){
        piecesPositions[Pair(posX,posY)] = textureToPiece(selectedPieceTexture,posX,posY,color)
    }
    fun inAvailableSpace(posY : Int, color : PlayerColor) : Boolean{
        if (color==PlayerColor.WHITE) return posY>=size/2+1
        return posY<size/2-1
    }
}