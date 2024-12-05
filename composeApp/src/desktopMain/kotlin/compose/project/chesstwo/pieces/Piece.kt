package compose.project.chesstwo.pieces

import compose.project.chesstwo.Board
import compose.project.chesstwo.Move
import org.jetbrains.compose.resources.DrawableResource

interface Piece {
    public var posX : Int
    public var posY : Int
    public var texture : DrawableResource
    public var color : Int
    public fun getMoves(board : Board) : List<Move>
    public fun move(endX : Int,endY : Int, board : Board){
        posX=endX
        posY=endY
    }

}