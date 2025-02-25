package engine.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import engine.utils.loadImageBitmap
import engine.utils.toImage
import org.jetbrains.skia.Paint
import org.jetbrains.skia.Rect

interface PxImageFill {
    var srcRect: Rect
    var dstRect: Rect

    fun calculateFill(image: ImageBitmap, size: Size)
}

class Fill(val stretch: Boolean = false) : PxImageFill {
    override lateinit var srcRect: Rect
    override lateinit var dstRect: Rect

    override fun calculateFill(image: ImageBitmap, size: Size) {
        srcRect = Rect(0f, 0f, image.width.toFloat(), image.height.toFloat())

        if (stretch) {
            dstRect = Rect(0f, 0f, size.width, size.height)
        } else {
            val aspectRatio = image.width.toFloat() / image.height.toFloat()
            val canvasAspectRatio = size.width / size.height

            if (aspectRatio > canvasAspectRatio) {
                val newWidth = size.width
                val newHeight = newWidth / aspectRatio
                dstRect = Rect(0f, (size.height - newHeight) / 2f, newWidth, (size.height + newHeight) / 2f)
            } else {
                val newHeight = size.height
                val newWidth = newHeight * aspectRatio
                dstRect = Rect((size.width - newWidth) / 2f, 0f, (size.width + newWidth) / 2f, newHeight)
            }
        }
    }
}

class Scale : PxImageFill {
    override lateinit var srcRect: Rect
    override lateinit var dstRect: Rect
    private lateinit var scaleFactor: Pair<Float, Float>

    constructor(scale: Float = 1f) {
        scaleFactor = scale to scale
    }

    constructor(scaleX: Float = 1f, scaleY: Float = 1f) {
        scaleFactor = scaleX to scaleY
    }

    override fun calculateFill(image: ImageBitmap, size: Size) {
        srcRect = Rect(0f, 0f, image.width.toFloat(), image.height.toFloat())

        val scaledWidth = image.width * scaleFactor.first
        val scaledHeight = image.height * scaleFactor.second

        val offsetX = (size.width - scaledWidth) / 2f
        val offsetY = (size.height - scaledHeight) / 2f

        dstRect = Rect(offsetX, offsetY, offsetX + scaledWidth, offsetY + scaledHeight)
    }
}

@Composable
fun PxImage (
    resourceName: String,
    modifier: Modifier = Modifier,
    fillType: PxImageFill = Fill()
) {
    val imgBmp = loadImageBitmap(resource = resourceName)

    Canvas(
        modifier = Modifier
            .background(Color.Transparent)
            .fillMaxSize()
            .then(modifier)
    ) {
        fillType.calculateFill(imgBmp, size)

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawImageRect(
                imgBmp.toImage(),
                fillType.srcRect,
                fillType.dstRect,
                Paint().apply {
                    isAntiAlias = false
                }
            )
        }
    }
}