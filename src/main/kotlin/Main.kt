import engine.data.Game.sceneController
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import engine.data.Game
import engine.utils.loadImageBitmap
import java.awt.Dimension
import java.awt.Toolkit
import java.io.File

@Composable
@Preview
fun App() {
    MaterialTheme (
        typography = mainTypography(),
    ) {
        sceneController.showScene()
    }
}

fun main() = application {
    val iconPath = "drawable/moon_icon.png"  // Adjust the path accordingly
    val iconPainter = try {
        BitmapPainter(loadImageBitmap(iconPath))
    } catch (e: Exception) {
        null
    }

    Window(
        onCloseRequest = ::exitApplication,
        undecorated = true,
        resizable = false,
        state = WindowState(placement = WindowPlacement.Fullscreen),
        title = "Forever Midnight v0.1.a",
        icon = iconPainter
    ) {
        Game.exit = ::exitApplication

        val screenSize: Dimension = Toolkit.getDefaultToolkit().screenSize
        Game.screenSize = IntSize(screenSize.width, screenSize.height)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {
            App()
        }
    }
}
