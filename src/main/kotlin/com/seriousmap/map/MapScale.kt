package com.seriousmap.map

import com.seriousmap.utils.Vec2i
import com.seriousmap.utils.minus
import net.minecraft.world.storage.MapData

data class MapScale(
    val start: Vec2i,
    val roomSize: Int,
    val roomCount: Vec2i,
) {
    fun getTileCorner(tile: Vec2i) = start + tile.floorDiv(2) * (roomSize + 4) + (tile % 2) * roomSize
    fun getTileCenter(tile: Vec2i) = start + roomSize.floorDiv(2) + tile * (roomSize + 4).floorDiv(2)

    companion object {
        const val MAP_SIZE = 128
        const val MAP_PADDING = 5

        fun fromMapData(mapData: MapData): MapScale {
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
                if (firstGreenIndex != -1
                    && lastGreenIndex == -1
                    && elem != 30.toByte()
                ) lastGreenIndex = i
            }

            val roomSize = lastGreenIndex - firstGreenIndex
            val start = Vec2i(
                (startIndex % MAP_SIZE - MAP_PADDING) % (roomSize + 4) + MAP_PADDING,
                (startIndex.floorDiv(MAP_SIZE) - MAP_PADDING) % (roomSize + 4) + MAP_PADDING
            )
            val roomCount = (MAP_SIZE - start).floorDiv(roomSize + 4)
            return MapScale(start, roomSize, roomCount)
        }
    }
}
