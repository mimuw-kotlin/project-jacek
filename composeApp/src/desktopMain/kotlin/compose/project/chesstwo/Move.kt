package compose.project.chesstwo

data class Move (
    val startX : Int,
    val startY : Int,
    val endX : Int,
    val endY : Int,
    val isEnPassant : Boolean = false,
    val isCastling : Boolean = false
)