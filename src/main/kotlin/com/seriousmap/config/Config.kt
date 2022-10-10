package com.seriousmap.config

import SeriousMap
import com.seriousmap.gui.MoveGui
import gg.essential.vigilance.Vigilant
import java.awt.Color
import java.io.File


object Config : Vigilant(
    File(SeriousMap.configDirectory, "config.toml"),
    SeriousMap.metadata.name
) {

    var mapSpin = true
    var mapX = 0
    var mapY = 0
    var borderScale = 1.0F
    var mapScale = 1.0F

    var borderColor = Color(16, 0, 16, 240)
    var bgColor = Color(80, 0, 255, 80)

    var doorWidth = 4
    var checkScale = 0.8F
    var doorDarken = 0.8F

    var colorUndiscovered = Color(65, 65, 65)
    var colorRoom = Color(107, 58, 17)
    var colorEntrance = Color(20, 133, 0)
    var colorYellow = Color(254, 223, 0)
    var colorTrap = Color(216, 127, 51)
    var colorFairy = Color(224, 0, 255)
    var colorPuzzle = Color(117, 0, 133)
    var colorBlood = Color(255, 0, 0)
    var colorWither = Color(0, 0, 0)
    var colorOpened = Color(107, 58, 17)

    init {
        category("Hidden") {
            slider(
                ::mapX,
                name = "Map X",
                min = 0,
                max = 2000,
                hidden = true
            )
            slider(
                ::mapY,
                name = "Map Y",
                min = 0,
                max = 1000,
                hidden = true
            )
            decimalSlider(
                ::borderScale,
                name = "Border scale",
                min = 0.1F,
                max = 5.0F,
                hidden = true
            )
        }
        category("Visual") {
            button(
                name = "Move map",
            ) {
                SeriousMap.currentGui = MoveGui()
            }
            switch(
                ::mapSpin,
                name = "Spinny map"
            )
            decimalSlider(
                ::mapScale,
                name = "Map scale",
                min = 0.5F,
                max = 1F,
            )

            color(::bgColor, name = "Background color")
            color(::borderColor, name = "Border color")

            slider(
                ::doorWidth,
                name = "Door width",
                min = 2,
                max = 8
            )
            decimalSlider(
                ::checkScale,
                name = "Check scale",
                min = 0.1F,
                max = 1.0F
            )
            percentSlider(
                ::doorDarken,
                name = "Door darken percent"
            )

        }
        category("Room and door colors") {
            color(::colorUndiscovered, name = "Undiscovered room color", allowAlpha = false)
            color(::colorRoom, name = "Normal room color", allowAlpha = false)
            color(::colorEntrance, name = "Entrance room color", allowAlpha = false)
            color(::colorYellow, name = "Yellow room color", allowAlpha = false)
            color(::colorTrap, name = "Trap room color", allowAlpha = false)
            color(::colorFairy, name = "Fairy room color", allowAlpha = false)
            color(::colorPuzzle, name = "Puzzle room color", allowAlpha = false)
            color(::colorBlood, name = "Blood room color", allowAlpha = false)
            color(::colorWither, name = "Wither door color", allowAlpha = false)
            color(::colorOpened, name = "Opened door color", allowAlpha = false)
        }

    }


}