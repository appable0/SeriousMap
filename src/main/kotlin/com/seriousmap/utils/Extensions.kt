package com.seriousmap.utils

import gg.essential.universal.ChatColor.Companion.stripControlCodes
import net.minecraft.client.renderer.GlStateManager
import net.minecraft.item.ItemStack
import net.minecraft.util.Vec4b
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

// from Harry282/FunnyMap
val Vec4b.mapX
    inline get() = (this.func_176112_b() + 128) / 2

val Vec4b.mapY
    inline get() = (this.func_176113_c() + 128) / 2

val Vec4b.yaw
    inline get() = this.func_176111_d() * 22.5f

fun Int.mapToRange(original: IntRange, target: IntRange): Double = this.toDouble().mapToRange(original, target)

fun Double.mapToRange(original: IntRange, target: IntRange): Double {
    val ratio = (this - original.first) / (original.last - original.first)
    return ratio * (target.last - target.first)
}

val ItemStack.skyblockID: String?
    get() {
        if (!this.hasTagCompound()) return null
        val extraAttributes = this.getSubCompound("ExtraAttributes", false) ?: return null
        return if (!extraAttributes.hasKey("id", 8)) null else extraAttributes.getString("id")
    }