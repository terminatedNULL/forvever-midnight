package engine.thirdparty.sprite_view.domain

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

/**
 * This class is created to represent various screen dimensions in
 * four categories. That way we can pass four different sprite sheets
 * where small ones should be used on smaller, and large ones on larger screens.
 *
 * If you don't want to create a sprite sheet/image with different dimensions,
 * that's also okay. [SpriteSpec] accepts multiple optional parameters, and if
 * you don't want to use them, you can pass only 'default' [SpriteSheet], and that
 * way only use a single sprite sheet/image in your project.
 *
 * @property Small Reserved for smaller mobile devices from 0dp to 360dp in width.
 * @property Normal Reserved for normal mobile devices from 360dp to 600dp in width.
 * @property Large Reserved for larger mobile devices from 600dp to 800dp in width.
 * @property Tablet Reserved for tablet devices from more then 800dp in width.
 * */
enum class ScreenCategory {
    Small,
    Normal,
    Large,
    Tablet
}