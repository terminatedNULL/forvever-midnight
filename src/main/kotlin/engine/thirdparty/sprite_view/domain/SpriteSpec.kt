package engine.thirdparty.sprite_view.domain

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

import androidx.compose.runtime.Composable
import engine.thirdparty.sprite_view.util.parseCategory

/**
 * Represents a specification for sprite sheet, providing different [SpriteSheet] for various [ScreenCategory].
 * This allows for responsive sprite selection based on the screen width, with a default sprite sheet as a fallback.
 *
 * @property screenWidth A value which is used to correctly calculate which [SpriteSheet] to display.
 * @property default The default [SpriteSheet] used if no other screen-specific version is available.
 * @property small An optional [SpriteSheet] tailored for small screen sizes.
 * @property normal An optional [SpriteSheet] tailored for normal screen sizes.
 * @property large An optional [SpriteSheet] tailored for large screen sizes.
 * @property tablet An optional [SpriteSheet] tailored for tablet-sized screens.
 *
 * @see ScreenCategory
 */
data class SpriteSpec(
    val screenWidth: Float,
    val default: SpriteSheet,
    val small: SpriteSheet? = null,
    val normal: SpriteSheet? = null,
    val large: SpriteSheet? = null,
    val tablet: SpriteSheet? = null
) {
    /**
     * One of the four screen categories that are calculated based on the current screen width.
     * */
    val screenCategory = screenWidth.parseCategory()
    /**
     * [SpriteSheet] selected based on the current screen width.
     * Falls back to the `default` SpriteSheet if no screen-specific SpriteSheet is provided.
     */
    val spriteSheet =
        if (screenCategory == ScreenCategory.Small && small != null) small
        else if (screenCategory == ScreenCategory.Normal && normal != null) normal
        else if (screenCategory == ScreenCategory.Large && large != null) large
        else if (screenCategory == ScreenCategory.Tablet && tablet != null) tablet
        else default

    /**
     * ImageBitmap derived from the selected [spriteSheet].
     * Uses the `default` SpriteSheet if no screen-specific SpriteSheet is available.
     */
    val imageBitmap @Composable get() = spriteSheet.image
}