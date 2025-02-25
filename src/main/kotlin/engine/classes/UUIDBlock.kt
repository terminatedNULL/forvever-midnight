package engine.classes

import androidx.compose.runtime.Composable
import engine.data.Game

data class UUID(val uuid: Long, var alias: String)

class UUIDBlock(private var block: () -> Unit = {}) {
    private lateinit var uuid: UUID

    init {
        uuid = UUID(Game.UUIDGenerator.next(), this.uuid.toString())
    }

    constructor(alias: String, block: () -> Unit = {}) : this() {
        this.uuid = UUID(Game.UUIDGenerator.next(), alias)
        this.block = block
    }

    @Composable
    fun invoke() { block.invoke()  }
    fun uuid(): Long { return uuid.uuid }
    fun alias(): String { return uuid.alias }
    fun equals(uuidObj: UUID): Boolean { return uuid.uuid == uuidObj.uuid && uuid.alias == uuidObj.alias }
    fun copy(): UUID { return uuid }
}

class ComposableUUIDBlock<T>(block: @Composable (T) -> Unit) {
    private var uuid = UUID(Game.UUIDGenerator.next(), "")
    private var block: @Composable (T) -> Unit = {}

    init {
        this.block = block
        uuid.alias = uuid.uuid.toString()
    }

    constructor(alias: String, block: @Composable (T) -> Unit = {}) : this(block) {
        this.uuid.alias = alias
    }

    @Composable
    fun invoke(param: T) { block.invoke(param) }
    fun uuid(): Long { return uuid.uuid }
    fun alias(): String { return uuid.alias }
    fun equals(uuidObj: UUID): Boolean { return uuid.uuid == uuidObj.uuid && uuid.alias == uuidObj.alias }
    fun copy(): UUID { return uuid }
}