package utils

import androidx.compose.runtime.MutableState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset

class Constants {
    companion object {
        const val SQUARE_SIZE = 120f
        val BACKGROUND_COLOR = Color(0xfff9f9f9)
    }
}

val Color.Companion.DarkSquare: Color
    get() = this.DarkGray

val Color.Companion.LightSquare: Color
    get() = this.LightGray

fun MutableState<Offset>.toInt() = IntOffset(this.value.x.toInt(), this.value.y.toInt())