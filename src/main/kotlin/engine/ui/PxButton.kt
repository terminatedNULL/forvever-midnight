package engine.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engine.controllers.Sound
import engine.data.Game
import engine.utils.squareEdgeBorder
import kotlinx.coroutines.delay
import pixelMPlus12

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun PxButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    idleColor: Color = Color.White,
    hoverColor: Color = Color.LightGray,
    clickColor: Color = Color.Gray
) {
    var isHovered by remember { mutableStateOf(false) }
    var isClicked by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isHovered) if (isClicked) 1.07f else 1.1f else 1f,
        animationSpec = tween(durationMillis = 200)
    )

    val textColor by rememberUpdatedState(if (isClicked) clickColor else if (isHovered) hoverColor else idleColor)

    Box(
        modifier = modifier
            .size(200.dp, 50.dp)
            .squareEdgeBorder(3.dp, Color.White)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        Game.audioController.playSound(Sound.ButtonClick)
                        isClicked = true
                        onClick()
                        tryAwaitRelease()
                        isClicked = false
                    }
                )
            }
            .pointerMoveFilter(
                onEnter = { isHovered = true; true },
                onExit = { isHovered = false; true }
            ),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.button,
        )
    }
}