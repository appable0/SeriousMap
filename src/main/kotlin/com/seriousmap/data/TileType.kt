package com.seriousmap.data

import com.seriousmap.config.Config
import java.awt.Color

enum class TileType {
    Undiscovered, Room, Entrance, Yellow, Trap, Fairy, Puzzle, Blood, Wither, Opened;

    companion object {
        fun fromColor(color: MapColor?): TileType? = when (color) {
            MapColor.Gray -> Undiscovered
            MapColor.Brown -> Room
            MapColor.Green -> Entrance
            MapColor.Yellow -> Yellow
            MapColor.Orange -> Trap
            MapColor.Pink -> Fairy
            MapColor.Purple -> Puzzle
            MapColor.Red -> Blood
            MapColor.Black -> Wither
            else -> null
        }

        fun toColor(tileType: TileType): Color = when (tileType) {
            Undiscovered -> Config.colorUndiscovered
            Room -> Config.colorRoom
            Entrance -> Config.colorEntrance
            Yellow -> Config.colorYellow
            Trap -> Config.colorTrap
            Fairy -> Config.colorFairy
            Puzzle -> Config.colorPuzzle
            Blood -> Config.colorBlood
            Wither -> Config.colorWither
            Opened -> Config.colorOpened
        }
    }
}