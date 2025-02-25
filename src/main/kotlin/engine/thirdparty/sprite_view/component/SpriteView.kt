package engine.thirdparty.sprite_view.component

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import engine.thirdparty.sprite_view.domain.SpriteFlip
import engine.thirdparty.sprite_view.domain.SpriteSpec
import engine.thirdparty.sprite_view.domain.SpriteState

/**
 * Composable function which is used to display and animate the
 * Sprite Sheet within a composable hierarchy.
 *
 * A sprite sheet is a single image file that contains multiple smaller images (or "frames")
 * arranged in a grid. Each frame represents a different stage or part
 * of an animation, often used in 2D video games and animations.
 * By displaying frames in a sequence, a sprite sheet creates the illusion
 * of movement or changes in appearance.
 *
 * @param modifier Optional modifier.
 * @param spriteState [SpriteState] manages the state of a sprite sheet animation.
 * @param spriteSpec [SpriteSpec] is used to optionally pass multiple sprite sheets with
 * different dimensions, to support various screen sizes. You can optionally include multiple
 * sprite sheets, or use only one (default).
 * @param spriteFlip Use it to flip the sprite Vertically, Horizontally or both Vertically and Horizontally.
 *
 * @see ScreenCategory
 * */
@Composable
fun SpriteView(
    modifier: Modifier = Modifier,
    spriteState: SpriteState,
    spriteSpec: SpriteSpec,
    spriteFlip: SpriteFlip? = null
) {
    val spriteImage = spriteSpec.imageBitmap
    val currentFrame = spriteState.currentFrame.collectAsState().value
    // Get the row and column position based on the current frame
    val row by rememberUpdatedState(
        newValue = currentFrame / spriteState.framesPerRow
    )
    val column by rememberUpdatedState(
        newValue = currentFrame % spriteState.framesPerRow
    )
    val frameSize by remember {
        derivedStateOf {
            IntSize(
                spriteSpec.spriteSheet.frameWidth,
                spriteSpec.spriteSheet.frameHeight
            )
        }
    }
    val frameOffset by remember {
        derivedStateOf {
            IntOffset(
                x = column * frameSize.width,
                y = row * frameSize.height
            )
        }
    }

    Canvas(
        modifier = modifier
            .width(with(LocalDensity.current) { frameSize.width.toDp() })
            .height(with(LocalDensity.current) { frameSize.height.toDp() })
    ) {
        val dstOffsetX = (size.width - frameSize.width.toFloat()) / 2
        val dstOffsetY = (size.height - frameSize.height.toFloat()) / 2

        withTransform(
            transformBlock = {
                if (spriteFlip != null) {
                    scale(
                        scaleX = when (spriteFlip) {
                            SpriteFlip.Both -> -1f
                            SpriteFlip.Horizontal -> -1f
                            else -> 1f
                        },
                        scaleY = when (spriteFlip) {
                            SpriteFlip.Both -> -1f
                            SpriteFlip.Vertical -> -1f
                            else -> 1f
                        },
                        pivot = when (spriteFlip) {
                            SpriteFlip.Horizontal -> {
                                Offset(
                                    x = dstOffsetX + frameSize.width / 2f,
                                    y = dstOffsetY
                                )
                            }
                            SpriteFlip.Vertical -> {
                                Offset(
                                    x = dstOffsetX,
                                    y = dstOffsetY + frameSize.height / 2f
                                )
                            }
                            else -> {
                                Offset(
                                    x = dstOffsetX + frameSize.width / 2f,
                                    y = dstOffsetY + frameSize.height / 2f
                                )
                            }
                        }
                    )
                }
            }
        ){
            drawImage(
                image = spriteImage,
                srcOffset = frameOffset,
                srcSize = frameSize,
                dstOffset = IntOffset(
                    dstOffsetX.toInt(),
                    dstOffsetY.toInt()
                ),
                dstSize = frameSize
            )
        }
    }
}

/**
 * Function which is used to draw a sprite sheet inside the DrawScope of the Canvas.
 *
 * @param spriteState Control the sprite sheet animation.
 * @param spriteSpec Sprite sheet specification with optional parameters for various screen dimensions.
 * @param currentFrame The current frame of the sprite sheet animation.
 * @param image Sprite sheet image that needs to be drawn on the canvas.
 * @param offset An optional offset.
 * @param spriteFlip Use it to flip the sprite Vertically, Horizontally or both Vertically and Horizontally.
 * */
fun DrawScope.drawSpriteView(
    spriteState: SpriteState,
    spriteSpec: SpriteSpec,
    currentFrame: Int,
    image: ImageBitmap,
    offset: IntOffset = IntOffset.Zero,
    spriteFlip: SpriteFlip? = null
) {
    // Get the row and column position based on the current frame
    val row = currentFrame / spriteState.framesPerRow
    val column = currentFrame % spriteState.framesPerRow

    val frameSize = IntSize(
        spriteSpec.spriteSheet.frameWidth,
        spriteSpec.spriteSheet.frameHeight
    )

    val frameOffset = IntOffset(
        x = column * frameSize.width,
        y = row * frameSize.height
    )
    withTransform(
        transformBlock = {
            if (spriteFlip != null) {
                scale(
                    scaleX = when (spriteFlip) {
                        SpriteFlip.Both -> -1f
                        SpriteFlip.Horizontal -> -1f
                        else -> 1f
                    },
                    scaleY = when (spriteFlip) {
                        SpriteFlip.Both -> -1f
                        SpriteFlip.Vertical -> -1f
                        else -> 1f
                    },
                    pivot = when (spriteFlip) {
                        SpriteFlip.Horizontal -> {
                            Offset(
                                x = offset.x + frameSize.width / 2f,
                                y = offset.y.toFloat()
                            )
                        }
                        SpriteFlip.Vertical -> {
                            Offset(
                                x = offset.x.toFloat(),
                                y = offset.y + frameSize.height / 2f
                            )
                        }
                        else -> {
                            Offset(
                                x = offset.x + frameSize.width / 2f,
                                y = offset.y + frameSize.height / 2f
                            )
                        }
                    }
                )
            }
        }
    ) {
        drawImage(
            image = image,
            srcOffset = frameOffset,
            srcSize = frameSize,
            dstOffset = offset,
            dstSize = frameSize
        )
    }
}