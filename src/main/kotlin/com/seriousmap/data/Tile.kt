package com.seriousmap.data
import CornerTile
import EdgeTile
import RoomTile
import com.seriousmap.map.DungeonMap
import com.seriousmap.utils.Vec2i

abstract class Tile(val position: Vec2i) {
    var tileType: TileType? = null

    abstract fun updateTileData(map: DungeonMap)
    abstract fun draw(angle: Float)
    fun getRenderPos(): Vec2i = position * 10 + 8

    companion object {
        fun makeTile(position: Vec2i): Tile {
            return when (position % 2) {
                Vec2i(0, 0) -> RoomTile(position)
                Vec2i(1, 0) -> EdgeTile(position, EdgeTile.Orientation.VERTICAL)
                Vec2i(0, 1) -> EdgeTile(position, EdgeTile.Orientation.HORIZONTAL)
                Vec2i(1, 1) -> CornerTile(position)
                else -> null
            }!!
        }
    }
}