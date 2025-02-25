package engine.thirdparty.quarks.emitters

import androidx.compose.ui.graphics.drawscope.DrawScope
import engine.thirdparty.quarks.core.Vector2D
import engine.thirdparty.quarks.particle.Particle
import engine.thirdparty.quarks.particle.ParticleConfigData
import engine.thirdparty.quarks.particle.createAccelerationVector
import engine.thirdparty.quarks.particle.createVelocityVector
import engine.thirdparty.quarks.particle.getExactColor
import engine.thirdparty.quarks.particle.getExactSize

internal abstract class Emitter(
    private val particleConfigData: ParticleConfigData
) {

    val particlePool = mutableListOf<Particle>()

    abstract fun generateParticles(numberOfParticles: Int)

    fun addParticle() {
        val particle = createFreshParticle()
        particlePool.add(particle)
    }

    private fun createFreshParticle(): Particle {
        return Particle(
            initialX = particleConfigData.x,
            initialY = particleConfigData.y,
            color = particleConfigData.particleColor.getExactColor(),
            size = particleConfigData.particleSize.getExactSize(),
            velocity = particleConfigData.velocity.createVelocityVector(),
            acceleration = particleConfigData.acceleration.createAccelerationVector(),
            lifetime = particleConfigData.lifeTime.maxLife,
            agingFactor = particleConfigData.lifeTime.agingFactor,
        )
    }

    fun applyForce(force: Vector2D) {
        for (particle in particlePool) {
            particle.applyForce(force)
        }
    }

    abstract fun update(dt: Float)

    fun render(drawScope: DrawScope) {
        for (particle in particlePool) {
            particle.show(drawScope)
        }
    }
}
