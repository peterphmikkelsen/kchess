package board
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import piece.Piece
import piece.PieceColor
import piece.type.*
import utils.Constants
import utils.DarkSquare
import utils.LightSquare
import utils.Position


class Board {
    private var board = Array(8) { arrayOfNulls<Piece>(8) }
    val moves = mutableListOf<String>()

    init {
        reset()
    }

    fun reset() {
        board = Array(8) { arrayOfNulls(8) }
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
                        4 -> King(color, position, false)
                        3 -> Queen(color, position)
                        2, 5 -> Bishop(color, position)
                        1, 6 -> Knight(color, position)
                        0, 7 -> Rook(color, position, false)
                        else -> null
                    }
                }
            }
        }
        moves.clear()
    }

    fun move(from: Position, to: Position): Boolean {
        if (from == to) return false

        val piece = board[from.first][from.second] ?: return false
        val toPiece = board[to.first][to.second]

        if (toPiece != null && piece.color == toPiece.color) return false
        if (!piece.isValidMove(board, to)) return false

        moves.add(moveToChessNotation(from, to))

        // Make move on board
        board[from.first][from.second] = null
        board[to.first][to.second] = piece

        // Update piece position
        piece.position = to

        return true
    }

    operator fun get(position: Position): Piece? {
        return board[position.first][position.second]
    }

    fun getPieces(): MutableList<Piece> {
        val pieces = mutableListOf<Piece>()
        this.board.forEach { pieces.addAll(it.filterNotNull()) }
        return pieces
    }

    fun moveToChessNotation(from: Position, to: Position): String {
        val fromPiece = board[from.first][from.second]
        val toPiece = board[to.first][to.second]
        return buildString {
            if (fromPiece!!.name != "pawn") append(fromPiece)
            if (toPiece != null) {
                if (fromPiece is Pawn) append('a' + from.second)
                append("x")
            }
            append('a' + to.second)
            append(8 - to.first)
        }
    }

    fun boardToFen(): String {
        return buildString {
            for (row in board) {
                var empty = 0
                for (piece in row) {
                    if (piece != null) {
                        if (empty > 0) {
                            append(empty)
                            empty = 0
                        }
                        if (piece.color == PieceColor.WHITE) append(piece) else append(piece.toString().lowercase())
                    } else empty++
                }
                if (empty > 0) append(empty)
                append("/")
            }

        }.dropLast(1)
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