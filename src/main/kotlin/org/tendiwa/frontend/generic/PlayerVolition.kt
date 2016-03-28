package org.tendiwa.frontend.generic

import org.tendiwa.backend.existence.AbstractAspect
import org.tendiwa.backend.existence.aspect
import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.Voxel
import org.tendiwa.backend.space.aspects.Position
import org.tendiwa.backend.time.Activity
import org.tendiwa.backend.time.ActivityProcess
import org.tendiwa.backend.time.TimeStream

class PlayerVolition(val reality: Reality) : AbstractAspect() {
    private val actor: PlayerTemporalActor = PlayerTemporalActor()

    fun sendActivity(activity: Activity) {
        actor.chooseNextActivity(activity)
    }

    fun addActorTo(timeStream: TimeStream<Reality>) {
        timeStream.addActor(actor)
    }
}

fun PlayerVolition.move(x: Int, y: Int, z: Int) {
    val position = host.aspect<Position>()
    sendActivity(
        Activity(
            listOf(
                ActivityProcess(1, {
                    position.change(reality, Voxel(x, y, z))
                })
            )
        )
    )
}
