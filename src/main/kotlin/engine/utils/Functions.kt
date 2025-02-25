package engine.utils

import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import javax.imageio.ImageIO

fun loadImageBitmap(
    resource: String
): ImageBitmap {
    return ImageIO.read(ClassLoader.getSystemResourceAsStream(resource)).toComposeImageBitmap()
}