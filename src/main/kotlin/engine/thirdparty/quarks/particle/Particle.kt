package engine.thirdparty.quarks.particle

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import engine.thirdparty.quarks.core.Vector2D
import engine.thirdparty.quarks.core.add
import engine.thirdparty.quarks.core.roundTo
import engine.thirdparty.quarks.core.scalarMultiply

internal class Particle constructor(
    var initialX: Float = 0f, var initialY: Float = 0f,
    val color: Color = Color.Yellow,
    var size: Float = 25f,
    var velocity: Vector2D = Vector2D(0f, 0f),
    var acceleration: Vector2D = Vector2D(0f, 0f),
    var lifetime: Float = 255f,
    var agingFactor: Float = 20f
) : Vector2D(initialX, initialY) {

    private val originalLife = lifetime
    private var alpha = 1f

    fun finished(): Boolean = this.lifetime < 0

    fun applyForce(force: Vector2D) {
        this.acceleration.add(force)
    }

    fun update(dt: Float) {
        lifetime -= agingFactor

        if (lifetime >= 0) {
            this.alpha = (lifetime / originalLife).roundTo(3)
        }

        // Add acceleration to velocity vector
        this.velocity.add(acceleration)

        // add velocity vector to positions
        this.add(velocity, scalar = dt)

        //set acceleration back to 0
        this.acceleration.scalarMultiply(0f)
    }

    fun show(drawScope: DrawScope) {
        drawScope.drawRect(
            color = color,
            size = Size(size, size),
            topLeft = Offset(x, y),
            alpha = alpha
        )
    }
}
