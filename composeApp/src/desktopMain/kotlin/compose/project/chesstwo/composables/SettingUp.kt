package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
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
fun SideButton(choosingColor: Int, setChoosingColor: (Int)->Unit,width: Int) {
    //var choosingColor by remember { mutableStateOf(0) }

    val buttonColor = if (choosingColor == 1) Color.White else Color.Black
    val textColor = if (choosingColor == 1) Color.Black else Color.White

    Button(
        onClick = {
            setChoosingColor(1-choosingColor)
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        modifier = Modifier.padding(16.dp).width(width.dp)
    ) {
        Text(
            text = if (choosingColor == 0) "Black Side" else "White Side",
            color = textColor,
            style = MaterialTheme.typography.h6
        )
    }
}

@Composable
fun SettingUpButton(
    onClick: () -> Unit, text: String,
    textColor: Color, buttonColor: ButtonColors,
    width: Int
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(vertical = 20.dp)
            .width(width.dp),
        colors = buttonColor
    ) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun PieceSelection(textures : List<DrawableResource>, selectPiece : (DrawableResource) -> Unit){
    Column(modifier = Modifier.wrapContentSize(Alignment.TopCenter)){
        for (t in textures) {
            Image(painter = painterResource(t),
                contentDescription = null,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .clickable {
                        selectPiece(t)
                    }
                    .width(70.dp)
                    .height(70.dp))
        }
    }
}