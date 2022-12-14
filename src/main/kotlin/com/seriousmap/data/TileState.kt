package com.seriousmap.data

import SeriousMap.Companion.config
import SeriousMap.Companion.mc
import com.seriousmap.utils.RenderUtils
import net.minecraft.client.renderer.GlStateManager
import java.awt.Color

class TileState() {
    var marker: TileMarker? = null
    var name: String? = null
        set(value) {
            field = when (value) {
                "Higher Or Lower" -> "Blaze"
                "Ice Path" -> "Silverfish"
                else -> value
            }
        }

    fun draw(angle: Float) {
        GlStateManager.rotate(-angle, 0F, 0F, 1F)
        GlStateManager.translate(0F, 0F, 1F)

        val shouldRenderCheck = when {
            name == null -> true
            config.puzzleNames == 0 -> true
            config.puzzleNames == 1 -> false
            else -> marker != null
        }

        if (shouldRenderCheck) {
            marker?.resourceLocation?.let { resourceLocation ->
                val textureSize = (16 * config.checkScale / 2).toInt()
                GlStateManager.enableAlpha()
                GlStateManager.color(255f, 255f, 255f, 255f)
                mc.textureManager.bindTexture(resourceLocation)
                RenderUtils.drawTexturedModalRect(-textureSize, -textureSize, textureSize * 2, textureSize * 2)
            }
        } else {
            GlStateManager.scale(config.checkScale * 0.75, config.checkScale * 0.75, 1.0)
            RenderUtils.renderCenteredMultiLineText(name, marker?.color ?: Color.WHITE)
        }
    }
}