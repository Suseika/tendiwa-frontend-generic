package org.tendiwa.frontend.generic

import org.tendiwa.backend.existence.AbstractAspect
import org.tendiwa.backend.existence.aspect
import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.Voxel
import org.tendiwa.backend.space.aspects.Position
import org.tendiwa.backend.time.Activity
import org.tendiwa.backend.time.ActivityProcess
import org.tendiwa.backend.time.TemporalActor

class PlayerVolition(val reality: Reality) : AbstractAspect(), TemporalActor<Reality> {

    private val actor: PlayerTemporalActor = PlayerTemporalActor()

    fun sendActivity(activity: Activity) {
        actor.chooseNextActivity(activity)
    }

    override fun act(context: Reality): Activity =
        actor.act(context)
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
