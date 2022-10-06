package com.seriousmap.data

import com.seriousmap.config.Config
import java.awt.Color

enum class TileType {
    UNDISCOVERED,
    ROOM,
    ENTRANCE,
    YELLOW,
    TRAP,
    FAIRY,
    PUZZLE,
    BLOOD,
    WITHER;

    companion object {
        fun fromByte(byte: Byte?): TileType? = when (byte) {
            85.toByte() -> UNDISCOVERED
            63.toByte() -> ROOM
            30.toByte() -> ENTRANCE
            74.toByte() -> YELLOW
            62.toByte() -> TRAP
            82.toByte() -> FAIRY
            66.toByte() -> PUZZLE
            18.toByte() -> BLOOD
            119.toByte() -> WITHER
            else -> null
        }

        fun toColor(tileType: TileType): Color = when (tileType) {
            UNDISCOVERED -> Config.colorUndiscovered
            ROOM -> Config.colorRoom
            ENTRANCE -> Config.colorEntrance
            YELLOW -> Config.colorYellow
            TRAP -> Config.colorTrap
            FAIRY -> Config.colorFairy
            PUZZLE -> Config.colorPuzzle
            BLOOD -> Config.colorBlood
            WITHER -> Config.colorWither
        }
    }
}