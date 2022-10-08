import SeriousMap.Companion.mc
import com.seriousmap.config.Config
import com.seriousmap.data.Tile
import com.seriousmap.data.TileMarker
import com.seriousmap.data.TileState
import com.seriousmap.data.TileType
import com.seriousmap.map.DungeonMap
import com.seriousmap.utils.RenderUtils
import com.seriousmap.utils.Vec2i
import net.minecraft.client.renderer.GlStateManager

class RoomTile(position: Vec2i) : Tile(position) {
    val state: TileState = TileState()

    override fun updateTileData(map: DungeonMap) {
        val corner = TileType.fromByte(map.getCorner(position))
        var center = TileMarker.fromByte(map.getCenter(position))
        if ((corner == TileType.ENTRANCE && center == TileMarker.GREEN)
            || (corner == TileType.BLOOD && center == TileMarker.FAILED)
        ) {
            center = null
        }

        if (corner == TileType.PUZZLE && tileType != TileType.PUZZLE) {
            map.addPuzzlePosition(position)
        }
        tileType = corner
        state.marker = center
    }

    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)
            RenderUtils.renderRect(-8.0, -8.0, 16.0, 16.0, TileType.toColor(it))
            state.draw(angle)
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)

        }
    }

    override fun toString(): String {
        return "RoomTile(type=$tileType, marker=${state.marker}, name=${state.name})"
    }
}