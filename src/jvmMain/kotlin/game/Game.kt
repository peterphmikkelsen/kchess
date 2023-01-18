package game
import utils.Constants
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import board.Board
import board.BoardView
import piece.PieceColor
import piece.PiecesView

class Game {
    val state = GameState()
    val board = mutableStateOf(Board())

    fun start() {

    }

    fun stop() {

    }

    fun checkTurn() = state.turn.value == PieceColor.WHITE

    fun endTurn() {
        state.turn.value = if (checkTurn()) PieceColor.BLACK else PieceColor.WHITE
    }

    fun winner(): Boolean {
        return true
    }
}

@Composable
@Preview
fun GameView() {
    val game = remember { Game() }
    Box(Modifier.fillMaxSize().background(Constants.BACKGROUND_COLOR)) {
        Row {
            Box(Modifier.size((8 * Constants.SQUARE_SIZE).dp).border(3.dp, Color.Black)) {
                BoardView()
                PiecesView(game)
            }

            // TODO: Move to separate Composable
            Box(Modifier.size(50.dp)
                .padding(10.dp)
                .border(1.dp, Color.DarkGray)
                .background(if (game.checkTurn()) Color.White else Color.Black)
            ) {}
        }
    }
}