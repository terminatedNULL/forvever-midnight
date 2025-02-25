package engine.controllers

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import engine.classes.ComposableUUIDBlock
import engine.data.Game
import java.util.*
import kotlin.collections.HashMap

class InputController {
    private val _events = mutableListOf<InputEvent>()
    private var _processing = false
    private var _blockMouse = false
    private var _blockKeyboard = false
    private var _keyBlocks: HashMap<Key, ArrayList<Pair<ComposableUUIDBlock<Key>, Boolean>>> = HashMap()
    private var _mouseBlocks: EnumMap<MouseEvent, ArrayList<Pair<ComposableUUIDBlock<PointerInput>, Boolean>>> = EnumMap(MouseEvent::class.java)

    enum class MouseEvent {
        CLICK,
        DOUBLE_CLICK,
        PRESS,
        LONG_PRESS,
        DRAG,
        DRAG_START,
        DRAG_END,
        DRAG_CANCEL
    }

    data class PointerInput(
        var type: MouseEvent? = null,
        var offset: Offset? = null,
        var change: PointerInputChange? = null,
        var dragAmount: Offset? = null,
    )

    open class InputEvent
    data class MouseInput(val input: PointerInput? = null) : InputEvent()
    data class KeyboardInput(val input: Key? = null) : InputEvent()

    /**
     * An invisible wrapper for scenes that is served by the global [SceneController]
     */
    @Composable
    fun inputWrapper(block: @Composable () -> Unit) {
        val events = remember { mutableStateListOf<InputEvent>() }
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        events.forEach { event -> registerInput(event) }
        events.clear()

        Box (
            modifier = Modifier
                // Always maintain focus to capture events
                .focusRequester(focusRequester)
                .focusable()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && Game.initialized) {
                        focusRequester.requestFocus()
                    }
                }
                .pointerInput(Unit) {
                detectTapGestures (
                    onTap = { offset ->
                        events.add(MouseInput(PointerInput(type = MouseEvent.CLICK, offset = offset)))
                    },
                    onDoubleTap = { offset ->
                        events.add(MouseInput(PointerInput(type = MouseEvent.DOUBLE_CLICK, offset = offset)))
                    },
                    onLongPress = { offset ->
                        events.add(MouseInput(PointerInput(type = MouseEvent.LONG_PRESS, offset = offset)))
                    },
                    onPress = { offset ->
                        events.add(MouseInput(PointerInput(type = MouseEvent.PRESS, offset = offset)))
                    }
                )

                detectDragGestures(
                    onDrag = { change, dragAmount ->
                        events.add(MouseInput(PointerInput(type= MouseEvent.DRAG, change = change, dragAmount = dragAmount)))
                    },
                    onDragStart = { offset ->
                        events.add(MouseInput(PointerInput(type = MouseEvent.DRAG_START, offset = offset)))
                    },
                    onDragEnd = {
                        events.add(MouseInput(PointerInput(type = MouseEvent.DRAG_END)))
                    },
                    onDragCancel = {
                        events.add(MouseInput(PointerInput(type= MouseEvent.DRAG_CANCEL)))
                    }
                )
            }.onKeyEvent { keyEvent -> // TODO : Create custom class for handling more complex inputs & combinations
                events.add(KeyboardInput(keyEvent.key))
                true
            }
        ) {
            block()
        }
    }

    fun registerKeyEvent(keyInput: Key, permanent: Boolean, block: ComposableUUIDBlock<Key>) {
        if (_keyBlocks[keyInput] == null) { _keyBlocks[keyInput] = ArrayList() }
        _keyBlocks[keyInput]?.add(Pair(block, permanent))
    }

    fun registerMouseEvent(
        type: MouseEvent,
        permanent: Boolean,
        block: ComposableUUIDBlock<PointerInput>
    ) {
        if (_mouseBlocks[type] == null) { _mouseBlocks[type] = ArrayList() }
        _mouseBlocks[type]?.add(Pair(block, permanent))
    }

    fun destroyEvent(uuid: UUID) {
        _keyBlocks.forEach { entry ->
            entry.value.forEachIndexed { index, obj ->
                if (obj.first.equals(uuid)) {
                    entry.value.removeAt(index)
                }
            }
        }
    }

    fun clearEventMap() {
        _keyBlocks.forEach { entry ->
            entry.value.forEachIndexed { idx, obj ->
                if (!obj.second) { entry.value.removeAt(idx)  }
            }
        }
    }

    @Composable
    private fun registerInput(event: InputEvent) {
        if (event::class == MouseInput::class && _blockMouse) { return }
        if (event::class == KeyboardInput::class && _blockKeyboard) { return }

        _events.add(event)
        if (!_processing) {
            processEvents()
        }
    }

    @Composable
    private fun processEvents() {
        _processing = true
        while(_events.isNotEmpty()) {
            val event = _events.removeFirst()

            // TODO : Fix this nonexistent error handling!!!!
            when (event::class) {
                MouseInput::class -> {
                    if (_mouseBlocks.containsKey((event as MouseInput).input?.type)) {
                        _mouseBlocks[event.input?.type]?.forEach { it.first.invoke(event.input!!) }
                    }
                }
                KeyboardInput::class -> {
                    if (_keyBlocks.containsKey((event as KeyboardInput).input)) {
                        _keyBlocks[event.input]?.forEach { it.first.invoke(event.input!!) }
                    }

                }
            }
        }
        _processing = false
    }
}