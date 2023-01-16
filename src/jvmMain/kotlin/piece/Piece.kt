package piece

import Constants
import Game
import Position
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.roundToInt


interface Piece {
    val name: String
    val color: PieceColor
    var position: Position
    var state: PieceState
    fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean

    fun focus() {
        state.focused.value = true
    }

    fun unFocus() {
        state.focused.value = false
    }
}

@ExperimentalFoundationApi
@Composable
fun PieceView(game: Game, piece: Piece?) {
    piece ?: return
    val offset = mutableStateOf(piece.toWindowPosition())

    Box(Modifier.size(Constants.SQUARE_SIZE.dp).zIndex(if (piece.state.focused.value) 2f else 1f)) {
        Image(
            painter = painterResource("${piece.name}_${piece.color}.svg"),
            contentDescription = "${piece.color} piece.type.${piece.name.capitalize(Locale.current)}",
            modifier = Modifier.fillMaxSize()
                .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
                .onDrag(
                    onDragStart = { piece.focus() },
                    onDragEnd = {
                        offset.snapToCenterOfSquare()

                        val moved = game.board.value.move(piece.position, offset.toLogicalPosition())
                        if (!moved) {
                            offset.value = piece.toWindowPosition()
                            piece.unFocus()
                            return@onDrag
                        }

                        game.endTurn()
                        piece.unFocus()
                    },
                    onDragCancel = {
                        offset.value = piece.toWindowPosition()
                        piece.unFocus()
                    }
                ) {
                    if (piece.color != game.state.turn.value) return@onDrag
                    offset.value += it
                }
        )
    }
}

private fun MutableState<Offset>.snapToCenterOfSquare() {
    val squareOffset = Constants.SQUARE_SIZE * 2
    this.value = Offset(
        (this.value.x / squareOffset).roundToInt() * squareOffset,
        (this.value.y / squareOffset).roundToInt() * squareOffset
    )
}

private fun Piece.toWindowPosition(): Offset {
    val squareOffset = Constants.SQUARE_SIZE * 2
    return Offset(this.position.second * squareOffset, this.position.first * squareOffset)
}

private fun MutableState<Offset>.toLogicalPosition(): Position {
    val squareOffset = Constants.SQUARE_SIZE * 2
    val newPositionY = (this.value.x / squareOffset).roundToInt()
    val newPositionX = (this.value.y / squareOffset).roundToInt()
    return Position(newPositionX, newPositionY)
}