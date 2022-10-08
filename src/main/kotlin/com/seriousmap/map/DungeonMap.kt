package com.seriousmap.map

import RoomTile
import SeriousMap.Companion.mc
import com.seriousmap.config.Config
import com.seriousmap.data.Tile
import com.seriousmap.player.DungeonPlayer
import com.seriousmap.utils.*
import gg.essential.universal.UChat
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.world.storage.MapData
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
import java.awt.Color
import kotlin.math.roundToInt

class DungeonMap(private val mapScale: MapScale, var mapData: MapData) {
    private val puzzlePositions = mutableListOf<Vec2i>()
    private val assignedPuzzleNames = mutableSetOf<String>()
    private val unassignedPuzzleNames = mutableSetOf<String>()
    private val tiles = mutableListOf<Tile>()
    private val players = mutableMapOf<String, DungeonPlayer>()

    fun update(mapData: MapData) {
        this.mapData = mapData
        updateTiles()
        val tabInfo = TabListUtils.fetchTabEntries()
        updatePlayersFromTab(TabScan.getPlayers(tabInfo))
        addPuzzleNames(TabScan.getPuzzleNames(tabInfo))
        updatePuzzles()
        updatePlayersFromMap()
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
            0.0, 0.0, borderSize.toDouble(), borderSize.toDouble(), Color(0.0F, 0.0F, 0.0F, 0.5F)
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
        val scaling = borderSize.toDouble() / (20 * 6 - 4) * Config.mapScale
        GlStateManager.scale(scaling, scaling, 1.0)
        GlStateManager.translate(-renderWidth / 2.0, -renderHeight / 2.0, 0.0)
        tiles.forEach { it.draw(angle) }
        players.values.forEach { it.draw() }
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

    fun updatePlayersFromTab(data: List<TabScan.PlayerTabData>) {
        data.forEach {
            val dungeonPlayer = players.getOrPut(it.name) { DungeonPlayer(it.name) }
            dungeonPlayer.updateFromTab(it)
        }
    }

    fun updatePlayersFromMap() {
        val decor = mapData.mapDecorations
        players.values.forEach {
            val playerVector = decor["icon-${it.mapIndex}"] ?: return@forEach
            val startCorner = mapScale.start
            val endCorner = mapScale.end
            val renderX = playerVector.mapX.mapToRange(startCorner.x..endCorner.x, 0..mapScale.renderedMapSize.x)
            val renderY = playerVector.mapY.mapToRange(startCorner.y..endCorner.y, 0..mapScale.renderedMapSize.y)
            //UChat.chat("(${playerVector.mapX}, ${playerVector.mapY}), $startCorner to $endCorner, 0 to ${mapScale.renderedMapSize}, ($renderX, $renderY)")
            it.positionMap = DungeonPlayer.PlayerPosition(renderX, renderY, playerVector.yaw)
        }
    }

    fun updatePlayersFromWorld() {
        players.values.forEach { it.positionRender = null }
        mc.theWorld?.playerEntities?.filter { it.uniqueID.version() == 4 }?.forEach {
            val player = players[it.name] ?: return@forEach
            val endCorner = -202 + mapScale.roomCount * 32
            val renderX = it.posX.mapToRange(-200..endCorner.x, 0..mapScale.renderedMapSize.x)
            val renderY = it.posZ.mapToRange(-200..endCorner.y, 0..mapScale.renderedMapSize.y)
            player.positionRender = DungeonPlayer.PlayerPosition(renderX, renderY, it.rotationYaw)
        }
    }
}