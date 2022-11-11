package piece

import Board
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
fun PieceView(board: Board, piece: Piece?) {
    piece ?: return
    var prevOffset = Offset(0f, 0f)
    val offset = mutableStateOf(prevOffset)

    Box(modifier = Modifier.size(120.dp)) {
        Image(
            painter = painterResource("${piece.name}_${piece.color}.svg"),
            contentDescription = "White piece.type.King",
            modifier = Modifier.fillMaxSize().zIndex(100f).offset {
                IntOffset(offset.value.x.toInt(), offset.value.y.toInt())
            }.onDrag(
                onDragEnd = {
                    offset.value = Offset(round(offset.value.x / 120f) * 120f, round(offset.value.y / 120f) * 120f)
//                    println(Position((piece.position.first + (offset.value.y / 120)).toInt(), (piece.position.second + (offset.value.x / 120)).toInt()))
                    val newPositionX = (piece.position.first + ((offset.value.y - prevOffset.y) / 120)).toInt()
                    val newPositionY = (piece.position.second + ((offset.value.x - prevOffset.x) / 120)).toInt()
                    board.move(piece.position, Position(newPositionX, newPositionY))
                    println(board)
                    prevOffset = offset.value
                }
            ) {
                offset.value += it
            }
        )
    }
}