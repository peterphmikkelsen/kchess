import androidx.compose.ui.graphics.toPainter
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.singleWindowApplication
import game.GameView
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

fun main() = singleWindowApplication(
    title = "", state = WindowState(size = DpSize(1200.dp, 1000.dp)),
    icon = getGameIcon().toPainter()
) {
    GameView()
}

private fun getGameIcon(): BufferedImage {
    return ImageIO.read(File("src/jvmMain/resources/icon.png"))
        ?: BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB)
}
