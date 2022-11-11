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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import java.lang.Math.round


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
    var prevOffset = Offset(0f, 0f)
    val offset = mutableStateOf(prevOffset)

    val squareSize = Constants.SQUARE_SIZE

    Box(Modifier.size(squareSize.dp)) {
        Image(
            painter = painterResource("${piece.name}_${piece.color}.svg"),
            contentDescription = "${piece.color} piece.type.${piece.name.capitalize(Locale.current)}",
            modifier = Modifier.fillMaxSize().zIndex(100f).offset {
                IntOffset(offset.value.x.toInt(), offset.value.y.toInt())
            }.onDrag(
                onDragEnd = {
                    // Move piece to center of square on release
                    offset.value = Offset(round(offset.value.x / squareSize) * squareSize, round(offset.value.y / squareSize) * squareSize)

                    // Move piece in internal game logic
                    val newPositionX = (piece.position.first + ((offset.value.y - prevOffset.y) / squareSize)).toInt()
                    val newPositionY = (piece.position.second + ((offset.value.x - prevOffset.x) / squareSize)).toInt()
                    val moved = game.board.value.move(piece.position, Position(newPositionX, newPositionY))

                    // Don't end the turn if the move was not successful
                    if (!moved) return@onDrag
                    game.state.endTurn()

                    println(game.board.value)

                    prevOffset = offset.value
                }
            ) {
                if (piece.color != game.state.turn.value) return@onDrag
                offset.value += it
            }
        )
    }
}