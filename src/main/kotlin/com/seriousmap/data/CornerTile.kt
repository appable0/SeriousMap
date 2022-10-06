import com.seriousmap.config.Config
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
            val cornerSize = Config.doorThickness.toDouble()
            RenderUtils.renderRect( -cornerSize / 2,-cornerSize / 2,cornerSize, cornerSize, TileType.toColor(it))
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)
        }
    }

    override fun toString(): String {
        return "CornerTile(type=$tileType)"
    }
}