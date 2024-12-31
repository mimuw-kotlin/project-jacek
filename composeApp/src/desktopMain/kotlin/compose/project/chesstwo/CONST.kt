package compose.project.chesstwo

import androidx.compose.ui.graphics.Color
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.pawnB
import chesstwo.composeapp.generated.resources.pawnW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW

val lightSquareColor = Color(0xffe0e0e0)
val darkSquareColor = Color(0xffc7a25a)

val fogLightSquareColor = Color(0xffa0a0a0)
val fogDarkSquareColor = Color(0xff87621a)

val textures = listOf(
    listOf(
        Res.drawable.queenW, Res.drawable.rookW,
        Res.drawable.bishopW, Res.drawable.knightW
    ),
    listOf(
        Res.drawable.queenB, Res.drawable.rookB,
        Res.drawable.bishopB, Res.drawable.knightB
    )
)

val allTextures = listOf(
    listOf(
        Res.drawable.queenW, Res.drawable.rookW,
        Res.drawable.bishopW, Res.drawable.knightW,
        Res.drawable.pawnW
    ),
    listOf(
        Res.drawable.queenB, Res.drawable.rookB,
        Res.drawable.bishopB, Res.drawable.knightB,
        Res.drawable.pawnB
    )
)