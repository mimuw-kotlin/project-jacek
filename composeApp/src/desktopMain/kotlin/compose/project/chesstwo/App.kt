package compose.project.chesstwo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column

import androidx.compose.material.MaterialTheme

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res

import chesstwo.composeapp.generated.resources.rookB
import chesstwo.composeapp.generated.resources.rookW
import chesstwo.composeapp.generated.resources.queenB
import chesstwo.composeapp.generated.resources.queenW
import chesstwo.composeapp.generated.resources.bishopW
import chesstwo.composeapp.generated.resources.bishopB
import chesstwo.composeapp.generated.resources.knightW
import chesstwo.composeapp.generated.resources.knightB
import chesstwo.composeapp.generated.resources.mmBackground
import compose.project.chesstwo.composables.BoardParametersScene
import compose.project.chesstwo.composables.ChessBoard
import compose.project.chesstwo.composables.EndDisplay
import compose.project.chesstwo.composables.EndGameButton
import compose.project.chesstwo.composables.Logo
import compose.project.chesstwo.composables.MainMenuButton
import compose.project.chesstwo.composables.MainMenuScene
import compose.project.chesstwo.composables.PieceSelection
import compose.project.chesstwo.composables.RightPanel
import compose.project.chesstwo.composables.SettingUpButton
import compose.project.chesstwo.composables.SettingUpScene
import compose.project.chesstwo.composables.SideButton
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Piece
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random
import kotlin.system.exitProcess

val buttonColor = Color(0xff89d0e8)

fun textureToString(texture : DrawableResource) : Pair<StringResource,StringResource>?{
    var id = allTextures[0].indexOf(texture)
    if(id==-1) id = allTextures[1].indexOf(texture)
    if(id==-1){
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
    //Scene that is currently displayed
    var currentScene by remember { mutableStateOf<Scene>(Scene.MainMenu) }

    var board = Board(size = 10)
    var boardSize = 8
    var points = 38
    var computerGame = false
    var computerColor = PlayerColor.BLACK
    MaterialTheme {
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame =
            { currentScene = Scene.BoardParameters;  computerGame=false;},
                {currentScene = Scene.BoardParameters; computerGame=true;})

            is Scene.Gameplay -> ChessBoard(board, onExitToMenu =
            { currentScene = Scene.MainMenu }, gameWithComputer = computerGame, computerColor = computerColor)

            is Scene.SettingUp -> SettingUpScene(onExitToMenu =
            { currentScene = Scene.MainMenu }, startGame = {pos -> board = Board(boardSize);board.piecesPositions=pos;currentScene=Scene.Gameplay}, newBoardSize = boardSize, newPoints = points, computerGame = computerGame, computerColor = computerColor)

            is Scene.BoardParameters -> BoardParametersScene(onSubmitParameters = {newBoardSize,newPoints, newComputerColor ->boardSize = newBoardSize; points = newPoints;computerColor=newComputerColor;currentScene=Scene.SettingUp},
                onSelectedScenario = {pos, newBoardSize, newComputerColor -> board = Board(newBoardSize); board.piecesPositions=pos;computerColor=newComputerColor;currentScene = Scene.Gameplay},computerGame)
        }
    }
}