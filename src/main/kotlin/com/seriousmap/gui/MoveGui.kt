package com.seriousmap.gui

import SeriousMap.Companion.config
import SeriousMap.Companion.mc
import gg.essential.universal.UChat
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.gui.ScaledResolution
import org.lwjgl.input.Mouse
import kotlin.math.roundToInt

class MoveGui : GuiScreen() {

    var isDragging = false
    var clickOffsetX = 0
    var clickOffsetY = 0

    override fun mouseClicked(mouseX: Int, mouseY: Int, mouseButton: Int) {
        isDragging = isHoveringOverMap(mouseX, mouseY)
        clickOffsetX = mouseX - config.mapX
        clickOffsetY = mouseY - config.mapY
        //UChat.chat("Click offset: $clickOffsetX, $clickOffsetY")
    }

    override fun mouseReleased(mouseX: Int, mouseY: Int, state: Int) {
        isDragging = false
        super.mouseReleased(mouseX, mouseY, state)
    }

    override fun mouseClickMove(mouseX: Int, mouseY: Int, clickedMouseButton: Int, timeSinceLastClick: Long) {
        if (isDragging) {
            //UChat.chat("Mouse X and Y: $mouseX, $mouseY")

            val newX = mouseX - clickOffsetX
            val newY = mouseY - clickOffsetY
            //UChat.chat("New X and Y: $newX, $newY")
            config.mapX = coerceIntoScreenWidth(newX)
            config.mapY = coerceIntoScreenHeight(newY)
        }
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick)
    }

    override fun handleMouseInput() {
        super.handleMouseInput()
        val scaledResolution = ScaledResolution(mc)
        val mouseX = Mouse.getX() / scaledResolution.scaleFactor
        val mouseY = scaledResolution.scaledHeight - Mouse.getY() / scaledResolution.scaleFactor
        if (isHoveringOverMap(mouseX, mouseY)) {
            val scrollAmount = Mouse.getEventDWheel()
            if (scrollAmount != 0) {
                val oldScale = config.borderScale
                val newScale = (config.borderScale + scrollAmount / 7200.0).coerceIn(0.2, 3.0)
                val newX = (mouseX + (newScale / oldScale) * (config.mapX - mouseX)).roundToInt()
                val newY = (mouseY + (newScale / oldScale) * (config.mapY - mouseY)).roundToInt()
                config.mapX = coerceIntoScreenWidth(newX)
                config.mapY = coerceIntoScreenHeight(newY)
                config.borderScale = newScale.toFloat()
                clickOffsetX = mouseX - newX
                clickOffsetY = mouseY - newY
            }
        }
    }

    override fun onGuiClosed() {
        config.markDirty()
        config.writeData()
        super.onGuiClosed()
    }

    companion object {
        private fun coerceIntoScreenWidth(x: Int): Int {
            val xRange = 0..ScaledResolution(mc).scaledWidth - (150 * config.borderScale).roundToInt()
            return if (xRange.isEmpty()) 0 else x.coerceIn(xRange)
        }

        private fun coerceIntoScreenHeight(y: Int): Int {
            val yRange = 0..ScaledResolution(mc).scaledHeight - (150 * config.borderScale).roundToInt()
            return if (yRange.isEmpty()) 0 else y.coerceIn(yRange)
        }

        private fun isHoveringOverMap(x: Int, y: Int) =
            (x in config.mapX..((config.mapX + 150 * config.borderScale).roundToInt()) && (y in config.mapY..(config.mapY + 150 * config.borderScale).roundToInt()))
    }
}