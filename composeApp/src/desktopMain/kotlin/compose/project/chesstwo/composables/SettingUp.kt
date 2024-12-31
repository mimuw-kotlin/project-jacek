package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res
import compose.project.chesstwo.Board
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource

@Composable
fun SideButton(choosingColor: Int, setChoosingColor: (Int)->Unit) {
    //var choosingColor by remember { mutableStateOf(0) }

    val buttonColor = if (choosingColor == 1) Color.White else Color.Black
    val textColor = if (choosingColor == 1) Color.Black else Color.White

    Button(
        onClick = {
            //choosingColor = 1 - choosingColor
            setChoosingColor(1-choosingColor)
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = if (choosingColor == 0) "Black Side" else "White Side",
            color = textColor
        )
    }
}

@Composable
fun PieceSelection(textures : List<DrawableResource>, selectPiece : (DrawableResource) -> Unit){
    Column{
        for (t in textures) {
            Image(painter = painterResource(t),
                contentDescription = null,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .clickable {
                        selectPiece(t)
                    })
        }
    }
}