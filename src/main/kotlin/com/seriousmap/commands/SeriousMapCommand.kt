package com.seriousmap.commands

import SeriousMap
import com.seriousmap.config.Config
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.ChatComponentText

class SeriousMapCommand : CommandBase() {
    override fun getCommandName() = "seriousmap"

    override fun getCommandAliases() = listOf("smap")

    override fun getCommandUsage(sender: ICommandSender?) = "/$commandName"

    override fun getRequiredPermissionLevel() = 0

    override fun processCommand(sender: ICommandSender?, args: Array<out String>?) {
        SeriousMap.currentGui = Config.gui()
    }
}