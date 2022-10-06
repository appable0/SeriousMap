package com.seriousmap.data

import com.seriousmap.config.Config
import com.seriousmap.data.Tile
import com.seriousmap.data.TileType
import com.seriousmap.map.DungeonMap
import com.seriousmap.utils.RenderUtils
import com.seriousmap.utils.Vec2i
import net.minecraft.client.renderer.GlStateManager

sealed class CornerTile : Tile() {
    object Empty : CornerTile()
    object Corner : CornerTile()

    override fun transition(corner: MapColor, center: MapColor, puzzle: String?): CornerTile {
        return if (TileType.fromColor(corner) == TileType.Room) Corner else Empty
    }
}

/*


    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)
            val cornerSize = Config.doorThickness.toDouble()
            RenderUtils.renderRect( -cornerSize / 2,-cornerSize / 2,cornerSize, cornerSize, TileType.toColor(it))
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)
        }
    }

    override fun toString(): String {
        return "CornerTile(type=$tileType)"
    }
}

 */