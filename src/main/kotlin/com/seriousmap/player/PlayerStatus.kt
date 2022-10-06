package com.seriousmap.player

sealed class PlayerStatus {
    object Dead: PlayerStatus()

    sealed class WithIndex(val mapIndex: Int): PlayerStatus() {
        class Empty(mapIndex: Int): WithIndex(mapIndex)
        class Class(mapIndex: Int, val dungeonClass: DungeonClass): WithIndex(mapIndex)
    }

    override fun toString(): String {
        return when (this) {
            Dead -> "PlayerStatus.Dead"
            is WithIndex.Empty -> "PlayerStatus.Empty(icon-${mapIndex})"
            is WithIndex.Class -> "PlayerStatus.Class(icon-${mapIndex}; $dungeonClass)"
        }
    }
}