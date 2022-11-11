import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import piece.PieceColor

class Game {

    val state = GameState()
    val board = mutableStateOf(Board())

    fun start() {

    }

    fun stop() {

    }

    fun winner(): Boolean {
        return true
    }
}

@Composable
@Preview
fun GameView() {
    val game = remember { Game() }
    Row(Modifier.fillMaxSize().background(Constants.BACKGROUND_COLOR)) {
        BoardView(game)

        // TODO: Move to separate Composable
        Box(Modifier.size(50.dp)
            .padding(10.dp)
            .border(1.dp, Color.DarkGray)
            .background(if (game.state.checkTurn()) Color.White else Color.Black)
        ) {}
    }
}