package scenes

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engine.data.Game
import engine.ui.ReturnButton
import engine.utils.rawStr
import engine.utils.with
import kotlinx.coroutines.DelicateCoroutinesApi

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun CreditsScene() {
    Box (
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                Game.regionStrings.rawStr("creditsTitle"),
                color = Color.White,
                style = MaterialTheme.typography.h3
                    .with(fontWeight = FontWeight.Bold, letterSpacing = 5.sp)
            )

            Spacer(Modifier.height(50.dp))

            Row(
                modifier = Modifier
                    .width(300.dp)
            ) {
                Column(
                    modifier = Modifier
                        .width(150.dp),
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(Game.regionStrings.rawStr("creditsLineA"), style = MaterialTheme.typography.button)
                    Text(Game.regionStrings.rawStr("creditsLineB"), style = MaterialTheme.typography.button)
                }

                Column(
                    modifier = Modifier
                        .width(150.dp),
                    horizontalAlignment = Alignment.End,
                ) {
                    Text("Alexander Yauchler", style = MaterialTheme.typography.body1)
                    Text("Stefan Jovanovic", style = MaterialTheme.typography.body1)
                    Text("Nikhil Chaudhari", style = MaterialTheme.typography.body1)
                }
            }

            Spacer(Modifier.height(50.dp))

            ReturnButton("drawable/return_arrow.png") { TitleScene() }
        }
    }
}