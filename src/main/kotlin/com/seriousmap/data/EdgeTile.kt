import com.seriousmap.config.Config
import com.seriousmap.data.Tile
import com.seriousmap.data.TileType
import com.seriousmap.map.DungeonMap
import com.seriousmap.utils.RenderUtils
import com.seriousmap.utils.Vec2i
import com.seriousmap.utils.scale
import net.minecraft.client.renderer.GlStateManager

class EdgeTile(position: Vec2i, private val orientation: Orientation) : Tile(position) {
    var edgeType: EdgeType? = null

    override fun updateTileData(map: DungeonMap) {
        val corner = TileType.fromByte(map.getCorner(position))
        val center = TileType.fromByte(map.getCenter(position))
        
        if (corner == TileType.ROOM) {
            tileType = corner
            edgeType = EdgeType.SEPARATOR
        } else if (center != null) {
            tileType = center
            edgeType = EdgeType.DOOR
        } else {
            tileType = null
            edgeType = null
        }
    }

    override fun draw(angle: Float) {
        tileType?.let {
            val renderPos = getRenderPos()
            GlStateManager.translate(renderPos.x.toDouble(), renderPos.y.toDouble(), 0.0)

            val width = when {
                orientation == Orientation.VERTICAL -> 4
                edgeType == EdgeType.DOOR  -> Config.doorWidth
                edgeType == EdgeType.SEPARATOR -> 16
                else -> null
            }!!.toDouble()

            val height = when {
                orientation == Orientation.HORIZONTAL -> 4
                edgeType == EdgeType.DOOR  -> Config.doorWidth
                edgeType == EdgeType.SEPARATOR -> 16
                else -> null
            }!!.toDouble()

            val color = if (edgeType == EdgeType.DOOR) {
                TileType.toColor(it).scale(Config.doorDarken)
            } else TileType.toColor(it)

            RenderUtils.renderRect(-width / 2 ,-height / 2, width, height, color)
            GlStateManager.translate(-renderPos.x.toDouble(), -renderPos.y.toDouble(), 0.0)
        }
    }

    override fun toString(): String {
        return "EdgeTile(type=$tileType, orientation=${orientation}, edgeType=${edgeType})"
    }

    enum class EdgeType { DOOR, SEPARATOR }
    enum class Orientation { HORIZONTAL, VERTICAL }
}