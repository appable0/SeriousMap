package com.seriousmap.map

import SeriousMap.Companion.mc
import com.seriousmap.utils.LocationUtils
import net.minecraft.init.Items
import net.minecraft.item.ItemMap
import net.minecraft.world.storage.MapData
import net.minecraftforge.event.world.WorldEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent

object MapScan {
    private var ticks = 0
    var dungeonMap: DungeonMap? = null

    private fun getMapData(): MapData? {
        val mapItem = mc.thePlayer.inventory.getStackInSlot(8)
        if (mapItem?.item !is ItemMap || mapItem.displayName != "§bMagical Map") return null
        return Items.filled_map.getMapData(mapItem, mc.theWorld)
    }

    /*
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onOverlay(event: RenderGameOverlayEvent.Pre) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL) return
        dungeonMap?.renderMap()
    }

     */

    @SubscribeEvent
    fun onUnload(event: WorldEvent.Unload) {
        dungeonMap = null
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (LocationUtils.dungeonFloor == null || event.phase != TickEvent.Phase.START) return
        if (ticks % 4 == 0) {
            val mapData = getMapData() ?: return
            dungeonMap = dungeonMap?.transition(mapData) ?: MapScale.fromMap(mapData)?.let {
                DungeonMap(it)
            }
        }
        if (ticks % 40 == 0) {
            println(dungeonMap)
        }
        ticks++
    }
}
