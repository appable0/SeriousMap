package com.seriousmap.data

enum class MapColor {
    Gray, Green, Brown, Yellow, Orange, Pink, Purple, Red, Black, White;

    companion object {
        fun fromByte(byte: Byte?) = when (byte?.toInt()) {
            63 -> Brown
            30 -> Green
            74 -> Yellow
            62 -> Orange
            82 -> Pink
            66 -> Purple
            18 -> Red
            85 -> Gray
            119 -> Black
            34 -> White
            else -> null
        }
    }
}
