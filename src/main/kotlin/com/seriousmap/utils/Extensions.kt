package com.seriousmap.utils

import gg.essential.universal.ChatColor.Companion.stripControlCodes
import java.awt.Color
import kotlin.math.roundToInt


fun String.substringBetween(start: String, end: String): String {
    return this
        .substringAfter("(", "")
        .substringBefore(")", "")
}

fun String.cleanSB(): String {
    return stripControlCodes(this)
        ?.toCharArray()
        ?.filter { it.code in 21..126 }
        ?.joinToString("")
        ?: ""
}

fun Color.scale(amount: Float): Color {
    return Color(
        (this.red * amount).roundToInt().coerceIn(0, 255),
        (this.green * amount).roundToInt().coerceIn(0, 255),
        (this.blue * amount).roundToInt().coerceIn(0, 255),
        this.alpha
    )
}