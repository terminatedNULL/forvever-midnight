package engine.utils

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import javax.imageio.ImageIO
import kotlin.random.Random

fun loadImageBitmap(
    resource: String
): ImageBitmap {
    return ImageIO.read(ClassLoader.getSystemResourceAsStream(resource)).toComposeImageBitmap()
}

fun randomColor(): Color {
    return Color(
        red = Random.nextFloat(),
        green = Random.nextFloat(),
        blue = Random.nextFloat(),
        alpha = 1f
    )
}

fun randomPastelColor(): Color {
    return arrayOf(
        Color(0xFFF1F1F6),
        Color(0xFFE1CCEC),
        Color(0xFFC9B6E4),
        Color(0xFFBE9FE1),
        Color(0xFFA6B1E1),
        Color(0xFFDCD6F7),
        Color(0xFFAEE2FF),
        Color(0xFFB9F3FC),
        Color(0xFFE5E0FF),
        Color(0xFFAAC4FF),
    ).random()
}