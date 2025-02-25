package engine.thirdparty.sprite_view.domain

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

import androidx.compose.ui.graphics.ImageBitmap

/**
 * A sprite sheet is a single image file that contains multiple smaller images (or "frames")
 * arranged in a grid. Each frame represents a different stage or part
 * of an animation, often used in 2D video games and animations.
 * By displaying frames in a sequence, a sprite sheet creates the illusion
 * of movement or changes in appearance.
 *
 * Usually you want to fill in the information about your previously exported sheet that contains
 * all frames together. Like:
 *```
 * -------------
 * | 1 | 2 | 3 |
 * | 4 | 5 | 6 |
 * | 7 | 8 | 9 |
 * -------------
 *```
 * The above graph represents an image that consist of 9 different
 * sprite frames. It has 3 columns and three rows of image frames.
 * Sprite animation starts from the frame 1.
 * @param frameWidth The width of a single frame within the sprite sheet,
 *  * represented in px.
 * @param frameHeight The height of a single frame within the sprite sheet,
 * represented in px.
 * @param image The actual PNG/JPG image resources, that you have added
 * to your project's common 'composeResource' directory.
 * */
data class SpriteSheet(
    val frameWidth: Int,
    val frameHeight: Int,
    val image: ImageBitmap
)