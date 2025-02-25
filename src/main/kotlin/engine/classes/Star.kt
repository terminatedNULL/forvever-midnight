package engine.classes

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

enum class StarType {
    Dot
}

data class Star(
    val id: Int,
    val x: Dp,
    val y: Dp,
    val size: Dp,
    val duration: Long,
    val color: Color,
    val type: StarType,
    var expired: Boolean = false
)

@Composable
fun AnimatedStar(
    obj: Star,
    onEnd: () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    val size = remember { Animatable(0f) }

    LaunchedEffect(obj.id) {
        launch {
            alpha.animateTo(1f, animationSpec = tween(durationMillis = (obj.duration / 2).toInt()))
        }

        launch {
            size.animateTo(obj.size.value, animationSpec = tween(durationMillis = (obj.duration / 2).toInt()))
        }

        delay(obj.duration / 2)

        launch {
            alpha.animateTo(0f, animationSpec = tween(durationMillis = (obj.duration / 2).toInt()))
        }

        launch {
            size.animateTo(0f, animationSpec = tween(durationMillis = (obj.duration / 2).toInt()))
        }

        delay(obj.duration / 2)

        onEnd()
    }

    Box(
        modifier = Modifier
            .offset(x = obj.x, y = obj.y)
            .size(size.value.dp)
            .background(obj.color.copy(alpha = alpha.value)),
    )
}

fun generateStar(screenSize: IntSize, starObjects: MutableList<Star>) {
    val id = (0..10000).random()
    val x = (0..screenSize.width).random().dp
    val y = (0..screenSize.height).random().dp
    val size = (1..5).random().dp
    val duration = (2..5).random() * 1000L
    val color = if (Random.nextInt(3) != 1) Color.White
    else Color(Random.nextInt(256), Random.nextInt(256), Random.nextInt(256), 255)
    val type = StarType.entries[(0 until StarType.entries.size).random()]

    starObjects.add(Star(id, x, y, size, duration, color, type))
}