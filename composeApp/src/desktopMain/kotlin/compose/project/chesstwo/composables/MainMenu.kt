package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.mmBackground
import chesstwo.composeapp.generated.resources.pawnW
import compose.project.chesstwo.buttonColor
import org.jetbrains.compose.resources.painterResource
import kotlin.system.exitProcess

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
        colors = buttonColor,

    ) {
        Text(text = text, color = textColor, style = MaterialTheme.typography.h6)
    }
}

@Composable
fun MainMenuScene(onStartGame: () -> Unit, onStartComputerGame: () -> Unit) {

    //Background
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Main Menu background",
        alpha = 0.7f
    )

    //Buttons
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x7689d0e8))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Logo()
        Spacer(modifier = Modifier.height(100.dp))
        MainMenuButton(
            onStartGame, "Start game", Color.Black,
            ButtonDefaults.buttonColors(backgroundColor = buttonColor)
        )

        MainMenuButton(
            onStartComputerGame, "Play the computer", Color.Black,
            ButtonDefaults.buttonColors(backgroundColor = buttonColor)
        )

        MainMenuButton(
            { exitProcess(0) }, "Exit", Color.White,
            ButtonDefaults.buttonColors(backgroundColor = Color.Red)
        )
    }
}