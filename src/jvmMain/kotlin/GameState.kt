import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import piece.PieceColor

data class GameState(
    var turn: MutableState<PieceColor> = mutableStateOf(PieceColor.WHITE),
    var view: MutableState<Boolean> = mutableStateOf(true)
) {
    fun flipView() {
        view.value = !view.value
    }
}