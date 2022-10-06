package com.seriousmap.player

import com.seriousmap.map.TabScan
import net.minecraft.util.ResourceLocation

data class DungeonPlayer(val name: String) {
    var skin: ResourceLocation? = null
    var mapIndex: Int? = null
    var dungeonClass: DungeonClass? = null
    var isDead: Boolean = false

    fun updateFromTab(tabData: TabScan.PlayerTabData) {
        val status = tabData.playerStatus
        skin = tabData.skin
        when (status) {
            PlayerStatus.Dead -> {
                mapIndex = null
                isDead = true
            }
            is PlayerStatus.WithIndex -> {
                mapIndex = status.mapIndex
                if (status is PlayerStatus.WithIndex.Class) {
                    dungeonClass = status.dungeonClass
                }
            }
        }
    }
}