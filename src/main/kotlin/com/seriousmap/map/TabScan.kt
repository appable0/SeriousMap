package com.seriousmap.map

import com.seriousmap.player.DungeonClass
import com.seriousmap.player.PlayerStatus
import com.seriousmap.utils.LocationUtils
import com.seriousmap.utils.TabListUtils
import com.seriousmap.utils.text
import gg.essential.universal.UChat
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils.stripControlCodes
import net.minecraftforge.event.entity.player.PlayerUseItemEvent.Tick
import net.minecraftforge.event.world.NoteBlockEvent.Play
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.util.*

object TabScan {
    private val playerRegex =
        Regex("^\\[\\d{1,3}\\] (?<name>[\\w]{3,16}) (?:.*)*\\((?:(?<class>Healer|Tank|Berserk|Mage|Archer) (?<level>[XVIL0]+)|(?<status>DEAD|EMPTY))\\)")
    private val puzzleRegex = Regex("^ ([\\w ]+): \\[[✦|✔|✖].+")
    private var ticks = 0

    fun getPlayers(tab: List<NetworkPlayerInfo>): List<PlayerTabData> {
        var counter = 0
        var players = tab.filter { networkPlayerInfo ->
            playerRegex.matches(stripControlCodes(networkPlayerInfo.text))
        }
        Collections.rotate(players, -1)
        return players.mapNotNull { networkPlayerInfo ->
            val matchResult = playerRegex.find(stripControlCodes(networkPlayerInfo.text))
            matchResult?.let { result ->
                val name = result.groups["name"]!!.value
                val skin = networkPlayerInfo.locationSkin
                val dungeonClass = DungeonClass.values().firstOrNull { dungeonClass ->
                    dungeonClass.name.equals(result.groups["class"]?.value, ignoreCase = true)
                }
                val playerStatus = when (result.groups["status"]?.value) {
                    "DEAD" -> PlayerStatus.Dead
                    "EMPTY" -> PlayerStatus.WithIndex.Empty(counter++)
                    else -> PlayerStatus.WithIndex.Class(counter++, dungeonClass!!)
                }
                PlayerTabData(name, skin, playerStatus)
            }
        }
    }

    fun getPuzzleNames(tab: List<NetworkPlayerInfo>): Set<String> {
        return tab.mapNotNull {
            val matchResult = puzzleRegex.find(stripControlCodes(it.text))
            matchResult?.let { result ->
                result.groupValues[1]
            }
        }.toSet()
    }

    data class PlayerTabData(
        val name: String,
        val skin: ResourceLocation,
        val playerStatus: PlayerStatus
    )
}