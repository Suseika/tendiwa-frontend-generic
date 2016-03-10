package org.tendiwa.frontend.generic

import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.time.Activity
import org.tendiwa.backend.time.ActivityProcess
import org.tendiwa.backend.time.ActivityResult
import org.tendiwa.backend.time.Actor

internal class PlayerActor : Actor<Reality> {
    companion object {
        val failingActivity = Activity(
            listOf(
                ActivityProcess(
                    0,
                    ActivityResult {
                        throw IllegalStateException(
                            "Actual activity was not created"
                        )
                    }
                )
            )
        )
    }

    private val frontendLock = Object()

    internal var activity: Activity = failingActivity

    override fun act(context: Reality): Activity {
        synchronized(frontendLock, {
            frontendLock.wait()
            return activity
        })
    }

    fun chooseNextActivity(activity: Activity) {
        this.activity = activity
        synchronized(frontendLock, {
            frontendLock.notify()
        })
    }

}
