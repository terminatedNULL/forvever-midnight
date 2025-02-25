package engine.thirdparty.sprite_view.util

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

import engine.thirdparty.sprite_view.domain.ScreenCategory

internal fun Float.parseCategory(): ScreenCategory {
    return when (this) {
        in 0f..360f -> ScreenCategory.Small
        in 360f..600f -> ScreenCategory.Normal
        in 600f..800f -> ScreenCategory.Large
        else -> ScreenCategory.Tablet
    }
}