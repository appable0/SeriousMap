package com.seriousmap.player

import SeriousMap.Companion.config
import SeriousMap.Companion.mc
import com.seriousmap.map.TabScan
import com.seriousmap.utils.RenderUtils
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import java.awt.Color

data class DungeonPlayer(val name: String) {
    var skin: ResourceLocation? = null
    var mapIndex: Int? = null
    var dungeonClass: DungeonClass? = null
    var isDead: Boolean = false
    var positionRender: PlayerPosition? = null
    var positionMap: PlayerPosition? = null
    var wearingHat = false

    override fun toString(): String {
        return "DungeonPlayer($name, icon-$mapIndex, $dungeonClass, map: $positionMap, render: $positionRender)"
    }

    private val renderString: String
        get() = if (dungeonClass == null) "§a$name" else "§e[${dungeonClass!!.shortName}] §a$name"

    fun draw(angle: Float, shouldDrawName: Boolean) {
        if (isDead && name != mc.thePlayer.name) return
        val position = positionRender ?: (positionMap ?: return)
        GlStateManager.translate(position.x, position.y, 20.0)
        GlStateManager.scale(config.playerScale.toDouble(), config.playerScale.toDouble(), 1.0)
        if (shouldDrawName) {
            GlStateManager.pushMatrix()
            GlStateManager.translate(0.0, 0.0, 10.0)
            GlStateManager.rotate(-angle, 0.0F, 0.0F, 1.0F)
            GlStateManager.scale(0.6, 0.6, 1.0)
            RenderUtils.renderCenteredTextWithBackground(renderString, 0, 11)
            GlStateManager.popMatrix()
        }
        GlStateManager.rotate(position.yaw + 180, 0.0F, 0.0F, 1.0F)
        RenderUtils.renderRect(-5.0, -5.0, 10.0, 10.0, Color.BLACK)
        GlStateManager.enableAlpha()
        GlStateManager.color(255.0F, 255.0F, 255.0F, 255.0F)
        mc.textureManager.bindTexture(skin)
        Gui.drawScaledCustomSizeModalRect(-4, -4, 8f, 8f, 8, 8, 8, 8, 64f, 64f)
        if (wearingHat) {
            Gui.drawScaledCustomSizeModalRect(-4, -4, 40f, 8f, 8, 8, 8, 8, 64f, 64f)
        }
        GlStateManager.disableAlpha()
    }

    fun updateFromTab(tabData: TabScan.PlayerTabData) {
        val status = tabData.playerStatus
        skin = tabData.skin
        when (status) {
            PlayerStatus.Dead -> {
                mapIndex = null
                isDead = true
            }
            is PlayerStatus.WithIndex -> {
                isDead = false
                mapIndex = status.mapIndex
                if (status is PlayerStatus.WithIndex.Class) {
                    dungeonClass = status.dungeonClass
                }
            }
        }
    }

    data class PlayerPosition(
        val x: Double,
        val y: Double,
        val yaw: Float
    )
}