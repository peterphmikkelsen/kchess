import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication

fun main() = singleWindowApplication(
    title = "Kotlin Chess", state = WindowState(size = DpSize(1000.dp, 1000.dp))
) {
    GameView()
}
