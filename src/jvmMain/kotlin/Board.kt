import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.onClick
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.traceEventEnd
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import piece.Piece
import piece.PieceColor
import piece.PieceView
import piece.type.*

class Board {
    private var board = Array(8) { arrayOfNulls<Piece>(8) }

    init {
        for (i in 0 until 8) {
            for (j in 0 until 8) {
                val position = Position(i, j)
                // Handle pawns
                if (i == 1) board[i][j] = Pawn(PieceColor.BLACK, position)
                if (i == 6) board[i][j] = Pawn(PieceColor.WHITE, position)

                // Handle other pieces
                if (i == 0 || i == 7) {
                    val color = if (i == 0) PieceColor.BLACK else PieceColor.WHITE
                    board[i][j] = when (j) {
                        4 -> King(color, position)
                        3 -> Queen(color, position)
                        2, 5 -> Bishop(color, position)
                        1, 6 -> Knight(color, position)
                        0, 7 -> Rook(color, position)
                        else -> null
                    }
                }
            }
        }
    }

    fun move(from: Position, to: Position): Boolean {
        if (from == to) return false

        val piece = board[from.first][from.second] ?: return false
        if (!piece.isValidMove(board, to)) return false

        // Make move on board
        // TODO: Handle attack logic
        board[from.first][from.second] = null
        board[to.first][to.second] = piece

        // Update piece position
        piece.position = to

        return true
    }

    operator fun get(position: Position): Piece? {
        return board[position.first][position.second]
    }

    override fun toString(): String {
        return buildString {
            for (row in board) {
                for (sq in row)
                    if (sq == null) this.append(" . ") else this.append(" $sq ")
                this.appendLine()
            }
        }
    }
}

@Composable
fun BoardView() {
    Box {
        Column {
            repeat(8) { column ->
                Row {
                    repeat(8) { row ->
                        val color = if (column % 2 == 0) {
                            if (row % 2 == 0) Color.LightSquare else Color.DarkSquare
                        } else {
                            if (row % 2 == 0) Color.DarkSquare else Color.LightSquare
                        }
                        Box(Modifier.size(Constants.SQUARE_SIZE.dp).background(color)) {}
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PieceBoardView(game: Game) {
    for (i in 0 until 8) {
        for (j in 0 until 8) {
            PieceView(game, game.board.value[Position(j, i)])
        }
    }
}

fun Modifier.absolutePosition(x: Int, y: Int) =
    layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(x, y)
        }
    }


val Color.Companion.DarkSquare: Color
    get() = this.DarkGray

val Color.Companion.LightSquare: Color
    get() = this.LightGray