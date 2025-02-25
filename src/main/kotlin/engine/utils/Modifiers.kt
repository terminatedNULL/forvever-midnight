package engine.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.squareEdgeBorder(
    borderWidth: Dp,
    borderColor: Color,
    offset: Dp = 0.dp
): Modifier = this.then(
    Modifier.drawBehind {
        val strokeWidth = borderWidth.toPx()
        val inset = strokeWidth / 2
        val offsetPx = offset.toPx()

        val width = size.width
        val height = size.height

        // Top border
        drawLine(
            color = borderColor,
            start = Offset(strokeWidth - offsetPx, inset - offsetPx),
            end = Offset(width - strokeWidth + offsetPx, inset - offsetPx),
            strokeWidth = strokeWidth
        )

        // Bottom border
        drawLine(
            color = borderColor,
            start = Offset(strokeWidth - offsetPx, height - inset + offsetPx),
            end = Offset(width - strokeWidth + offsetPx, height - inset + offsetPx),
            strokeWidth = strokeWidth
        )

        // Left border
        drawLine(
            color = borderColor,
            start = Offset(inset - offsetPx, strokeWidth - offsetPx),
            end = Offset(inset - offsetPx, height - strokeWidth + offsetPx),
            strokeWidth = strokeWidth
        )

        // Right border
        drawLine(
            color = borderColor,
            start = Offset(width - inset + offsetPx, strokeWidth - offsetPx),
            end = Offset(width - inset + offsetPx, height - strokeWidth + offsetPx),
            strokeWidth = strokeWidth
        )
    }
)