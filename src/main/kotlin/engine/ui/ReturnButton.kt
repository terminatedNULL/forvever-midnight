package engine.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import engine.controllers.Sound
import engine.data.Game
import engine.utils.squareEdgeBorder
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun ReturnButton (resourcePath: String, scene: @Composable () -> Unit) {
    IconButton (
        modifier = Modifier
            .squareEdgeBorder(3.dp, Color.White)
            .size(36.dp, 36.dp),
        onClick = {
            GlobalScope.launch {
                Game.audioController.playSound(Sound.ButtonClick)
                Game.sceneController.loadScene(scene)
            }
        },
    ) {
        PxImage(
            resourcePath,
            modifier = Modifier.padding(10.dp),
        )
    }
}