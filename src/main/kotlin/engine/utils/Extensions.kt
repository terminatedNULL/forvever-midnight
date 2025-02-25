package engine.utils

import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.style.*
import androidx.compose.ui.unit.TextUnit
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import org.jetbrains.skia.Image
import org.jetbrains.skiko.toBitmap
import org.jetbrains.skiko.toImage
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import javax.imageio.metadata.IIOMetadataNode

fun JsonObject.rawStr(key: String): String {
    return this[key]?.jsonPrimitive?.content ?: ""
}

fun JsonObject.rawArray(key: String): Array<String> {
    return this[key]?.jsonArray?.map { it.jsonPrimitive.content }?.toTypedArray() ?: emptyArray()
}

fun BufferedImage.getTextMetadata(): HashMap<String, String> {
    val map = HashMap<String, String>()

    val reader = ImageIO.getImageReadersByMIMEType("png").next()
    reader.input = ImageIO.createImageInputStream(this)

    val metadata = reader.getImageMetadata(0)
    val tree = metadata.getAsTree("javax_imageio_1.0") as IIOMetadataNode
    val textNode = tree.getElementsByTagName("TextEntry")

    for (i in 0 until textNode.length) {
        val node = textNode.item(i) as IIOMetadataNode
        val keyword = node.getAttribute("keyword")
        val value = node.getAttribute("value")
        if (keyword.isNotEmpty() && value.isNotEmpty()) {
            map[keyword] = value
        }
    }

    return map
}

//fun BufferedImage.gridSplit(): Array<BufferedImage> {
//
//}

fun Array<BufferedImage>.toImageBitmapArray(): Array<ImageBitmap> {
    return this.map { it.toBitmap().asComposeImageBitmap() }.toTypedArray()
}

fun ImageBitmap.toImage(): Image {
    return this.toAwtImage().toImage()
}

/*
 * TextStyle helper functions for modifying preset typography styles
 */

fun TextStyle.weight(weight: FontWeight): TextStyle {
    return this.merge(TextStyle(fontWeight = weight))
}

fun TextStyle.size(size: TextUnit): TextStyle {
    return this.merge(TextStyle(fontSize = size))
}

fun TextStyle.spacing(spacing: TextUnit): TextStyle {
    return this.merge(TextStyle(letterSpacing = spacing))
}

fun TextStyle.family(family: FontFamily): TextStyle {
    return this.merge(TextStyle(fontFamily = family))
}

fun TextStyle.color(color: Color): TextStyle {
    return this.merge(TextStyle(color = color))
}

fun TextStyle.geoTransform(geoTransform: TextGeometricTransform): TextStyle {
    return this.merge(TextStyle(textGeometricTransform = geoTransform))
}

fun TextStyle.with(
    fontWeight: FontWeight? = null,
    fontSize: TextUnit? = null,
    fontFamily: FontFamily? = null,
    letterSpacing: TextUnit? = null,
    color: Color? = null,
    geoTransform: TextGeometricTransform? = null,
    featureSettings: String? = null,
    textDecoration: TextDecoration? = null,
    drawStyle: DrawStyle? = null,
    fontStyle: FontStyle? = null,
    background: Color? = null,
    baselineShift: BaselineShift? = null,
    fontSynthesis: FontSynthesis? = null,
    hyphens: Hyphens? = null,
    lineBreak: LineBreak? = null,
    lineHeight: TextUnit? = null,
    localeList: LocaleList? = null,
    platformStyle: PlatformTextStyle? = null,
    shadow: Shadow? = null,
    textAlign: TextAlign? = null,
    textDirection: TextDirection? = null,
    textIndent: TextIndent? = null,
    textMotion: TextMotion? = null,
): TextStyle {
    return this.copy(
        fontWeight = fontWeight ?: this.fontWeight,
        fontSize = fontSize ?: this.fontSize,
        fontFamily = fontFamily ?: this.fontFamily,
        letterSpacing = letterSpacing ?: this.letterSpacing,
        color = color ?: this.color,
        textGeometricTransform = geoTransform ?: this.textGeometricTransform,
        fontFeatureSettings = featureSettings ?: this.fontFeatureSettings,
        textDecoration = textDecoration ?: this.textDecoration,
        drawStyle = drawStyle ?: this.drawStyle,
        fontStyle = fontStyle ?: this.fontStyle,
        background = background ?: this.background,
        baselineShift = baselineShift ?: this.baselineShift,
        fontSynthesis = fontSynthesis ?: this.fontSynthesis,
        hyphens = hyphens ?: this.hyphens,
        lineBreak = lineBreak ?: this.lineBreak,
        lineHeight = lineHeight ?: this.lineHeight,
        localeList = localeList ?: this.localeList,
        platformStyle = platformStyle ?: this.platformStyle,
        shadow = shadow ?: this.shadow,
        textAlign = textAlign ?: this.textAlign,
        textDirection = textDirection ?: this.textDirection,
        textIndent = textIndent ?: this.textIndent,
        textMotion = textMotion ?: this.textMotion
    )
}