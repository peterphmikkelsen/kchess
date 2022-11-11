import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import piece.PieceColor

data class GameState(
    var turn: MutableState<PieceColor> = mutableStateOf(PieceColor.WHITE),
    var view: MutableState<Boolean> = mutableStateOf(true)
) {
    fun checkTurn() = turn.value == PieceColor.WHITE

    fun endTurn() {
        turn.value = if (checkTurn()) PieceColor.BLACK else PieceColor.WHITE
    }

    fun flipView() {
        view.value = !view.value
    }
}