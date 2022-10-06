package com.seriousmap.map

import RoomTile
import SeriousMap.Companion.mc
import com.seriousmap.config.Config
import com.seriousmap.data.Tile
import com.seriousmap.player.DungeonPlayer
import com.seriousmap.utils.RenderUtils
import com.seriousmap.utils.Vec2i
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.world.storage.MapData
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.max
import kotlin.math.roundToInt

class DungeonMap(mapData: MapData) {
    private val mapScale: MapScale = MapScale.fromMapData(mapData)
    private val puzzlePositions = mutableListOf<Vec2i>()
    private val assignedPuzzleNames = mutableSetOf<String>()
    private val unassignedPuzzleNames = mutableSetOf<String>()
    private val tiles = mutableListOf<Tile>()
    private val players = mutableMapOf<String, DungeonPlayer>()

    var mapData: MapData = mapData
        set(newData) {
            field = newData
            updateTiles()
            updatePuzzles()
        }

    private val renderWidth: Int
        get() = mapScale.roomCount.x * 20 - 4

    private val renderHeight: Int
        get() = mapScale.roomCount.y * 20 - 4

    init {
        for (y in 0 until mapScale.roomCount.y * 2) {
            for (x in 0 until mapScale.roomCount.x * 2) {
                val pos = Vec2i(x, y)
                tiles.add(Tile.makeTile(Vec2i(x, y)))
            }
        }
    }

    override fun toString(): String {
        return tiles.joinToString(", ")
    }

    fun renderMap() {
        val angle = 180 - mc.thePlayer.rotationYaw
        val scale = ScaledResolution(mc).scaleFactor
        val borderSize = (150 * Config.borderScale).roundToInt()
        GlStateManager.pushMatrix()
        GlStateManager.translate(Config.mapX.toDouble(), Config.mapY.toDouble(), 0.0)
        RenderUtils.renderRect(
            0.0,
            0.0,
            borderSize.toDouble(),
            borderSize.toDouble(),
            Color(0.0F, 0.0F, 0.0F, 0.5F)
        )
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        GL11.glScissor(
            Config.mapX * scale,
            (mc.displayHeight - Config.mapY * scale - borderSize * scale),
            (borderSize * scale),
            (borderSize * scale)
        )
        GlStateManager.translate(borderSize / 2.0, borderSize / 2.0, 0.0)
        GlStateManager.rotate(angle, 0F, 0F, 1F)
        val scaling = borderSize.toDouble() / max(renderWidth, renderHeight) * Config.mapScale
        GlStateManager.scale(scaling, scaling, 1.0)
        GlStateManager.translate(-renderWidth / 2.0, -renderHeight / 2.0, 0.0)
        tiles.forEach { it.draw(angle) }
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        GlStateManager.popMatrix()
    }

    private fun getColor(mapPosition: Vec2i): Byte? =
        mapData.colors.getOrNull(mapPosition.x + mapPosition.y * MapScale.MAP_SIZE)

    private fun updateTiles() {
        tiles.forEach { it.updateTileData(this) }
    }

    fun addPuzzlePosition(pos: Vec2i) {
        puzzlePositions.add(pos)
    }

    fun addPuzzleNames(names: Set<String>) {
        unassignedPuzzleNames.addAll(names subtract assignedPuzzleNames)
    }

    fun updatePuzzles() {
        val puzzleTile = tiles.find { it.position == puzzlePositions.singleOrNull() } as? RoomTile
        puzzleTile?.apply {
            unassignedPuzzleNames.singleOrNull()?.let {
                state.name = it
                assignedPuzzleNames.add(it)
                puzzlePositions.clear()
                unassignedPuzzleNames.clear()
            }
        }
        if (puzzlePositions.size > 1 || unassignedPuzzleNames.size > 1) {
            assignedPuzzleNames.addAll(unassignedPuzzleNames)
            puzzlePositions.clear()
            unassignedPuzzleNames.clear()
        }
    }

    fun getCorner(tilePosition: Vec2i): Byte? = getColor(mapScale.getTileCorner(tilePosition))
    fun getCenter(tilePosition: Vec2i): Byte? = getColor(mapScale.getTileCenter(tilePosition))

    fun addPlayerData(data: List<TabScan.PlayerTabData>) {
        data.forEach {
            val dungeonPlayer = players.getOrPut(it.name) { DungeonPlayer(it.name) }
            dungeonPlayer.updateFromTab(it)
            println(dungeonPlayer)
        }
    }
}