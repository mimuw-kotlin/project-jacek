package compose.project.chesstwo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import compose.project.chesstwo.composables.BoardParametersScene
import compose.project.chesstwo.composables.ChessBoard
import compose.project.chesstwo.composables.MainMenuScene
import compose.project.chesstwo.composables.SettingUpScene
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

val buttonColor = Color(0xff89d0e8)

fun textureToString(texture: DrawableResource): Pair<StringResource, StringResource>? {
    var id = allTextures[0].indexOf(texture)
    if (id == -1) id = allTextures[1].indexOf(texture)
    if (id == -1) {
        return null
    }
    return Pair(pieceDescriptions[0][id], pieceDescriptions[1][id])

}

sealed class Scene {
    data object MainMenu : Scene()
    data object Gameplay : Scene()
    data object SettingUp : Scene()
    data object BoardParameters : Scene()
}


@Composable
@Preview
fun App() {
    // Scene that is currently displayed
    var currentScene by remember { mutableStateOf<Scene>(Scene.MainMenu) }

    var board = Board(size = 10)
    var boardSize = 8
    var points = 38
    var computerGame = false
    var computerColor = PlayerColor.BLACK
    MaterialTheme {
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame =
            { currentScene = Scene.BoardParameters; computerGame = false; },
                { currentScene = Scene.BoardParameters; computerGame = true; })

            is Scene.Gameplay -> ChessBoard(
                board,
                onExitToMenu =
                { currentScene = Scene.MainMenu },
                gameWithComputer = computerGame,
                computerColor = computerColor
            )

            is Scene.SettingUp -> SettingUpScene(
                onExitToMenu =
                { currentScene = Scene.MainMenu },
                startGame = { pos ->
                    board = Board(boardSize);board.piecesPositions = pos;currentScene =
                    Scene.Gameplay
                },
                newBoardSize = boardSize,
                newPoints = points,
                computerGame = computerGame,
                computerColor = computerColor
            )

            is Scene.BoardParameters -> BoardParametersScene(
                onSubmitParameters = { newBoardSize, newPoints, newComputerColor ->
                    boardSize = newBoardSize; points = newPoints;computerColor =
                    newComputerColor;currentScene = Scene.SettingUp
                },
                onSelectedScenario = { pos, newBoardSize, newComputerColor ->
                    board = Board(newBoardSize); board.piecesPositions = pos;computerColor =
                    newComputerColor;currentScene = Scene.Gameplay
                }, computerGame
            )
        }
    }
}