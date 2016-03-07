package org.tendiwa.frontend.generic

import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.Voxel
import org.tendiwa.backend.space.aspects.position
import org.tendiwa.existence.NoReactionAspect
import org.tendiwa.existence.NoStimuliAspectKind
import org.tendiwa.time.Activity
import org.tendiwa.time.ActivityProcess
import org.tendiwa.time.ActivityResult

class PlayerVolition() : NoReactionAspect(kind) {
    companion object {
        val kind = NoStimuliAspectKind()
    }

    private val actor: PlayerActor = PlayerActor()

    fun sendActivity(context: Reality, activity: Activity) {
        actor.activity = activity
        actor.act(context)
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
