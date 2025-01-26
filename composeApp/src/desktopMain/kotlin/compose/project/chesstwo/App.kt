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
import compose.project.chesstwo.composables.EndDisplay
import compose.project.chesstwo.composables.EndGameButton
import compose.project.chesstwo.composables.Logo
import compose.project.chesstwo.composables.MainMenuButton
import compose.project.chesstwo.composables.PieceSelection
import compose.project.chesstwo.composables.RightPanel
import compose.project.chesstwo.composables.SettingUpButton
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
@Composable
fun ChessBoard(board: Board, onExitToMenu: () -> Unit, gameWithComputer: Boolean = false, computerColor : PlayerColor = PlayerColor.BLACK) {

    var isPieceSelected by remember { mutableStateOf(false) }

    //Position of the selected piece
    var selectedPieceX by remember { mutableIntStateOf(-1) }
    var selectedPieceY by remember { mutableIntStateOf(-1) }

    //List of squares attacked by the selected piece
    var attackedSquares = remember { mutableStateListOf<Pair<Int, Int>>() }

    //A need to display pieces for promotion
    var promotionDisplay by remember { mutableStateOf(false) }

    //A need to display game result
    var endDisplay by remember { mutableStateOf(false) }

    var computerToPlay by remember {mutableStateOf(gameWithComputer && computerColor==PlayerColor.WHITE)}//(!gameWithComputer || !computerToPlay
    val computer = Computer(computerColor)
    println("EWALUACJA: ${computer.evaluate(board)}")


    if(gameWithComputer && computerToPlay && !board.promotionChoice){
        //println("SEARCH: ${computer.search(board,3, -1001,1001)}")
        val move = computer.makeMove(board)
        if(move!=null){
            board.possibleMoves.clear()
            board.possibleMoves.addAll(board.piecesPositions[Pair(move.startX,move.startY)]!!.getMoves(board))
            board.move(move.startX,move.startY,move.endX,move.endY, computerMove = true)

        }

        endDisplay = board.gameEnded
        computerToPlay = false
    }

    //Textures of pieces available for promotion


    //Background image
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Chessboard background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Row {

            //Chessboard

            Box(modifier = Modifier.padding(25.dp)) {

                //Construction of the chessboard
                Column {
                    for (row in 0 until board.size) {
                        Row {
                            for (col in 0 until board.size) {
                                Box(
                                    modifier = Modifier
                                        .size((800/board.size).dp)
                                        .background(
                                            if ((row + col) % 2 == 0) lightSquareColor
                                            else darkSquareColor

                                        )
                                        .clickable {

                                            //Clicked empty square
                                            if (isPieceSelected && !board.promotionChoice && !(gameWithComputer && computerToPlay)) {
                                                //Move if available
                                                if (Pair(col, row) in attackedSquares.toList()) {
                                                    board.move(
                                                        selectedPieceX,
                                                        selectedPieceY,
                                                        col,
                                                        row
                                                    )
                                                    endDisplay = board.gameEnded
                                                    computerToPlay = true

                                                }
                                                isPieceSelected = false

                                            }
                                        }

                                ) {
                                    if (Pair(col, row) in board.piecesPositions.keys) {
                                        // Image of a piece on that square
                                        val currentTexture = board.piecesPositions[Pair(
                                            col,
                                            row
                                        )]!!.texture
                                        Image(
                                            painter = painterResource(
                                                currentTexture
                                            ),
                                            contentDescription = if(textureToString(currentTexture)!=null) stringResource(textureToString(currentTexture)!!.first) else null,
                                            modifier = Modifier.clickable {
                                                if (!board.promotionChoice && !(gameWithComputer && computerToPlay)) {
                                                    val newPiece = board
                                                        .piecesPositions[Pair(col, row)]!!
                                                    //Attacked another piece
                                                    if (isPieceSelected && Pair(
                                                            col,
                                                            row
                                                        ) in attackedSquares.toList()
                                                    ) {
                                                        board.move(
                                                            selectedPieceX,
                                                            selectedPieceY,
                                                            col,
                                                            row
                                                        )
                                                        endDisplay = board.gameEnded
                                                        isPieceSelected = false
                                                        computerToPlay = true

                                                    }
                                                    //Selected another piece

                                                    else if (
                                                        newPiece.color == PlayerColor.turnToColor(board.turn)) {

                                                        isPieceSelected = false

                                                        //Getting all possible moves
                                                        //for newly selected piece
                                                        board.possibleMoves.clear()
                                                        board.possibleMoves.addAll(
                                                            newPiece.getMoves(board)
                                                        )

                                                        //Getting all attacked squares
                                                        //for newly selected piece
                                                        attackedSquares.clear()
                                                        attackedSquares
                                                            .addAll(board.getAttackedSquares())

                                                        //Update selected piece information
                                                        if (selectedPieceX != col
                                                            || selectedPieceY != row
                                                            || !isPieceSelected
                                                        ) {
                                                            selectedPieceX = col
                                                            selectedPieceY = row
                                                            isPieceSelected = true

                                                        }
                                                    }
                                                }
                                            }
                                        )

                                    }

                                    //Drawing squares attacked by selected piece
                                    if (isPieceSelected && Pair(col, row)
                                        in board.getAttackedSquares()
                                    ) {
                                        Canvas(
                                            modifier = Modifier
                                                .size((400/board.size).dp)
                                                .align(Alignment.Center),
                                            onDraw = {
                                                drawCircle(color = Color.Green, alpha = 0.5f)
                                            })
                                    }

                                }
                            }
                        }
                    }
                }
            }
            //Right panel
            Column(horizontalAlignment = Alignment.CenterHorizontally){
                if(!gameWithComputer){
                    Spacer(modifier = Modifier.height(25.dp))
                    val backgroundColor = if(PlayerColor.turnToColor(board.turn) == PlayerColor.WHITE)Color.White else Color.Black
                    Box(modifier = Modifier.background(backgroundColor).width(300.dp).height(60.dp), contentAlignment = Alignment.Center) {
                        if (PlayerColor.turnToColor(board.turn) == PlayerColor.WHITE) {
                            Text(
                                text = "White to move",
                                textAlign = TextAlign.Center,
                                color = Color.Black,
                                style = MaterialTheme.typography.h6
                            )
                        } else {
                            Text(
                                text = "Black to move",
                                textAlign = TextAlign.Center,
                                color = Color.White,
                                style = MaterialTheme.typography.h6
                            )
                        }
                    }
                }
                promotionDisplay = board.promotionChoice
                RightPanel(
                    promotionDisplay = promotionDisplay,
                    onEndDisplayChange = {newValue ->  println("endDisplay change: $endDisplay -> $newValue");endDisplay =newValue},
                    onExitToMenu = onExitToMenu,
                    board = board,
                    textures = textures,
                    computerGame = gameWithComputer
                )

            }

        }
        // Display of the result of the game
        if (endDisplay) {
            EndDisplay(board, onExitToMenu)

        }
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
fun setUpComputerSide(computerColor : PlayerColor, points : Int, board : Board) : MutableMap<Pair<Int,Int>,Piece>{
    val piecesPositions = board.piecesPositions.toMutableMap()
    val rank = if(computerColor==PlayerColor.BLACK) listOf(0,1,2) else listOf(board.size-1,board.size-2,board.size-3)
    var triesLeft = 7
    var pointsLeft = points
    while(points>0 && triesLeft>0){

        val choosenPiece = board.textureToPiece(allTextures[computerColor.value].random(),0,0,computerColor)
        if(choosenPiece.value>pointsLeft){
            triesLeft-=1
            continue
        }
        val posX = Random.nextInt(0,board.size-1)
        val posY = if(choosenPiece.value<=4) rank[2] else if(choosenPiece.value<=8) rank[1] else rank[0]
        if(Pair(posX,posY) in piecesPositions.keys){
            triesLeft-=1
            continue
        }
        choosenPiece.posX=posX
        choosenPiece.posY=posY
        piecesPositions[Pair(posX,posY)]=choosenPiece
        triesLeft=7
        pointsLeft-=choosenPiece.value
    }
    return piecesPositions

}


@Composable
fun SettingUpScene(onExitToMenu: () -> Unit, startGame : (MutableMap<Pair<Int,Int>,Piece>) -> Unit, newBoardSize : Int = 8, newPoints: Int = 38, computerGame : Boolean = false, computerColor : PlayerColor = PlayerColor.BLACK){
    var choosingColor by remember { mutableStateOf(PlayerColor.negateColor(computerColor)) }
    var selectedPiece by remember { mutableStateOf(Res.drawable.mmBackground)}
    val board by remember {mutableStateOf(SetUpBoard(newBoardSize))}
    val points = remember{ mutableStateListOf(newPoints,newPoints)}
    var sideSelectedPiece by remember{ mutableStateOf(false)}
    //val board = SetUpBoard(8)
    //println(numPieces)
    Image(
        painter = painterResource((Res.drawable.mmBackground)),
        contentDescription = "Setting up background", alpha = 0.4f
    )

    Box(
        modifier = Modifier
            .background(Color(0x9989d0e8)).fillMaxSize()
    ) {
        Row {
            Box(modifier = Modifier.width(175.dp)){
                Column{
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        SideButton(choosingColor=choosingColor, setChoosingColor =  {choosingColor=it; sideSelectedPiece = false},width = 150, enabled = !computerGame)
                    }
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        SettingUpButton(onClick = { if(!computerGame)startGame(board.piecesPositions) else startGame(
                            setUpComputerSide(computerColor,points[computerColor.value],board)) },text = "Start", textColor = Color.Black, buttonColor = ButtonDefaults.buttonColors(backgroundColor = Color.Green),width=150)
                    }
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
                        SettingUpButton(onClick = onExitToMenu,text = "Main Menu", textColor = Color.White, buttonColor = ButtonDefaults.buttonColors(backgroundColor = Color.Red),width=150)
                    }
                    if(sideSelectedPiece){
                        val desc = textureToString(selectedPiece)!!
                        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally){
                            Text(text = stringResource(desc.first),style = MaterialTheme.typography.h5)
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(text = stringResource(desc.second),style = MaterialTheme.typography.h6, textAlign = TextAlign.Center)
                        }

                    }

                }
            }
            //Chessboard

            Box(modifier = Modifier.padding(25.dp)) {

                //Construction of the chessboard
                Column {
                    for (row in 0 until board.size) {
                        Row {
                            for (col in 0 until board.size) {
                                Box(
                                    modifier = Modifier
                                        .size((800/board.size).dp)
                                        .background(
                                            if(board.inAvailableSpace(row,choosingColor)){
                                                if ((row + col) % 2 == 0) lightSquareColor
                                                else darkSquareColor
                                            } else {
                                                if ((row + col) % 2 == 0) fogLightSquareColor
                                                else fogDarkSquareColor
                                            }


                                        )
                                        .clickable {
                                            if(sideSelectedPiece && Pair(col,row) !in board.piecesPositions.keys && board.inAvailableSpace(row,choosingColor)
                                                && points[choosingColor.value] >=board.textureToPiece(selectedPiece,col,row,choosingColor).value){
                                                board.placePiece(col,row,choosingColor)
                                                points[choosingColor.value]-=board.textureToPiece(selectedPiece,col,row,choosingColor).value
                                            }

                                        }

                                ){
                                    if(Pair(col,row) in board.piecesPositions.keys && board.piecesPositions[Pair(col,row)]!!.color==choosingColor){
                                        val currentTexture = board.piecesPositions[Pair(
                                            col,
                                            row
                                        )]!!.texture
                                        Image(
                                            painter = painterResource(
                                                currentTexture
                                            ),
                                            contentDescription = if(textureToString(currentTexture)!=null) stringResource(textureToString(currentTexture)!!.first) else null,
                                            modifier = Modifier
                                                .clickable {
                                                    val pieceToDelete = board.piecesPositions[Pair(col,row)]!!
                                                    if(pieceToDelete !is King &&
                                                        !((pieceToDelete.color==PlayerColor.WHITE && pieceToDelete.posY==board.size-2 && pieceToDelete.posX==board.size/2)
                                                                ||(pieceToDelete.color==PlayerColor.BLACK && pieceToDelete.posY==1 && pieceToDelete.posX==board.size/2))){
                                                        points[choosingColor.value]+=pieceToDelete.value
                                                        board.piecesPositions.remove(Pair(col,row))
                                                    }

                                                }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Box{
                Column(modifier = Modifier.fillMaxWidth().wrapContentSize(Alignment.TopCenter)){

                    PieceSelection(allTextures[choosingColor.value]) {
                        selectedPiece = it
                        board.selectedPieceTexture = it
                        sideSelectedPiece = true
                    }
                    Text(text = "Points left: ${points[choosingColor.value]}",style = MaterialTheme.typography.h6, modifier = Modifier.padding(top=25.dp).width(200.dp))
                    if(sideSelectedPiece){
                        Text(text = "Piece value: ${board.textureToPiece(selectedPiece,0,0,PlayerColor.WHITE).value}",style = MaterialTheme.typography.h6,
                            modifier = Modifier.width(200.dp))
                    }
                }

            }
        }

    }
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
    //board.setupPieces()
    MaterialTheme {
        when (currentScene) {
            is Scene.MainMenu -> MainMenuScene(onStartGame =
            { currentScene = Scene.BoardParameters;  computerGame=false;},//board = Board(size=10); board.setupPieces();
                {currentScene = Scene.BoardParameters; computerGame=true;})// board = Board(size=10); board.setupPieces();

            is Scene.Gameplay -> ChessBoard(board, onExitToMenu =
            { currentScene = Scene.MainMenu }, gameWithComputer = computerGame, computerColor = computerColor)

            is Scene.SettingUp -> SettingUpScene(onExitToMenu =
            { currentScene = Scene.MainMenu }, startGame = {pos -> board = Board(boardSize);board.piecesPositions=pos;currentScene=Scene.Gameplay}, newBoardSize = boardSize, newPoints = points, computerGame = computerGame, computerColor = computerColor)

            is Scene.BoardParameters -> BoardParametersScene(onSubmitParameters = {newBoardSize,newPoints, newComputerColor ->boardSize = newBoardSize; points = newPoints;computerColor=newComputerColor;currentScene=Scene.SettingUp},
                onSelectedScenario = {pos, newBoardSize, newComputerColor -> board = Board(newBoardSize); board.piecesPositions=pos;computerColor=newComputerColor;currentScene = Scene.Gameplay},computerGame)
        }
    }
}