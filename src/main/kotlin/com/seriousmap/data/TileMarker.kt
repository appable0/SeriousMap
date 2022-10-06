package com.seriousmap.data

import net.minecraft.util.ResourceLocation
import java.awt.Color

enum class TileMarker(val resourceLocation: ResourceLocation, val color: Color) {
    Failed(ResourceLocation("seriousmap", "cross.png"), Color(161, 0, 0)),
    Undiscovered(ResourceLocation("seriousmap", "question.png"), Color.WHITE),
    White(ResourceLocation("seriousmap", "white_check.png"), Color.YELLOW),
    Green(ResourceLocation("seriousmap", "green_check.png"), Color.GREEN);

    companion object {
        fun fromColor(color: MapColor?): TileMarker? = when (color) {
            MapColor.Green -> Green
            MapColor.White -> White
            MapColor.Red -> Failed
            MapColor.Black -> Undiscovered
            else -> null
        }
    }
}
