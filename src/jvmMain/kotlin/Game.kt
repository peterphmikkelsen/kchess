import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class Game {

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
    BoardView(game.board)
}