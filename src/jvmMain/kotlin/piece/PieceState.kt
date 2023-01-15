package piece

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class PieceState(var focused: MutableState<Boolean> = mutableStateOf(false))