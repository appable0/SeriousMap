package com.seriousmap.map

import com.seriousmap.utils.LocationUtils
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.client.event.ClientChatReceivedEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

object ChatListener {
    // list stolen from UnclaimedBloom6/BloomCore
    val bossEntryMessages = listOf(
        "[BOSS] Bonzo: Gratz for making it this far, but I’m basically unbeatable.",
        "[BOSS] Scarf: This is where the journey ends for you, Adventurers.",
        "[BOSS] The Professor: I was burdened with terrible news recently...",
        "[BOSS] Thorn: Welcome Adventurers! I am Thorn, the Spirit! And host of the Vegan Trials!",
        "[BOSS] Livid: Welcome, you arrive right on time. I am Livid, the Master of Shadows.",
        "[BOSS] Sadan: So you made it all the way here... Now you wish to defy me? Sadan?!",
        "[BOSS] Maxor: WELL WELL WELL LOOK WHO’S HERE!"

    )

    @SubscribeEvent(receiveCanceled = true)
    fun onChatReceived(event: ClientChatReceivedEvent) {
        if (LocationUtils.dungeonFloor == null || MapScan.dungeonMap == null) return
        val message = stripControlCodes(event.message.unformattedText)
        // list stolen from UnclaimedBloom6/BloomCore
        if (listOf(
                "\$skytils-dungeon-score-mimic\$",
                "mimic dead!",
                "mimic dead",
                "mimic killed!",
                "mimic killed",
                "child destroyed!",
                "mimic obliterated!",
                "mimic exorcised!",
                "mimic destroyed!",
                "mimic annhilated!",
                "breefing killed",
                "breefing dead"
            ).contains(message.lowercase())
        ) {
            MapScan.dungeonMap?.mimicDead = true
        } else if (bossEntryMessages.contains(message)) {
            MapScan.dungeonMap?.clearDone = true
        } else if (message == "                             > EXTRA STATS <") {
            MapScan.dungeonMap?.bossDone = true
        }
    }

    // stolen from Skytils/SkytilsMod
    @SubscribeEvent
    fun onEntityDeath(event: LivingDeathEvent) {
        if (LocationUtils.dungeonFloor == null || MapScan.dungeonMap == null) return
        val entity = event.entity as? EntityZombie ?: return
        if (entity.isChild && (0..3).map { entity.getCurrentArmor(it) }.all { it == null }) {
            MapScan.dungeonMap?.mimicDead = true
            // really don't think people need yet another mimic dead message
        }
    }

}