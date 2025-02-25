package engine.thirdparty.sprite_view.domain

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * Manages the state of a sprite sheet animation by controlling the frame transitions and animation timing.
 *
 * The animation runs within a coroutine scope on the default dispatcher. It loops through the frames
 * at each interval specified by `animationSpeed` and resets when `stop()` is called.
 *
 * @param totalFrames The total number of frames in the sprite sheet. Determines the number of frames
 * that the animation cycles through.
 * @param framesPerRow The number of frames that are included in the sprite sheet, per row.
 * @param animationSpeed Controlling the speed of the animation.
 *
 * @property currentFrame A [StateFlow] that emits the current frame index. Observers can use this
 * property to get the current frame of the animation.
 * @property isRunning A [StateFlow] that emits the running state of the animation, indicating
 * whether the animation is actively running or stopped.
 *
 * @constructor Initializes the `SpriteState` with a specified number of frames and animation speed.
 */
class SpriteState(
    private val totalFrames: Int,
    internal val framesPerRow: Int,
    private val animationSpeed: Long
) {
    private val _currentFrame = MutableStateFlow(value = 0)
    val currentFrame: StateFlow<Int> get() = _currentFrame

    private var _isRunning = MutableStateFlow(false)
    val isRunning: StateFlow<Boolean> = _isRunning

    private val scope = CoroutineScope(
        context = Dispatchers.Default + SupervisorJob()
    )

    init {
        scope.launch {
            _isRunning.collect { running ->
                if (running) {
                    // Animating sprite images
                    while (_isRunning.value) {
                        delay(animationSpeed)
                        _currentFrame.value = (_currentFrame.value + 1) % totalFrames
                    }
                } else {
                    // Reset the sprite frame to it's initial position
                    _currentFrame.value = 0
                }
            }
        }
    }

    /**
     * Starts the sprite animation by setting the `isRunning` state to `true`, causing
     * frames to update based on the specified animation speed.
     * */
    fun start() {
        _isRunning.value = true
    }

    /**
     * Stops the sprite animation and resets the current frame to the initial frame.
     * */
    fun stop() {
        _isRunning.value = false
    }

    /**
     * Cancels the coroutine scope used by this class, releasing any resources and stopping
     * any ongoing animations.
     * */
    fun cleanup() {
        scope.cancel()
    }
}

@Composable
fun rememberSpriteState(
    totalFrames: Int,
    framesPerRow: Int,
    animationSpeed: Long = 50L
): SpriteState {
    return remember {
        SpriteState(
            totalFrames,
            framesPerRow,
            animationSpeed
        )
    }
}