package org.tendiwa.frontend.generic

import org.tendiwa.backend.existence.AbstractAspect
import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.Voxel
import org.tendiwa.backend.space.aspects.position
import org.tendiwa.backend.time.Activity
import org.tendiwa.backend.time.ActivityProcess
import org.tendiwa.backend.time.ActivityResult
import org.tendiwa.backend.time.TimeStream

class PlayerVolition(val reality: Reality) : AbstractAspect() {
    private val actor: PlayerActor = PlayerActor()

    fun sendActivity(activity: Activity) {
        actor.chooseNextActivity(activity)
    }

    fun addActorTo(timeStream: TimeStream<Reality>) {
        timeStream.addActor(actor)
    }
}

fun PlayerVolition.move(x: Int, y: Int) {
    val position = host.position
    sendActivity(
        Activity(
            listOf(
                ActivityProcess(1, ActivityResult {
                    position.move(reality, Voxel(x, y, position.voxel.z))
                })
            )
        )
    )
}
