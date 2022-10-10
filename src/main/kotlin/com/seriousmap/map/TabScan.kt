package com.seriousmap.map

import com.seriousmap.player.DungeonClass
import com.seriousmap.player.PlayerStatus
import com.seriousmap.utils.text
import gg.essential.universal.UChat
import net.minecraft.client.network.NetworkPlayerInfo
import net.minecraft.util.ResourceLocation
import net.minecraft.util.StringUtils.stripControlCodes
import java.util.*

object TabScan {
    private val playerRegex =
        Regex("^\\[\\d{1,3}\\] (?<name>[\\w]{3,16}) (?:.*)*\\((?:(?<class>Healer|Tank|Berserk|Mage|Archer) (?<level>[XVIL0]+)|(?<status>DEAD|EMPTY))\\)")
    private val puzzleRegex = Regex("^ ([\\w ]+): \\[[✦|✔|✖].+")
    private val deathsRegex = Regex("^Deaths: \\((\\d{1,3})\\)")
    private val secretsFoundRegex = Regex("^ Secrets Found: (\\d{1,3})$")
    private val cryptsRegex = Regex("^ Crypts: (\\d{1,3})")
    private val secretPercentRegex = Regex("^ Secrets Found: ([\\d.]{1,5})%")

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

    fun getDungeonData(tab: List<NetworkPlayerInfo>): MiscTabData {
        return MiscTabData(
            findMatching(tab, secretPercentRegex)?.groupValues?.get(1)?.toDoubleOrNull() ,
            findMatching(tab, secretsFoundRegex)?.groupValues?.get(1)?.toIntOrNull(),
            findMatching(tab, deathsRegex)?.groupValues?.get(1)?.toIntOrNull(),
            findMatching(tab, cryptsRegex)?.groupValues?.get(1)?.toIntOrNull(),
        )
    }

    private fun findMatching(tab: List<NetworkPlayerInfo>, regex: Regex): MatchResult? {
        return tab.firstNotNullOfOrNull { regex.find(stripControlCodes(it.text)) }
    }

    data class MiscTabData(
        val secretPercent: Double?,
        val secretCount: Int?,
        val deaths: Int?,
        val crypts: Int?
    )

    data class PlayerTabData(
        val name: String,
        val skin: ResourceLocation,
        val playerStatus: PlayerStatus
    )
}