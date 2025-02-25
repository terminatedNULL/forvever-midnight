package engine.data

import kotlinx.serialization.*

@Serializable
data class ConfigData(
    val region: String
)