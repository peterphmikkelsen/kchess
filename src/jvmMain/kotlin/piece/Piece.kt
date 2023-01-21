package piece

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import game.Game
import utils.Constants
import utils.Position
import utils.toInt
import java.awt.Cursor
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

    fun isFocused() = state.focused.value
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun PieceView(game: Game, piece: Piece?) {
    piece ?: return
    val density = mutableStateOf(LocalDensity.current.density)
    val offset = mutableStateOf(Offset.Zero)

    Box(Modifier.size(Constants.SQUARE_SIZE.dp).zIndex(if (piece.isFocused()) 2f else 1f)) {
        Image(
            painter = painterResource("${piece.name}_${piece.color}.svg"),
            contentDescription = "${piece.color} piece.type.${piece.name.capitalize(Locale.current)}",
            modifier = Modifier.fillMaxSize().positionPiece(piece, density)
                .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                .onPointerEvent(PointerEventType.Press) { offset.snapToCenterOfCursor(it.changes.first().position, density) }
                .onPointerEvent(PointerEventType.Release) { offset.snapToCenterOfSquare(density) }
                .offset { offset.toInt() }
                .onDrag(
                    onDragStart = { piece.focus() },
                    onDragEnd = {
                        val moved = game.board.value.move(piece.position, piece.toLogicalPosition(offset, density))
                        if (!moved) {
                            offset.value = piece.toWindowPosition(density)
                            piece.unFocus()
                            return@onDrag
                        }

                        game.endTurn()
                        piece.unFocus()
                    },
                    onDragCancel = {
                        offset.value = piece.toWindowPosition(density)
                        piece.unFocus()
                    }
                ) {
                    if (piece.color != game.state.turn.value) return@onDrag
                    offset.value += it
                }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PiecesView(game: Game) {
    for (piece in game.board.value.getPieces())
        PieceView(game, piece)
}

private fun MutableState<Offset>.snapToCenterOfSquare(density: MutableState<Float>) {
    val squareOffset = Constants.SQUARE_SIZE * density.value
    this.value = Offset(
        (this.value.x / squareOffset).roundToInt() * squareOffset,
        (this.value.y / squareOffset).roundToInt() * squareOffset
    )
}

private fun MutableState<Offset>.snapToCenterOfCursor(cursorPosition: Offset, density: MutableState<Float>) {
    val squareOffset = Constants.SQUARE_SIZE / (if (density.value == 1f) 2 else 1) // TODO: This is super hacky
    val positionX = cursorPosition.x - squareOffset
    val positionY = cursorPosition.y - squareOffset
    this.value = Offset(positionX, positionY)
}

private fun Piece.toWindowPosition(density: MutableState<Float>): Offset {
    val squareOffset = Constants.SQUARE_SIZE * density.value
    return Offset(this.position.second * squareOffset, this.position.first * squareOffset)
}

private fun Piece.toLogicalPosition(offset: MutableState<Offset>, density: MutableState<Float>): Position {
    val squareOffset = Constants.SQUARE_SIZE * density.value
    val newPositionX = (this.position.first + (offset.value.y / squareOffset)).roundToInt()
    val newPositionY = (this.position.second + (offset.value.x / squareOffset)).roundToInt()
    return Position(newPositionX, newPositionY)
}

private fun Modifier.positionPiece(piece: Piece, density: MutableState<Float>) = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    val pieceLocation = piece.toWindowPosition(density)
    layout(placeable.width, placeable.height) {
        placeable.placeRelative(IntOffset(pieceLocation.x.toInt(), pieceLocation.y.toInt()))
    }
}