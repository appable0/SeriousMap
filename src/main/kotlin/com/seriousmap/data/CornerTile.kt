import com.seriousmap.data.Tile
import com.seriousmap.data.TileType
import com.seriousmap.map.DungeonMap
import com.seriousmap.utils.RenderUtils
import com.seriousmap.utils.Vec2i
import net.minecraft.client.renderer.GlStateManager

class CornerTile(position: Vec2i) : Tile(position) {

    override fun updateTileData(map: DungeonMap) {
        val corner = TileType.fromByte(map.getCorner(position))
        tileType = corner
    }

    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)
            RenderUtils.renderRect( -2.0,-2.0, 4.0, 4.0, TileType.toColor(it))
        }
    }

    override fun toString(): String {
        return "CornerTile(type=$tileType)"
    }
}