package com.seriousmap.data

import com.seriousmap.config.Config
import com.seriousmap.utils.RenderUtils
import net.minecraft.client.renderer.GlStateManager

sealed class RoomTile : Tile() {

    object Empty : RoomTile()
    sealed class Room(val type: TileType, val marker: TileMarker?) : RoomTile()
    class Normal(type: TileType, marker: TileMarker?) : Room(type, marker)
    class Puzzle(type: TileType, marker: TileMarker?, val name: String?) : Room(type, marker)

    override fun transition(corner: MapColor?, center: MapColor?, puzzle: String?): RoomTile {
        val tileType = TileType.fromColor(corner) ?: return Empty
        return if ((this as? Normal)?.type == TileType.Puzzle && puzzle != null) {
            Puzzle(tileType, marker, puzzle)
        } else {
            val markerIsTileColor = (tileType == TileType.Entrance && center == MapColor.Green) || (tileType == TileType.Blood && center == MapColor.Red)
            val marker = if (markerIsTileColor) null else TileMarker.fromColor(center)
            Normal(tileType, marker)
        }
    }

    override fun toString(): String {
        return when (this) {
            is Empty -> "RoomTile.Empty"
            is Normal -> "RoomTile.Normal($type, $marker)"
            is Puzzle -> "RoomTile.Puzzle($type, $marker, $name)"
        }
    }
}


/*
    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            val roomSize = (20 - Config.doorThickness).toDouble()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)
            RenderUtils.renderRect(-roomSize / 2, -roomSize / 2, roomSize, roomSize, TileType.toColor(it))
            state.draw(angle, roomSize)
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)

        }
    }

    override fun toString(): String {
        return "com.seriousmap.data.RoomTile(type=$tileType, marker=${state.marker}, name=${state.name})"
    }

 */
