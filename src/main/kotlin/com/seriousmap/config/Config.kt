package com.seriousmap.config

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
                max = 100
            )
            slider(
                ::mapY,
                name = "Map Y",
                min = 0,
                max = 100
            )
            decimalSlider(
                ::borderScale,
                name = "Border scale",
                min = 0.1F,
                max = 5.0F,
            )
        }
        category("Visual") {
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
            color(::colorUndiscovered, name = "Undiscovered room color")
            color(::colorRoom, name = "Normal room color")
            color(::colorEntrance, name = "Entrance room color")
            color(::colorYellow, name = "Yellow room color")
            color(::colorTrap, name = "Trap room color")
            color(::colorFairy, name = "Fairy room color")
            color(::colorPuzzle, name = "Puzzle room color")
            color(::colorBlood, name = "Blood room color")
            color(::colorWither, name = "Wither door color")
            color(::colorOpened, name = "Opened door color")
        }

    }


}