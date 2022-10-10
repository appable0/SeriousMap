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

    var mapEnabled = true
    var showMapDuringBoss = false
    var showInfo = true
    var mapSpin = true
    var mapX = 5
    var mapY = 5
    var borderScale = 0.85F
    var mapScale = 1.0F
    var puzzleNames = 1
    var playerNames = 2

    var borderColor = Color(80, 0, 255) //240
    var borderOpacity = 0.4F
    var bgColor = Color(16, 0, 16)  //80

    var doorWidth = 6
    var checkScale = 0.8F
    var playerScale = 1F
    var doorDarken = 0.9F

    var colorUndiscovered = Color(65, 65, 65)
    var colorRoom = Color(107, 58, 17)
    var colorEntrance = Color(20, 133, 0)
    var colorYellow = Color(254, 223, 0)
    var colorTrap = Color(216, 127, 51)
    var colorFairy = Color(224, 0, 255)
    var colorPuzzle = Color(117, 0, 133)
    var colorBlood = Color(255, 0, 0)
    var colorWither = Color(0, 255, 255)
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
            decimalSlider(
                ::mapScale,
                name = "Map scale",
                min = 0.5F,
                max = 1F,
                hidden = true
            )
            decimalSlider(
                ::checkScale,
                name = "Check scale",
                description = "Sets the scale of checks and puzzle names.",
                min = 0.1F,
                max = 1.0F,
                hidden = true
            )
            decimalSlider(
                ::playerScale,
                name = "Player scale",
                description = "Sets the scale of players.",
                min = 0.5F,
                max = 2.0F,
                hidden = true
            )
        }
        category("General") {
            switch(
                ::mapEnabled,
                name = "Map enabled",
                description = "Turns map rendering on or off."
            )
            button(
                name = "Move map",
                description = "Move and/or scale map and its borders."
            ) {
                SeriousMap.currentGui = MoveGui()
            }
            switch(
                ::mapSpin,
                name = "Spinny map",
                description = "Enable map spinning about the center of the dungeon."
            )

            switch(
                ::showInfo,
                name = "Show dungeon information below map",
                description = "Show mimic status, secrets found, crypts, and deaths below the map."
            )

            switch(
                ::showMapDuringBoss,
                name = "Show dungeon map during boss",
                description = "Continues showing map after boss entry."
            )

        }
        category("Visual") {

            subcategory("General") {
                selector(
                    ::playerNames,
                    name = "Show player names",
                    description = "Sets whether player names should be rendered on map",
                    options = listOf("Never", "Always", "Spirit Leap")
                )
                color(
                    ::bgColor,
                    name = "Background color",
                    description = "Set the background color of map",
                    allowAlpha = false
                )

                color(
                    ::borderColor,
                    name = "Border color",
                    description = "Set the border color. Note that this fades toward black near the bottom.",
                    allowAlpha = false
                )

                percentSlider(
                    ::borderOpacity,
                    name = "Background opacity",
                    description = "Set the background opacity. 100 is opaque."
                )

                slider(
                    ::doorWidth,
                    name = "Door width",
                    description = "Sets the width of a door (room width is 16).",
                    min = 2,
                    max = 8
                )

                selector(
                    ::puzzleNames,
                    name = "Show puzzle names",
                    description = "Sets whether puzzle names should be rendered in map.",
                    options = listOf("Never", "Always", "If incomplete")
                )
            }

            subcategory("Room colors") {
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

                percentSlider(
                    ::doorDarken,
                    name = "Door darken percent",
                    description = "Sets the amount to darken doors, if any."
                )
            }


        }


    }


}