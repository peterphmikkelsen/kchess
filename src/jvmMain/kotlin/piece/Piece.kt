package piece

import Constants
import Game
import Position
import absolutePosition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.onDrag
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
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
    fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean
}

@ExperimentalFoundationApi
@Composable
fun PieceView(game: Game, piece: Piece?) {
    piece ?: return
    val squareOffset = Constants.SQUARE_SIZE * 2
    val offset = mutableStateOf(Offset(piece.position.second * squareOffset, piece.position.first * squareOffset))

    Box(Modifier.size(Constants.SQUARE_SIZE.dp)) {
        Image(
            painter = painterResource("${piece.name}_${piece.color}.svg"),
            contentDescription = "${piece.color} piece.type.${piece.name.capitalize(Locale.current)}",
            modifier = Modifier.fillMaxSize()
                .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
                .onDrag(
                    onDragEnd = {
                        // Move piece to center of square on release
                        offset.value = Offset(
                            (offset.value.x / squareOffset).roundToInt() * squareOffset,
                            (offset.value.y / squareOffset).roundToInt() * squareOffset
                        )

                        // Move piece in internal game logic
                        val newPositionX = (offset.value.y / squareOffset).toInt()
                        val newPositionY = (offset.value.x / squareOffset).toInt()
                        val moved = game.board.value.move(piece.position, Position(newPositionX, newPositionY))

                        // Don't end the turn if the move was not successful
                        if (!moved) return@onDrag
                        // TODO: Fix bug that increases offset when this is on
                        // game.state.endTurn()

                        println(game.board.value)
                    }
                ) {
                    if (piece.color != game.state.turn.value) return@onDrag
                    offset.value += it
                }
        )
    }
}