package engine.controllers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import engine.data.Game
import kotlinx.coroutines.delay

enum class TransitionType {
    NONE,
    FADE
}

class SceneController(
    defaultScene: @Composable () -> Unit
) {
    private var _currentScene by mutableStateOf<(@Composable () -> Unit)?>(null)
    private var _nextScene by mutableStateOf<(@Composable () -> Unit)?>(null)
    private var _currentTransition by mutableStateOf(TransitionType.FADE)
    private var _isAnimating by mutableStateOf(false)
    private var _transition by mutableStateOf(false)
    private var _clearCache by mutableStateOf(true)

    private var _intermediateBlock: (suspend () -> Unit)? = null
    private var _permBlocks: ArrayList<(suspend () -> Unit)> = arrayListOf()

    // Serve default scene
    init { loadScene(defaultScene, clearSoundCache = false) }

    fun loadScene(
        scene: @Composable () -> Unit,
        transition: TransitionType = TransitionType.FADE,
        clearSoundCache: Boolean = false
    ) {
        _nextScene = scene
        _currentTransition = transition
        _transition = true
        _clearCache = clearSoundCache
    }

    @Composable
    fun showScene() {
        if (_transition) {
            when (_currentTransition) {
                TransitionType.FADE -> fadeTransition()
                TransitionType.NONE -> {
                    _transition = false
                    _currentScene = _nextScene
                    _nextScene = null

                    Game.inputController.inputWrapper {
                        Game.popupController.popupWrapper {
                            _currentScene?.invoke()
                        }
                    }
                }
            }
            return
        }

        _intermediateBlock = null
        Game.inputController.inputWrapper {
            Game.popupController.popupWrapper {
                _currentScene?.invoke()
            }
        }
    }

    @Composable
    private fun fadeTransition() {
        Game.initialized = false
        var visibleCurrentScene by remember { mutableStateOf(true) }
        var visibleNextScene by remember { mutableStateOf(false) }
        var isLoading by remember { mutableStateOf(false) }

        _isAnimating = true

        LaunchedEffect(Unit) {
            visibleCurrentScene = false
            delay(500)

            if (_intermediateBlock != null) {
                isLoading = true
                delay(500)

                _permBlocks.forEach { it.invoke() }
                _intermediateBlock!!.invoke()

                isLoading = false
                delay(1000)
            }

            _currentScene = _nextScene
            _nextScene = null

            visibleNextScene = true
            delay(500)

            _isAnimating = false
            _transition = false
        }

        val progress by rememberInfiniteTransition().animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = 1000,
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Reverse
            )
        )

        AnimatedVisibility(
            visible = isLoading,
            exit = fadeOut(tween(500)),
            enter = fadeIn(tween(500))
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.width(150.dp),
                    color = Color.White,
                    backgroundColor = Color.DarkGray,
                    progress = progress
                )
            }
        }

        AnimatedVisibility(
            visible = visibleCurrentScene,
            exit = fadeOut(tween(500))
        ) {
            _currentScene?.invoke()
        }

        if (_clearCache) { Game.audioController.clearCache() }

        AnimatedVisibility(
            visible = visibleNextScene,
            enter = fadeIn(tween(500))
        ) {
            Game.initialized = true
            _currentScene?.invoke()
        }
    }

    fun setIntermediate(permanent: Boolean = false, block: suspend () -> Unit = {}) {
        if (permanent) {
            _permBlocks.add(block)
            return
        }

        val previousBlock = _intermediateBlock

        _intermediateBlock = if (previousBlock != null) {
            { previousBlock.invoke(); block.invoke() }
        } else {
            block
        }
        return
    }
}
