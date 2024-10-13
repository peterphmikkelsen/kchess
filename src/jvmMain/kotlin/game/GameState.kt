package game

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import piece.PieceColor

data class GameState(
    var turn: MutableState<PieceColor> = mutableStateOf(PieceColor.WHITE),
    var view: MutableState<Boolean> = mutableStateOf(true),
    var running: MutableState<Boolean> = mutableStateOf(false)
) {
    fun flipView() {
        view.value = !view.value
    }

    fun run() {
        running.value = true
    }

    fun cancel() {
        running.value = false
    }
}