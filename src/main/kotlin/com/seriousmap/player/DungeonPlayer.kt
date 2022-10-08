package com.seriousmap.player

import SeriousMap.Companion.mc
import com.seriousmap.map.TabScan
import com.seriousmap.utils.RenderUtils
import net.minecraft.client.gui.Gui
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.ResourceLocation
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL14
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

    fun draw() {
        if (isDead) return
        val position = positionRender ?: (positionMap ?: return)
        GlStateManager.pushMatrix()
        GlStateManager.translate(position.x, position.y, 20.0)
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
        GlStateManager.popMatrix()
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