package compose.project.chesstwo

import androidx.compose.ui.graphics.Color
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.allDrawableResources
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.bombB
import chesstwo.composeapp.generated.resources.bombW
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
        Res.drawable.pawnW,Res.drawable.princeW,
        Res.drawable.unicornW, Res.drawable.portalW,
        Res.drawable.bombW,Res.drawable.shieldW,
        Res.drawable.necromancerW
    ),
    listOf(
        Res.drawable.queenB, Res.drawable.rookB,
        Res.drawable.bishopB, Res.drawable.knightB,
        Res.drawable.pawnB,Res.drawable.princeB,
        Res.drawable.unicornB, Res.drawable.portalB,
        Res.drawable.bombB,Res.drawable.shieldB,
        Res.drawable.necromancerB
    )
)