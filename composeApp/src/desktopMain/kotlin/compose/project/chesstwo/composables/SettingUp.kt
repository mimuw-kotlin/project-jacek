package compose.project.chesstwo.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import chesstwo.composeapp.generated.resources.Res
import chesstwo.composeapp.generated.resources.mmBackground
import compose.project.chesstwo.Board
import compose.project.chesstwo.PlayerColor
import compose.project.chesstwo.SetUpBoard
import compose.project.chesstwo.allTextures
import compose.project.chesstwo.darkSquareColor
import compose.project.chesstwo.fogDarkSquareColor
import compose.project.chesstwo.fogLightSquareColor
import compose.project.chesstwo.lightSquareColor
import compose.project.chesstwo.pieces.King
import compose.project.chesstwo.pieces.Piece
import compose.project.chesstwo.textureToString
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.random.Random

@Composable
fun SideButton(choosingColor: PlayerColor, setChoosingColor: (PlayerColor)->Unit,width: Int, enabled : Boolean = true) {
    //var choosingColor by remember { mutableStateOf(0) }

    val buttonColor = if (choosingColor == PlayerColor.BLACK) Color.White else Color.Black
    val textColor = if (choosingColor == PlayerColor.BLACK) Color.Black else Color.White

    Button(
        onClick = {
            setChoosingColor(PlayerColor.negateColor(choosingColor))
        },
        colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor),
        modifier = Modifier.padding(16.dp).width(width.dp),
        enabled = enabled
    ) {
        Text(
            text = if (choosingColor == PlayerColor.WHITE) "Black Side" else "White Side",
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

@Composable
fun SettingUpScene(onExitToMenu: () -> Unit, startGame : (MutableMap<Pair<Int,Int>, Piece>) -> Unit, newBoardSize : Int = 8, newPoints: Int = 38, computerGame : Boolean = false, computerColor : PlayerColor = PlayerColor.BLACK){
    var choosingColor by remember { mutableStateOf(PlayerColor.negateColor(computerColor)) }
    var selectedPiece by remember { mutableStateOf(Res.drawable.mmBackground)}
    val board by remember {mutableStateOf(SetUpBoard(newBoardSize))}
    val points = remember{ mutableStateListOf(newPoints,newPoints) }
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
                            setUpComputerSide(computerColor,points[computerColor.value],board)
                        ) },text = "Start", textColor = Color.Black, buttonColor = ButtonDefaults.buttonColors(backgroundColor = Color.Green),width=150)
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
                                            contentDescription = if(textureToString(currentTexture) !=null) stringResource(
                                                textureToString(currentTexture)!!.first) else null,
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