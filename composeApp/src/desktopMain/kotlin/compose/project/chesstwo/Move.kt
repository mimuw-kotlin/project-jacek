package compose.project.chesstwo

data class Move(
    // Piece's starting position
    val startX: Int,
    val startY: Int,

    //Piece's ending position
    val endX: Int,
    val endY: Int,

    val isEnPassant: Boolean = false,
    val isCastling: Boolean = false,

    // Is the piece attacking a square
    // (The only exception is pawn pushing forward)
    val attacking: Boolean = true
)