package com.seriousmap.map

import RoomTile
import SeriousMap.Companion.config
import SeriousMap.Companion.mc
import com.seriousmap.data.Tile
import com.seriousmap.player.DungeonPlayer
import com.seriousmap.utils.*
import net.minecraft.client.gui.ScaledResolution
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.entity.player.EnumPlayerModelParts
import net.minecraft.world.storage.MapData
import org.lwjgl.opengl.GL11
import java.awt.Color
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.roundToInt

class DungeonMap(private val mapScale: MapScale, var mapData: MapData) {
    private val puzzlePositions = mutableListOf<Vec2i>()
    private val assignedPuzzleNames = mutableSetOf<String>()
    private val unassignedPuzzleNames = mutableSetOf<String>()
    private val tiles = mutableListOf<Tile>()
    private val players = mutableMapOf<String, DungeonPlayer>()

    private var secretsFound = 0
    private var secretsTotal: Int? = null
    private var crypts = 0
    private var deaths = 0
    var mimicDead = false

    var clearDone = false
    var bossDone = false

    private val cryptsString: String
        get() = when {
            crypts == 0 -> "§c$crypts"
            crypts >= 5 -> "§a$crypts"
            else -> "§e$crypts"
        }

    private val deathsString: String
        get() = if (deaths == 0) "§a$deaths" else "§c$deaths"

    private val secretsString: String
        get() = when (secretsTotal) {
            null -> "§b$secretsFound§7/§e?"
            else -> "§b$secretsFound§7/§e$secretsTotal"
        }

    private val mimicString: String
        get() {
            val hasMimic =
                LocationUtils.dungeonFloor?.endsWith("6") == true || LocationUtils.dungeonFloor?.endsWith("7") == true
            if (!hasMimic) return "§eNone"
            return if (mimicDead) "§aDead" else "§cAlive"
        }

    fun update(mapData: MapData) {
        this.mapData = mapData
        updateTiles()
        val tabInfo = TabListUtils.fetchTabEntries()
        updatePlayersFromTab(TabScan.getPlayers(tabInfo))
        addPuzzleNames(TabScan.getPuzzleNames(tabInfo))
        updateMiscFromTab(TabScan.getDungeonData(tabInfo))
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
                tiles.add(Tile.makeTile(Vec2i(x, y)))
            }
        }
    }

    override fun toString(): String {
        return tiles.joinToString(", ")
    }

    fun renderMap() {
        if (!config.mapEnabled || bossDone || (clearDone && !config.showMapDuringBoss)) return
        val shouldRenderNames = when (config.playerNames) {
            0 -> false
            1 -> true
            else -> listOf("SPIRIT_LEAP", "INFINITE_SPIRIT_LEAP", "HAUNT_ABILITY").contains(mc.thePlayer.heldItem?.skyblockID)
        }
        val angle = if (config.mapSpin) (180 - mc.thePlayer.rotationYaw) else 0.0F
        val borderWidth = (150 * config.borderScale).roundToInt()
        val borderHeight = (150 * config.borderScale + (if (config.showInfo) 15 else 0)).roundToInt()
        GlStateManager.pushMatrix()
        GlStateManager.translate(config.mapX.toDouble(), config.mapY.toDouble(), 0.0)

        val bgColor = Color(
            config.bgColor.red,
            config.bgColor.green,
            config.bgColor.blue,
            (config.borderOpacity * 255).roundToInt().coerceIn(0..255)
        )
        val borderColor = Color(
            config.borderColor.red,
            config.borderColor.green,
            config.borderColor.blue,
            (config.borderOpacity * 85).roundToInt().coerceIn(0..255)
        )

        RenderUtils.drawTooltip(2, 2, borderWidth - 4, borderHeight - 4, bgColor, borderColor)
        GL11.glEnable(GL11.GL_SCISSOR_TEST)
        ScaledResolution(mc).scaleFactor.let {
            GL11.glScissor(
                config.mapX * it,
                mc.displayHeight - (config.mapY + borderHeight) * it,
                (borderWidth * it),
                (borderHeight * it)
            )
        }

        GlStateManager.pushMatrix()
        GlStateManager.translate(borderWidth / 2.0, borderWidth / 2.0, 0.0)
        GlStateManager.rotate(angle, 0F, 0F, 1F)
        val scaling = borderWidth.toDouble() / (20 * 6 - 4) * config.mapScale
        GlStateManager.scale(scaling, scaling, 1.0)
        GlStateManager.translate(-renderWidth / 2.0, -renderHeight / 2.0, 0.0)
        tiles.forEach {
            GlStateManager.pushMatrix()
            it.draw(angle)
            GlStateManager.popMatrix()
        }
        players.values.forEach {
            GlStateManager.pushMatrix()
            it.draw(angle, shouldRenderNames)
            GlStateManager.popMatrix()
        }
        GlStateManager.popMatrix()
        GL11.glDisable(GL11.GL_SCISSOR_TEST)
        if (config.showInfo) {
            GlStateManager.pushMatrix()
            val scale = 0.7
            GlStateManager.translate(borderWidth / 2.0, borderHeight.toDouble(), 100.0)
            GlStateManager.scale(scale, scale, 1.0)
            val labelLeftX = (-65 * config.borderScale) / scale
            val valueRightX = (65 * config.borderScale) / scale
            RenderUtils.renderTextCustomAlign("§7Secrets:", labelLeftX, -18, RenderUtils.Align.Left)
            RenderUtils.renderTextCustomAlign("§7Mimic:", labelLeftX, -8, RenderUtils.Align.Left)
            RenderUtils.renderTextCustomAlign("§7Crypts:", valueRightX - 55, -18, RenderUtils.Align.Left)
            RenderUtils.renderTextCustomAlign("§7Deaths:", valueRightX - 55, -8, RenderUtils.Align.Left)
            RenderUtils.renderTextCustomAlign(secretsString, labelLeftX + 80, -18, RenderUtils.Align.Right)
            RenderUtils.renderTextCustomAlign(mimicString, labelLeftX + 80, -8, RenderUtils.Align.Right)
            RenderUtils.renderTextCustomAlign(cryptsString, valueRightX, -18, RenderUtils.Align.Right)
            RenderUtils.renderTextCustomAlign(deathsString, valueRightX, -8, RenderUtils.Align.Right)
            GlStateManager.popMatrix()
        }
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

    private fun updateMiscFromTab(data: TabScan.MiscTabData) {
        data.crypts?.let { crypts = it }
        data.deaths?.let { deaths = it }
        data.secretCount?.let { secretCount ->
            secretsFound = secretCount
            secretsTotal = data.secretPercent?.takeIf { it != 0.0 }?.let { secretPercent ->
                val minTotalSecrets = ceil(secretsFound / (secretPercent + 0.05) * 100).toInt()
                val maxTotalSecrets = floor(secretsFound / (secretPercent - 0.05) * 100).toInt()
                if (minTotalSecrets == maxTotalSecrets) minTotalSecrets else null
            }
        }
    }

    private fun updatePlayersFromTab(data: List<TabScan.PlayerTabData>) {
        data.forEach {
            val dungeonPlayer = players.getOrPut(it.name) { DungeonPlayer(it.name) }
            dungeonPlayer.updateFromTab(it)
        }
    }

    private fun updatePlayersFromMap() {
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
            player.wearingHat = it.isWearing(EnumPlayerModelParts.HAT) == true
        }
    }
}
