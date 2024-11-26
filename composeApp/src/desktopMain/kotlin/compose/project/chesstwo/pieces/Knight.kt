package compose.project.chesstwo.pieces

import org.jetbrains.compose.resources.DrawableResource

class Knight(
    override var posX: Int,
    override var posY: Int,
    override var texture: DrawableResource,
    override var color: Int
) : Piece {
}