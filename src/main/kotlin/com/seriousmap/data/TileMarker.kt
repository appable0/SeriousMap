package com.seriousmap.data

import net.minecraft.util.ResourceLocation
import java.awt.Color

enum class TileMarker(val resourceLocation: ResourceLocation, val color: Color) {
    FAILED(ResourceLocation("seriousmap", "cross.png"), Color(161, 0, 0)),
    UNDISCOVERED(ResourceLocation("seriousmap", "question.png"), Color.WHITE),
    WHITE(ResourceLocation("seriousmap", "white_check.png"), Color.YELLOW),
    GREEN(ResourceLocation("seriousmap", "green_check.png"), Color.GREEN);

    companion object {
        fun fromColor(color: MapColor?): TileMarker? = when (color) {
            MapColor.Green -> GREEN
            MapColor.White -> WHITE
            MapColor.Red -> FAILED
            MapColor.Black -> UNDISCOVERED
            else -> null
        }
    }
}
