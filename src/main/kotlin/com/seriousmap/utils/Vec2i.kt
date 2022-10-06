package com.seriousmap.utils

data class Vec2i(
    val x: Int,
    val y: Int,
) {
    operator fun plus(other: Vec2i): Vec2i = Vec2i(x + other.x, y + other.y)
    operator fun plus(other: Int): Vec2i = Vec2i(x + other, y + other)

    operator fun minus(other: Vec2i): Vec2i = Vec2i(x - other.x, y - other.y)
    operator fun minus(other: Int): Vec2i = Vec2i(x - other, y - other)

    operator fun times(other: Int): Vec2i = Vec2i(x * other, y * other)
    fun floorDiv(other: Int): Vec2i = Vec2i(x.floorDiv(other), y.floorDiv(other))

    operator fun rem(other: Int): Vec2i = Vec2i(x % other, y % other)
}

operator fun Int.plus(other: Vec2i): Vec2i = Vec2i(this + other.x, this + other.y)
operator fun Int.minus(other: Vec2i): Vec2i = Vec2i(this - other.x, this - other.y)
operator fun Int.times(other: Vec2i): Vec2i = Vec2i(this * other.x, this * other.y)