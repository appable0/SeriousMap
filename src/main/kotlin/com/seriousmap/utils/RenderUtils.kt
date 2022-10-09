package com.seriousmap.utils

import SeriousMap.Companion.mc
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.client.renderer.Tessellator
import net.minecraft.client.renderer.WorldRenderer
import net.minecraft.client.renderer.vertex.DefaultVertexFormats
import org.lwjgl.opengl.GL11.GL_QUADS
import java.awt.Color

// effectively stolen directly from Harry282/FunnyMap, under AGPL 3.0
object RenderUtils {
    private val tessellator: Tessellator = Tessellator.getInstance()
    private val worldRenderer: WorldRenderer = tessellator.worldRenderer

    private fun addQuadVertices(x: Double, y: Double, w: Double, h: Double) {
        worldRenderer.pos(x, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y + h, 0.0).endVertex()
        worldRenderer.pos(x + w, y, 0.0).endVertex()
        worldRenderer.pos(x, y, 0.0).endVertex()
    }

    fun renderRect(x: Double, y: Double, w: Double, h: Double, color: Color) {
        if (color.alpha == 0) return
        GlStateManager.enableBlend()
        GlStateManager.disableTexture2D()
        GlStateManager.enableAlpha()
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0)
        GlStateManager.color(color.red / 255f, color.green / 255f, color.blue / 255f, color.alpha / 255f)

        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION)
        addQuadVertices(x, y, w, h)
        tessellator.draw()

        GlStateManager.disableAlpha()
        GlStateManager.enableTexture2D()
        GlStateManager.disableBlend()
    }

    fun drawTexturedModalRect(x: Int, y: Int, width: Int, height: Int) {
        worldRenderer.begin(GL_QUADS, DefaultVertexFormats.POSITION_TEX)
        worldRenderer.pos(x.toDouble(), (y + height).toDouble(), 0.0).tex(0.0, 1.0).endVertex()
        worldRenderer.pos((x + width).toDouble(), (y + height).toDouble(), 0.0).tex(1.0, 1.0).endVertex()
        worldRenderer.pos((x + width).toDouble(), y.toDouble(), 0.0).tex(1.0, 0.0).endVertex()
        worldRenderer.pos(x.toDouble(), y.toDouble(), 0.0).tex(0.0, 0.0).endVertex()
        tessellator.draw()
    }

    fun renderCenteredTextWithBackground(str: String?, x: Int, y: Int) {
        val width = mc.fontRendererObj.getStringWidth(str)
        renderRect(x - width / 2.0 - 2, y - 1.0, width + 4.0, 10.0, Color(0, 0, 0, 150))
        mc.fontRendererObj.drawString(
            str,
            x - width / 2.0F,
            y.toFloat(),
            Color.WHITE.hashCode(),
            true
        )
    }

    fun renderCenteredMultiLineText(str: String?, color: Color) {
        if (str == null) return
        val text = str.split(" ")
        if (text.isNotEmpty()) {
            val yTextOffset = text.size * 4.5f
            for (i in text.indices) {
                mc.fontRendererObj.drawString(
                    text[i],
                    (-mc.fontRendererObj.getStringWidth(text[i]) shr 1).toFloat(),
                    i * 9 - yTextOffset,
                    color.hashCode(),
                    true
                )
            }
        }
    }


}