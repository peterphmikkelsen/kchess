package piece

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import game.Game
import piece.type.*
import utils.Constants
import utils.Position
import utils.Shapes
import utils.toInt
import java.awt.Cursor
import kotlin.math.roundToInt


interface Piece {
    val name: String
    val color: PieceColor
    var position: Position

    fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean

    fun isNotTurnColor(game: Game) = color != game.state.turn.value
}

@OptIn(ExperimentalComposeUiApi::class)
@ExperimentalFoundationApi
@Composable
fun PieceView(game: Game, piece: Piece?, offset: MutableState<Offset>, focused: MutableState<Boolean>) {
    piece ?: return
    val density = mutableStateOf(LocalDensity.current.density)

    Box(Modifier.size(Constants.SQUARE_SIZE.dp).zIndex(if (focused.value) 2f else 1f)) {
        Box(
//            painter = painterResource(if (focused.value) "${piece.name}_${piece.color}_shadow.svg" else "${piece.name}_${piece.color}.svg"),
//            contentDescription = "${piece.color} piece.type.${piece.name.capitalize(Locale.current)}",
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize().positionPiece(piece, density).offset { offset.toInt() }
                .rotate(if (game.state.view.value) 0f else 180f)
                .pointerHoverIcon(PointerIcon(Cursor(Cursor.HAND_CURSOR)))
                .onPointerEvent(PointerEventType.Press) {
                    if (piece.isNotTurnColor(game)) return@onPointerEvent

                    offset.value = snapToCursor(game, it.changes.first().position, density)
                    focused.value = true
                }
                .onPointerEvent(PointerEventType.Release) {
                    offset.snapToCenterOfSquare(density)

                    val moved = game.board.value.move(piece.position, piece.toLogicalPosition(offset, density))
                    if (!moved) {
                        focused.value = false
                        return@onPointerEvent
                    }

                    focused.value = false
                    game.endTurn()
                }
                .onDrag {
                    if (piece.isNotTurnColor(game)) return@onDrag
                    if (game.state.view.value) {
                        offset.value += it
                    } else {
                        offset.value -= it
                    }
                }
        ) {
            piece.Design(focused)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PiecesView(game: Game) {
    for (piece in game.board.value.getPieces()) {
        val offset = mutableStateOf(Offset.Zero)
        val focused = mutableStateOf(false)
        PieceView(game, piece, offset, focused)
    }
}

private fun MutableState<Offset>.snapToCenterOfSquare(density: MutableState<Float>) {
    val squareOffset = Constants.SQUARE_SIZE * density.value
    this.value = Offset(
        (this.value.x / squareOffset).roundToInt() * squareOffset,
        (this.value.y / squareOffset).roundToInt() * squareOffset
    )
}

private fun snapToCursor(game: Game, cursorPosition: Offset, density: MutableState<Float>): Offset {
    val squareOffset = Constants.SQUARE_SIZE / (if (density.value == 1f) 2 else 1) // TODO: This is super hacky
    val positionX = cursorPosition.x - squareOffset
    val positionY = cursorPosition.y - squareOffset
    return if (game.state.view.value) Offset(positionX, positionY) else Offset(-(positionX-10), -(positionY-5))
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

@Composable
private fun Piece.Design(focused: MutableState<Boolean>) {
    val color = if (this.color == PieceColor.WHITE) Color.White else Color.Black
    val shadow = if (focused.value) 15.dp else 0.dp
    when (this) {
        is Pawn -> Box(modifier = Modifier.shadow(shadow).background(color).fillMaxSize(0.4f))
        is Rook -> Box(modifier = Modifier.shadow(shadow).background(color).fillMaxSize(0.6f))
        is Bishop -> PieceGraphic(Shapes.Triangle, 0.6f, shadow, color)
        is Knight -> PieceGraphic(Shapes.L, 0.6f, shadow, color)
        is Queen -> PieceGraphic(Shapes.Octagon, 0.7f, shadow, color)
        is King -> Box(modifier = Modifier.rotate(-18f).graphicsLayer {
            shadowElevation = shadow.toPx()
            shape = Shapes.Pentagon
            clip = true
        }.fillMaxSize(0.7f).background(color))
    }
}

@Composable
private fun PieceGraphic(pieceShape: Shape, size: Float, shadow: Dp, color: Color) {
    Box(modifier = Modifier.graphicsLayer {
        shadowElevation = shadow.toPx()
        shape = pieceShape
        clip = true
    }.fillMaxSize(size).background(color))
}