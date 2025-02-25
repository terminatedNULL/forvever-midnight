package engine.controllers

import androidx.compose.ui.graphics.ImageBitmap
import engine.classes.Chunk
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import java.io.BufferedReader
import java.io.InputStreamReader

class MapController {
    private var _bgSheets: HashMap<Int, ImageBitmap> = hashMapOf()
    private var _fgSheets: HashMap<Int, ImageBitmap> = hashMapOf()
    private var _ptlSheets: HashMap<Int, ImageBitmap> = hashMapOf()

    private var _chunkNames: Array<String> = arrayOf()
    private var _chunks: Array<Chunk> = arrayOf()

    private enum class Sheet {
        BACKGROUND,
        FOREGROUND,
        PORTAL
    }

    fun loadMap(name: String) {
        val jsonData: JsonObject

        try {
            val stream = javaClass.classLoader
                .getResourceAsStream("maps/$name.json")
            val reader = BufferedReader(InputStreamReader(stream!!))
            jsonData = Json.parseToJsonElement(reader.readText()) as JsonObject
        } catch (e: Exception) {
            // TODO : Log error here
            println("Failed to load map data for '${name}'!\n${e.message}")
            return
        }

        try {
            jsonData["bgSheets"]!!.jsonArray.forEach { obj ->
                //loadSheet(obj.jsonPrimitive.content, Sheet.BACKGROUND)
            }
        } catch(e: Exception) {
            // TODO : Log error here
            println("Invalid map data in '${name}'!\n${e.message}")
        }
    }
}