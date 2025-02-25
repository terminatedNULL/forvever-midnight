package engine.data

import androidx.compose.ui.unit.IntSize
import engine.controllers.AudioController
import engine.controllers.InputController
import engine.controllers.PopupController
import engine.controllers.SceneController
import kotlinx.serialization.json.JsonObject
import scenes.IntroScene
import java.util.concurrent.atomic.AtomicLong

object Game {
    var screenSize: IntSize = IntSize.Zero
    var exit: () -> Unit = {}
    var initialized = false

    val stylePreferences = StylePreferences()
    object UUIDGenerator {
        private val counter = AtomicLong(0L)
        fun next(): Long = counter.incrementAndGet()
    }

    val sceneController = SceneController { IntroScene() }
    val audioController = AudioController()
    val inputController = InputController()
    val popupController = PopupController()

    var regionPrefix: String = ""
    lateinit var regionStrings: JsonObject

    enum class Popups {
        PauseMenu
    }
}