package engine.thirdparty.sprite_view.util

/*
 * Written by Stefan Jovanovic
 * https://github.com/stevdza-san/SpriteView-KMP
 */

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import engine.data.Game

@Composable
fun getScreenWidth(): Dp {
    return Game.screenSize.width.dp
}

@Composable
fun getScreenHeight(): Dp {
    return Game.screenSize.height.dp
}