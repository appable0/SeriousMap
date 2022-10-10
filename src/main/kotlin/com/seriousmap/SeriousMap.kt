import com.seriousmap.commands.SeriousMapCommand
import com.seriousmap.config.Config
import com.seriousmap.config.PersistentData
import com.seriousmap.map.MapScan
import com.seriousmap.map.ChatListener
import com.seriousmap.map.TabScan
import com.seriousmap.utils.LocationUtils
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiScreen
import net.minecraftforge.client.ClientCommandHandler
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ModMetadata
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import java.io.File

@Mod(
    modid = "seriousmap",
    name = "SeriousMap",
    version = "0.1",
    clientSideOnly = true
)
class SeriousMap {

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        metadata = event.modMetadata
        val directory = File(event.modConfigurationDirectory, event.modMetadata.modId)
        directory.mkdirs()
        configDirectory = directory
        persistentData = PersistentData.load()
        config = Config.apply { this.initialize() }
    }

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent) {
        ClientCommandHandler.instance.registerCommand(SeriousMapCommand())

        listOf(
            this,
            LocationUtils,
            MapScan,
            TabScan,
            ChatListener
        ).forEach(MinecraftForge.EVENT_BUS::register)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase != TickEvent.Phase.START || currentGui == null) return
        mc.displayGuiScreen(currentGui)
        currentGui = null
    }

    companion object {
        val mc: Minecraft = Minecraft.getMinecraft()
        var currentGui: GuiScreen? = null

        lateinit var configDirectory: File
        lateinit var config: Config
        lateinit var persistentData: PersistentData

        lateinit var metadata: ModMetadata
    }
}