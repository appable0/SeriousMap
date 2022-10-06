package com.seriousmap.map

import com.seriousmap.utils.Vec2i
import com.seriousmap.utils.minus
import net.minecraft.world.storage.MapData

data class MapScale(
    val start: Vec2i,
    val roomSize: Int,
    val roomCount: Vec2i,
) {
    val tileCount: Int
        get() = (roomCount * 2 - 1).let { it.x * it.y }

    fun indexToTilePosition(index: Int) =
        (roomCount.x * 2 - 1).let { Vec2i(index % it, index.floorDiv(it)) }


    fun getTileCorner(tile: Vec2i) = start + tile.floorDiv(2) * (roomSize + 4) + (tile % 2) * roomSize
    fun getTileCenter(tile: Vec2i) = start + roomSize.floorDiv(2) + tile * (roomSize + 4).floorDiv(2)

    private fun validate(): Boolean = listOf(16, 18).contains(roomSize) && listOf(
        Vec2i(22, 11), Vec2i(11, 11), Vec2i(16, 5), Vec2i(5, 16), Vec2i(5, 5)
    ).contains(start)

    companion object {
        const val MAP_SIZE = 128
        const val MAP_PADDING = 5

        fun fromMap(mapData: MapData): MapScale? {
            var startIndex = -1
            var firstGreenIndex = -1
            var lastGreenIndex = -1

            mapData.colors.forEachIndexed { i, elem ->
                if (elem != 0.toByte()) {
                    if (startIndex == -1) startIndex = i
                    if (elem == 30.toByte()) {
                        if (firstGreenIndex == -1) firstGreenIndex = i
                    }
                }
                if (firstGreenIndex != -1 && lastGreenIndex == -1 && elem != 30.toByte()) lastGreenIndex = i
            }

            val roomSize = lastGreenIndex - firstGreenIndex
            val start = Vec2i(
                (startIndex % MAP_SIZE - MAP_PADDING) % (roomSize + 4) + MAP_PADDING,
                (startIndex.floorDiv(MAP_SIZE) - MAP_PADDING) % (roomSize + 4) + MAP_PADDING
            )
            val roomCount = (MAP_SIZE - start).floorDiv(roomSize + 4)
            val proposedScale = MapScale(start, roomSize, roomCount)
            return if (proposedScale.validate()) proposedScale else null
        }
    }
}
