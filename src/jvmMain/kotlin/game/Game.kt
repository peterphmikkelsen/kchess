package game
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp
import board.Board
import board.BoardView
import piece.PieceColor
import piece.PiecesView
import utils.Constants
import java.awt.Cursor

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun GameView() {
    val game = remember { Game() }
    Box(Modifier.fillMaxSize().background(Constants.BACKGROUND_COLOR)) {
        Row {
            Box(Modifier.size((8 * Constants.SQUARE_SIZE).dp)
                .border(3.dp, Color.Black)
                .rotate(if (game.state.view.value) 0f else 180f)
            ) {
                BoardView()
                PiecesView(game)
            }

            Column {
                // TODO: Move to separate Composable
                Box(
                    Modifier.size(50.dp)
                        .padding(10.dp)
                        .border(1.dp, Color.DarkGray)
                        .background(if (game.checkTurn()) Color.White else Color.Black)
                ) {}
                Box {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        modifier = Modifier.size(40.dp).padding(start = 10.dp)
                            .onClick { game.state.flipView() }
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR))),
                        contentDescription = "flip view",
                        tint = Color.DarkGray
                    )
                }
            }
        }
    }
}