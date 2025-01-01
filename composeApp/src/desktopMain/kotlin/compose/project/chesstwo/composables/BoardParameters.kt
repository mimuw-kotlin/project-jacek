package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.mmBackground
import org.jetbrains.compose.resources.painterResource

@Composable
fun BoardParametersScene(
    onSubmitParameters : (Int,Int)->Unit) {

    val initialValue = 8
    val minValue = 8
    val maxValue = 30
    var boardSize by remember { mutableStateOf(initialValue.coerceIn(minValue, maxValue)) }
    var points by remember { mutableStateOf("38") }
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Select board size")
            Row {
                Slider(
                    value = boardSize.toFloat(),
                    onValueChange = {
                        val newValue = (it.toInt() / 2) * 2
                        boardSize = newValue
                    },
                    valueRange = minValue.toFloat()..maxValue.toFloat(),
                    steps = (maxValue - minValue) / 2 - 1,
                    modifier = Modifier.width(1100.dp)
                )
                Text(text = boardSize.toString(), modifier = Modifier.width(50.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Enter amount of points for players")
            TextField(
                value = points,
                onValueChange = {
                    val parsedValue = it.toIntOrNull()
                    if (parsedValue != null) {
                        points = it
                    }

                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onSubmitParameters(boardSize, points.toInt()) },
                modifier = Modifier
                    .padding(vertical = 20.dp)
                    .width(100.dp)
            ) {
                Text(text = "Done", style = MaterialTheme.typography.h6)
            }
        }
    }
}
