package org.tendiwa.frontend.generic

import org.tendiwa.backend.space.Reality
import org.tendiwa.time.Activity
import org.tendiwa.time.ActivityProcess
import org.tendiwa.time.ActivityResult
import org.tendiwa.time.Actor

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
