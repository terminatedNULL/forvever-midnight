package engine.controllers

import kotlinx.coroutines.*
import java.io.BufferedInputStream
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.Clip

enum class Sound(val path: String) {
    ButtonClick("/sounds/button_click.wav")
}

class AudioController {
    private val _soundCache = mutableMapOf<String, Clip>()

    suspend fun preloadSounds(vararg sounds: Sound) {
        withContext(Dispatchers.IO) {
            for (sound in sounds) {
                if (_soundCache.containsKey(sound.name)) {
                    continue
                }

                try {
                    val resourceStream = javaClass.getResourceAsStream(sound.path)
                        ?: throw IllegalArgumentException("Sound file not found: ${sound.path}")

                    val bufferedStream = BufferedInputStream(resourceStream)

                    val audioInputStream = AudioSystem.getAudioInputStream(bufferedStream)
                    val clip = AudioSystem.getClip()
                    clip.open(audioInputStream)
                    _soundCache[sound.name] = clip
                } catch (e: Exception) {
                    // TODO : Log error here
                    println("Failed to preload sound ${sound.name}\n${e.localizedMessage}")
                }
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun playSound(sound: Sound) {
        GlobalScope.launch(Dispatchers.IO) {
            val clip: Clip = _soundCache[sound.name] ?: run {
                try {
                    // Load the sound if not in the cache
                    val resourceStream = javaClass.getResourceAsStream(sound.path)
                        ?: throw IllegalArgumentException("Sound file not found: ${sound.path}")

                    val bufferedStream = BufferedInputStream(resourceStream)
                    val audioInputStream = AudioSystem.getAudioInputStream(bufferedStream)
                    val newClip = AudioSystem.getClip()
                    newClip.open(audioInputStream)

                    // Cache the clip for future use
                    _soundCache[sound.name] = newClip
                    newClip
                } catch (e: Exception) {
                    // Log error if loading fails
                    println("Failed to load sound ${sound.name}\n${e.localizedMessage}")
                    return@launch
                }
            }

            if (clip.isRunning) {
                clip.stop()
            }

            clip.flush()
            clip.framePosition = 0
            clip.start()
        }
    }

    fun clearCache() {
        if (_soundCache.isEmpty()) { return }
        for (sound in _soundCache) { sound.value.close() }
        _soundCache.clear()
    }
}