package engine.controllers

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.DpSize
import engine.classes.ComposableUUIDBlock

/**
 * TODO : Add z-index or priority control, add null checking!!!!
 */
class PopupController {
    private val _popupBlocks: MutableMap<Int, ComposableUUIDBlock<Unit>> = mutableMapOf()
    private val _popupData: MutableMap<Int, Triple<DpSize, DpOffset, Modifier>> = mutableMapOf()
    private val _activePopups = mutableStateListOf<Int>()

    fun registerPopup(
        key: Enum<*>,
        width: Dp, height: Dp,
        x: Dp, y: Dp,
        windowModifier: Modifier = Modifier,
        block: ComposableUUIDBlock<Unit>
    ) {
        _popupBlocks[key.ordinal] = block
        _popupData[key.ordinal] = Triple(DpSize(width, height), DpOffset(x, y), windowModifier)
    }

    fun showPopup(key: Enum<*>) {
        showPopup(key.ordinal)
    }

    fun showPopup(key: Int) {
        _activePopups.add(key)
    }

    @Composable
    fun popupWrapper(block: @Composable () -> Unit) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            block()

            _activePopups.forEach { key ->
                val currPopup = _popupBlocks[key]
                val currData = _popupData[key]

                if (currPopup != null) {
                    Box(
                        Modifier
                            .size(currData!!.first)
                            .offset(currData.second.x, currData.second.y)
                            .then(currData.third)
                    ) {
                        currPopup.invoke(Unit)
                    }
                }
            }
        }

    }
}