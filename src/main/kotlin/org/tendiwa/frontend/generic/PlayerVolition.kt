package org.tendiwa.frontend.generic

import org.tendiwa.backend.existence.Aspect
import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.Voxel
import org.tendiwa.backend.space.aspects.position
import org.tendiwa.backend.time.Activity
import org.tendiwa.backend.time.ActivityProcess
import org.tendiwa.backend.time.ActivityResult
import org.tendiwa.backend.time.TimeStream

class PlayerVolition() : Aspect {
    private val actor: PlayerActor = PlayerActor()

    fun sendActivity(context: Reality, activity: Activity) {
        actor.chooseNextActivity(activity)
    }

    fun addActorTo(timeStream: TimeStream<Reality>) {
        timeStream.addActor(actor)
    }
}

fun PlayerVolition.move(reality: Reality, x: Int, y: Int) {
    val position = reality.hostOf(this).position
    sendActivity(
        reality,
        Activity(
            listOf(
                ActivityProcess(1, ActivityResult {
                    position.move(reality, Voxel(x, y, position.voxel.z))
                })
            )
        )
    )
}
