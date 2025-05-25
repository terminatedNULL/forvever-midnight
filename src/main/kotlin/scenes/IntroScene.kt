package scenes

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Language
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import engine.controllers.Sound
import engine.data.ConfigData
import engine.data.Regions.regionStrings
import engine.data.Game
import engine.data.Regions.regionPrefixes
import engine.utils.squareEdgeBorder
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

@Composable
fun IntroScene() {
    var logoVis by remember { mutableStateOf(false) }
    var regionVis by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedRegion by remember { mutableStateOf("") }

    var loadTitle = false

    LaunchedEffect(Unit) {
        delay(500)
        logoVis = true
        delay(1500)

        Game.audioController.preloadSounds(Sound.ButtonClick)

        // Load config file (if exists)
        if (File("config.json").exists()) {
            try {
                val configTxt = File("config.json").readText()
                val dataClasses = Json.decodeFromString<ConfigData>(configTxt)

                Game.regionPrefix = dataClasses.region
                loadTitle = true

                Game.sceneController.setIntermediate {
                    println("B")
                    Game.audioController.preloadSounds(Sound.ButtonClick)

                    try {
                        val stream = javaClass.classLoader
                            .getResourceAsStream("strings/${Game.regionPrefix}-Strings.json")
                        val reader = BufferedReader(InputStreamReader(stream!!))

                        Game.regionStrings = Json.parseToJsonElement(reader.readText()) as JsonObject
                    } catch (e: Exception) {
                        // TODO : Log error here
                        println("Failed to load ${Game.regionPrefix} strings file!\n${e.localizedMessage}")
                    }
                }
            } catch (e: Exception) {
                // TODO : Log error here
                println("Failed to load config file!")
            }
        }

        logoVis = false
        delay(500)
        if (loadTitle) { Game.sceneController.loadScene({ TitleScene() })  }

        // Region selection
        if (Game.regionPrefix == "") {
            regionVis = true

            // Set intermediate config loading
            Game.sceneController.setIntermediate {
                println("C")
                Game.audioController.preloadSounds(Sound.ButtonClick)

                if (!loadTitle) {
                    File("config.json").createNewFile()

                    val jsonObj = buildJsonObject {
                        put("region", regionPrefixes[regionStrings.indexOf(selectedRegion)])
                    }

                    File("config.json").writeText(jsonObj.toString())
                }

                try {
                    val stream = javaClass.classLoader
                        .getResourceAsStream(
                            "strings/${regionPrefixes[regionStrings.indexOf(selectedRegion)]}-Strings.json"
                        )
                    val reader = BufferedReader(InputStreamReader(stream!!))

                    Game.regionStrings = Json.parseToJsonElement(reader.readText()) as JsonObject
                } catch (e: Exception) {
                    // TODO : Log error here
                    println("Failed to load ${regionPrefixes[regionStrings.indexOf(selectedRegion)]} strings file!\n${e.localizedMessage}")
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = logoVis,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Image(
                painter = painterResource("drawable/dev_logo.png"),
                contentDescription = "TerminatedNULL Developer Logo",
            )
        }

        AnimatedVisibility(
            visible = regionVis,
            enter = fadeIn(tween(500)),
            exit = fadeOut(tween(500))
        ) {
            Column {
                Row {
                    IconButton (
                        modifier = Modifier
                            .width(150.dp)
                            .squareEdgeBorder(3.dp, Color.White),
                        onClick = {
                            Game.audioController.playSound(Sound.ButtonClick)
                            expanded = true
                        },
                    ) {
                        Row (
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Outlined.Language,
                                "Region Selection Button",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(vertical = 5.dp)
                                    .padding(start = 5.dp)
                                    .weight(0.25f),
                            )

                            Text(
                                selectedRegion.ifBlank { "Language" },
                                modifier = Modifier
                                    .padding(vertical = 5.dp, horizontal = 7.dp)
                                    .weight(0.75f),
                                style = MaterialTheme.typography.button
                            )
                        }
                    }

                    Spacer(modifier = Modifier.size(10.dp))

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .width(150.dp)
                            .background(Color.Black, shape = RectangleShape)
                            .squareEdgeBorder(3.dp, Color.White),
                    ) {
                        regionStrings.forEach { region ->
                            DropdownMenuItem(
                                modifier = Modifier.width(100.dp),
                                onClick = {
                                    Game.audioController.playSound(Sound.ButtonClick)
                                    selectedRegion = region
                                    expanded = false
                                }
                            ) {
                                Text(region, style = MaterialTheme.typography.body1)
                            }
                        }
                    }

                    IconButton (
                        modifier = Modifier
                            .squareEdgeBorder(3.dp, Color.White),
                        onClick = {
                            Game.audioController.playSound(Sound.ButtonClick)
                            Game.sceneController.loadScene({ TitleScene() })
                        }
                    ) {
                        Icon(
                            Icons.Outlined.Check,
                            "Region Confirmation Button",
                            tint = Color.White,
                            modifier = Modifier
                                .padding(5.dp)
                        )
                    }
                }
            }
        }
    }
}