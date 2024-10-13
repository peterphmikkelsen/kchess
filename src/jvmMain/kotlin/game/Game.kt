package game
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import board.Board
import board.BoardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import piece.PieceColor
import piece.PiecesView
import utils.Constants
import utils.Position
import java.awt.Cursor

class Game {
    val state = GameState()
    val board = mutableStateOf(Board())

    // Scandinavian Opening
    private val predefinedMoves = listOf(
        Position(1, 3) to Position(3, 3), // d5
        Position(0, 3) to Position(3, 3), // Qxd5
        Position(0, 2) to Position(4, 6), // Bg4
        Position(0, 1) to Position(2, 2), // Nc6
        Position(3, 3) to Position(1, 3), // Qd7
        Position(4, 6) to Position(5, 5)  // Bxf3
    )

    suspend fun start() {
        println("Starting game...")
        state.run()
        var i = 0
        while (state.running.value) {
            if (!checkTurn()) {
                delay(1000)
                if (i >= predefinedMoves.size) {
                    break
                }
                board.value.move(predefinedMoves[i].first, predefinedMoves[i].second)
                i++
                endTurn()
            }
        }
        delay(5000)
        stop()
    }

    fun stop() {
        println("Stopping game!")
        state.cancel()
        board.value.reset()
    }

    fun checkTurn() = state.turn.value == PieceColor.WHITE

    fun endTurn() {
        println("${board.value.boardToFen()} ${if (state.turn.value == PieceColor.WHITE) "w" else "b"}")
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
    val coroutineScope = rememberCoroutineScope()

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
                Row {
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
                    Box(
                        Modifier.height(100.dp)
                            .width(200.dp)
                            .padding(10.dp)
                            .background(if (game.state.running.value) Color(255, 71, 76) else Color(144, 238, 144))
                            .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                            .onClick {
                                if (game.state.running.value) {
                                    game.stop()
                                } else {
                                    coroutineScope.launch { withContext(Dispatchers.IO) { game.start() } }
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(if (game.state.running.value) "Stop Game" else "Start Game", fontSize = 1.33.em, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Box(Modifier.fillMaxSize().padding(start = 10.dp, end = 10.dp, bottom = 12.dp).border(1.dp, Color.DarkGray)) {
                    val moves = game.board.value.moves.chunked(2)
                    Text("1. ${moves.joinToString("\n${moves.size}. ")}", modifier = Modifier.padding(5.dp))
                }
            }
        }
    }
}