package compose.project.chesstwo.pieces

import org.jetbrains.compose.resources.DrawableResource

interface Piece {
    public var posX : Int
    public var posY : Int
    public var texture : DrawableResource
    public var color : Int
    public fun getMoves(board : MutableMap<Pair<Int,Int>,Piece>) : List<Pair<Int,Int>>
}