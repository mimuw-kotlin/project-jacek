package compose.project.chesstwo

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {

    Window(
        onCloseRequest = ::exitApplication,
        title = "ChessTwo",
        resizable = false,
        state = WindowState(size = DpSize(1200.dp,900.dp))

    ) {
        App()
    }
}