package engine.thirdparty.quarks.emitters

import engine.thirdparty.quarks.particle.ParticleConfigData

internal class ParticleExplodeEmitter(
    numberOfParticles: Int,
    particleConfigData: ParticleConfigData
) : Emitter(particleConfigData) {

    init {
        generateParticles(numberOfParticles)
    }

    override fun generateParticles(numberOfParticles: Int) {
        repeat(numberOfParticles) { addParticle() }
    }

    override fun update(dt: Float) {
        for (particle in particlePool) {
            particle.update(dt)
        }
        particlePool.removeAll { it.finished() }
    }
}
