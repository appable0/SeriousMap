package com.seriousmap.data

import com.seriousmap.utils.Vec2i

sealed class Tile {
    abstract fun transition(corner: MapColor?, center: MapColor?, puzzle: String?): Tile
}