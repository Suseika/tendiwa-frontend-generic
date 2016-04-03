package org.tendiwa.frontend.generic

import org.tendiwa.backend.contains
import org.tendiwa.backend.existence.AbstractAspect
import org.tendiwa.backend.existence.Stimulus
import org.tendiwa.backend.existence.aspect
import org.tendiwa.backend.existence.temporalActors
import org.tendiwa.backend.space.Reality
import org.tendiwa.backend.space.TimeBubble
import org.tendiwa.backend.space.aspects.Position
import org.tendiwa.backend.space.chunks.*
import org.tendiwa.plane.grid.masks.BoundedGridMask
import org.tendiwa.plane.grid.masks.StringGridMask

class TimeBubbleOwnership(
    private val bubbleShape: BoundedGridMask = DEFAULT_BUBBLE_SHAPE
) : AbstractAspect() {
    companion object {
        val DEFAULT_BUBBLE_SHAPE =
            StringGridMask(
                ".###.",
                "#####",
                "#####",
                "#####",
                ".###."
            )
    }

    private lateinit var bubble: TimeBubble

    private lateinit var anchor: ChunkCoordinate

    override val stimuli: List<Class<out Stimulus>>
        get() = listOf(Position.Change::class.java)

    override fun init(reality: Reality) {
        anchor = host.aspect<Position>().voxel.chunkCoordinate
        bubble = TimeBubble(
            reality,
            chunksAround(anchor, reality)
        )
    }

    override fun reaction(reality: Reality, stimulus: Stimulus) {
        when {
            (stimulus is Position.Change && stimulus.host == host) -> {
                if (!anchor.contains(stimulus.new)) {
                    val newChunkCoordinate = stimulus.new.chunkCoordinate
                    val newMask = chunksAround(newChunkCoordinate, reality)
                    bubble.relocate(newMask, host.temporalActors())
                    anchor = newChunkCoordinate
                }
            }
        }
    }

    fun runnable(): Runnable =
        bubble.runnable()

    fun chunksAround(chunkCoordinate: ChunkCoordinate, reality: Reality): ChunkMask =
        ChunkMask(
            start = chunkCoordinate.moveByChunks(
                -(bubbleShape.hull.width - 1) / 2,
                -(bubbleShape.hull.height - 1) / 2
            ),
            levels = listOf(bubbleShape)
        )
            .let {
                ChunkMask(
                    it.chunkCoordinates
                        .filter { reality.space.hull.contains (it.voxel) }
                        .toSet()
                )
            }
}
