import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.material.Typography
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import engine.data.Game.stylePreferences
import engine.utils.squareEdgeBorder

enum class GameFont(val fontObj: FontFamily) {
    BACKUP(backupFont),
    M_PLUS_10(pixelMPlus10),
    M_PLUS_12(pixelMPlus12),
}

/*
 * Full Unicode Fonts
 */

val backupFont = FontFamily(
    Font(resource = "fonts/lana-pixel.ttf")
)

val pixelMPlus10 = FontFamily(
    Font(resource = "fonts/PixelMplus10-Regular.ttf", weight = FontWeight.Normal),
    Font(resource = "fonts/PixelMplus10-Bold.ttf", weight = FontWeight.Bold)
)

val pixelMPlus12 = FontFamily(
    Font(resource = "fonts/PixelMplus12-Regular.ttf", weight = FontWeight.Normal),
    Font(resource = "fonts/PixelMplus12-Bold.ttf", weight = FontWeight.Bold)
)

// Backup font is set as main typography to act as a fallback for any failed font loading
fun mainTypography(): Typography {
    return Typography(
        defaultFontFamily = backupFont,
        h1 = TextStyle(
            fontSize = 40.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        h2 = TextStyle(
            fontSize = 36.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        h3 = TextStyle(
            fontSize = 32.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        h4 = TextStyle(
            fontSize = 28.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        h5 = TextStyle(
            fontSize = 24.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        h6 = TextStyle(
            fontSize = 20.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        body1 = TextStyle(
            fontSize = 16.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        body2 = TextStyle(
            fontSize = 12.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            color = Color.White
        ),
        button = TextStyle(
            fontSize = 16.sp * stylePreferences.fontScale,
            fontFamily = pixelMPlus12,
            fontWeight = FontWeight.Bold,
            letterSpacing = (stylePreferences.fontScale).sp,
            color = Color.White,

        )
    )
}

// Modifier for popup styling
val popupWindowModifier = Modifier
    .background(Color.Black)
    .squareEdgeBorder(3.dp, Color.White)