package scenes

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engine.data.Game
import engine.ui.PxButton
import engine.classes.AnimatedStar
import engine.classes.ComposableUUIDBlock
import engine.classes.Star
import engine.classes.generateStar
import engine.controllers.InputController
import engine.thirdparty.quarks.CreateParticles
import engine.thirdparty.quarks.particle.*
import engine.utils.rawArray
import engine.utils.rawStr
import engine.utils.with
import kotlinx.coroutines.delay
import popupWindowModifier

@Composable
fun TitleScene() {
    val starObjects = remember { mutableStateListOf<Star>() }
    val particlePositions = remember { mutableStateListOf<Offset>() }
    var screenSize: IntSize

    // Setup game resources and register inputs
    Game.sceneController.setIntermediate {
        setupGlobalResources()
        registerGlobalInputs()
    }

    val btnLambdas: Array<() -> Unit> = arrayOf(
        { Game.sceneController.loadScene({ StartScene() }) },
        { Game.sceneController.loadScene({ OptionsScene() }) },
        { Game.sceneController.loadScene({ CreditsScene() }) },
        { Game.exit() }
    )

    Game.inputController.registerMouseEvent(
        InputController.MouseEvent.PRESS, false,
        ComposableUUIDBlock { input ->
            particlePositions.add(input.offset!!)
        }
    )

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Load screen size for star generation
        with(LocalDensity.current) {
            screenSize = IntSize(maxWidth.toPx().toInt(), maxHeight.toPx().toInt())
        }

        // Generate background stars
        LaunchedEffect(Unit) {
            while (true) {
                repeat((3..5).random()) {
                    generateStar(screenSize, starObjects)
                }
                delay(100)
            }
        }

        particlePositions.forEach {  pos ->
            CreateParticles(
                particleSize = ParticleSize.ConstantSize(4f),
                particleColor = ParticleColor.SingleColor(Color.White),
                emissionType = EmissionType.ExplodeEmission(numberOfParticles = 5),
                x = pos.x,
                y = pos.y,
                lifeTime = LifeTime(agingFactor = 2.5f)
            )
        }

        // Display background stars
        starObjects.forEach { obj ->
            AnimatedStar(obj = obj, onEnd = { obj.expired = true })
        }

        // Clean finished stars
        LaunchedEffect(starObjects) {
            delay(100)
            starObjects.removeAll { it.expired }
        }

        Box (
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier = Modifier.fillMaxHeight(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val animatedTitle = Game.regionStrings.rawStr("mainTitle").toCharArray()
                val transition = rememberInfiniteTransition()

                Row {
                    animatedTitle.forEachIndexed { index, char ->
                        val randomDuration = (1500..3000).random()

                        val yOffset by transition.animateFloat(
                            initialValue = 0f,
                            targetValue = 10f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(durationMillis = randomDuration, delayMillis = index * 100),
                                repeatMode = RepeatMode.Reverse
                            )
                        )

                        Text(
                            modifier = Modifier
                                .offset(y = yOffset.dp)
                                .padding(5.dp),
                            text = char.toString(),
                            color = Color.White,
                            style = MaterialTheme.typography.h3
                                .with(fontWeight = FontWeight.Bold, letterSpacing = 5.sp)
                        )
                    }
                }
                
                Spacer(Modifier.height(100.dp))

                // Generate buttons
                Game.regionStrings.rawArray("mainTitleOpts").forEachIndexed { index, str ->
                    PxButton(
                        text = str,
                        onClick = btnLambdas[index]
                    )

                    Spacer(Modifier.height(20.dp))
                }
            }
        }
    }
}

/**
 * Registers all the game control bindings
 */
fun registerGlobalInputs() {
    Game.inputController.registerKeyEvent(Key.W, true, ComposableUUIDBlock {
        println("W key pressed!")
    })

    Game.inputController.registerKeyEvent(Key.A, true, ComposableUUIDBlock {
        println("A key pressed!")
    })

    Game.inputController.registerKeyEvent(Key.S, true, ComposableUUIDBlock {
        println("S key pressed!")
    })

    Game.inputController.registerKeyEvent(Key.D, true, ComposableUUIDBlock {
        println("D key pressed!")
    })

    Game.inputController.registerKeyEvent(Key.Escape, true, ComposableUUIDBlock {
        Game.popupController.showPopup(Game.Popups.PauseMenu)
    })
}

fun setupGlobalResources() {
    Game.popupController.registerPopup(
        Game.Popups.PauseMenu,
        (Game.screenSize.width / 2).dp, (Game.screenSize.height / 2).dp,
        (Game.screenSize.width / 4).dp, (Game.screenSize.height / 4).dp,
        popupWindowModifier, ComposableUUIDBlock {
            Text("Pause Menu")
        }
    )
}