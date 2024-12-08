package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.pawnW
import org.jetbrains.compose.resources.painterResource

@Composable
fun Logo() {
    Image(
        painter = painterResource(Res.drawable.pawnW),
        contentDescription = "Game Logo",
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(top = 50.dp)
    )
}

@Composable
fun MainMenuButton(
    onClick: () -> Unit, text: String,
    textColor: Color, buttonColor: ButtonColors
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(300.dp)
            .height(100.dp)
            .padding(vertical = 20.dp),
        colors = buttonColor
    ) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.h6)
    }
}