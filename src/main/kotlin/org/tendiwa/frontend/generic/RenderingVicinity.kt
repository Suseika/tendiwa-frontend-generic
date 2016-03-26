package org.tendiwa.frontend.generic

import org.tendiwa.backend.existence.RealThing
import org.tendiwa.backend.space.Space
import org.tendiwa.backend.space.chunks.chunkWithVoxel
import org.tendiwa.backend.space.floors.FloorType
import org.tendiwa.backend.space.realThing.viewOfArea
import org.tendiwa.backend.space.walls.WallType
import org.tendiwa.plane.grid.constructors.GridRectangle
import org.tendiwa.plane.grid.dimensions.GridDimension
import org.tendiwa.plane.grid.masks.GridMask

class RenderingVicinity(
    private val space: Space,
    private val viewportSize: GridDimension,
    var boundsDepth: Int
) {
    var tileBounds = GridRectangle(viewportSize)

    fun floorAt(x: Int, y: Int): FloorType =
        space.floors.chunkWithVoxel(x, y, boundsDepth).floorAt(x, y)

    fun wallAt(x: Int, y: Int): WallType =
        space.walls.chunkWithVoxel(x, y, boundsDepth).wallAt(x, y)

    val things: List<RealThing> =
        space.realThings.planeAtZ(boundsDepth)
            .viewOfArea(tileBounds)
            .things

    var fieldOfView: GridMask = GridMask { x, y -> false }
}

/**
 * Checks if the wall at x:y is not void.
 */
fun RenderingVicinity.hasWallAt(x: Int, y: Int): Boolean =
    wallAt(x, y) != WallType.void
