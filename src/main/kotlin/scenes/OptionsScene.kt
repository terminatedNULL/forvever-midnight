package scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engine.controllers.Sound
import engine.data.Game
import engine.ui.PxImage
import engine.utils.rawStr
import engine.utils.squareEdgeBorder
import engine.utils.with
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun OptionsScene() {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                Game.regionStrings.rawStr("optionsTitle"),
                style = MaterialTheme.typography.h3
                    .with(fontWeight = FontWeight.Bold, letterSpacing = 5.sp)
            )

            Spacer(Modifier.height(50.dp))

            Row (
                modifier = Modifier.width(300.dp)
                    .squareEdgeBorder(3.dp, Color.White, 25.dp),
            ) {
                Column(
                    modifier = Modifier
                        .width(150.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(Game.regionStrings.rawStr("optionsLineA"), style = MaterialTheme.typography.body1)
                }

                Column(
                    modifier = Modifier
                        .width(150.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text("?", style = MaterialTheme.typography.body1)
                }
            }

            Spacer(modifier = Modifier.size(50.dp))

            IconButton(
                modifier = Modifier
                    .squareEdgeBorder(3.dp, Color.White)
                    .size(36.dp, 36.dp),
                onClick = {
                    GlobalScope.launch {
                        Game.audioController.playSound(Sound.ButtonClick)
                        Game.sceneController.loadScene({ TitleScene() })
                    }
                }
            ) {
                PxImage(
                    "drawable/return_arrow.png",
                    modifier = Modifier.padding(10.dp),
                )
            }
        }
    }
}