package engine.thirdparty.quarks

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import engine.thirdparty.quarks.emitters.ParticleExplodeEmitter
import engine.thirdparty.quarks.emitters.ParticleFlowEmitter
import engine.thirdparty.quarks.particle.Acceleration
import engine.thirdparty.quarks.particle.EmissionType
import engine.thirdparty.quarks.particle.Force
import engine.thirdparty.quarks.particle.LifeTime
import engine.thirdparty.quarks.particle.ParticleColor
import engine.thirdparty.quarks.particle.ParticleConfigData
import engine.thirdparty.quarks.particle.ParticleSize
import engine.thirdparty.quarks.particle.Velocity
import engine.thirdparty.quarks.particle.createForceVector

@Composable
fun CreateParticles(
    modifier: Modifier = Modifier,
    x: Float = 0f,
    y: Float = 0f,
    velocity: Velocity = Velocity(xDirection = 1f, yDirection = 1f),
    force: Force = Force.Gravity(0.0f),
    acceleration: Acceleration = Acceleration(0f, 0f),
    particleSize: ParticleSize = ParticleSize.ConstantSize(),
    particleColor: ParticleColor = ParticleColor.SingleColor(),
    lifeTime: LifeTime = LifeTime(255f, 1f),
    emissionType: EmissionType = EmissionType.ExplodeEmission(),
    durationMillis: Int = 10000,
) {

    val dt = remember { mutableStateOf(0f) }

    var startTime by remember { mutableStateOf(0L) }
    var previousTime by remember { mutableStateOf(System.nanoTime()) }

    val emitter = remember {
        val particleConfigData = ParticleConfigData(
            x, y, velocity, force, acceleration, particleSize, particleColor, lifeTime, emissionType
        )
        when (emissionType) {
            is EmissionType.ExplodeEmission -> {
                ParticleExplodeEmitter(emissionType.numberOfParticles, particleConfigData)
            }
            is EmissionType.FlowEmission -> {
                ParticleFlowEmitter(
                    durationMillis,
                    emissionType,
                    particleConfigData
                )
            }
        }
    }

    startTime = System.currentTimeMillis()
    LaunchedEffect(Unit) {
        val condition = if (emissionType is EmissionType.FlowEmission &&
            emissionType.maxParticlesCount == EmissionType.FlowEmission.INDEFINITE
        ) {
            true
        } else {
            System.currentTimeMillis() - startTime < durationMillis
        }
        while (condition) {
            withFrameNanos {
                dt.value = ((it - previousTime) / 1E7).toFloat()
                previousTime = it
            }
        }
    }

    Canvas(modifier) {
        emitter.render(this)
        emitter.applyForce(force.createForceVector())
        emitter.update(dt.value)
    }
}
