package ejektaflex.scalingwealth

import com.google.gson.GsonBuilder
import ejektaflex.scalingwealth.config.ConfigOptions
import ejektaflex.scalingwealth.defaults.DefaultData
import ejektaflex.scalingwealth.proxy.IProxy
import ejektaflex.scalingwealth.struct.WealthStructure
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger
import java.io.File


@Mod(modid = ScalingWealthInfo.MODID, name = ScalingWealthInfo.NAME, version = ScalingWealthInfo.VERSION, modLanguageAdapter = ScalingWealthInfo.ADAPTER, dependencies = ScalingWealthInfo.DEPENDS)
object ScalingWealth : IProxy {

    @SidedProxy(clientSide = ScalingWealthInfo.CLIENT, serverSide = ScalingWealthInfo.SERVER)
    @JvmStatic lateinit var proxy: IProxy

    private var gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create()

    lateinit var configLocation: File
    lateinit var configFile: File
    lateinit var dropsFile: File
    lateinit var logger: Logger

    var config = ConfigOptions()
    var drops = WealthStructure()

    @Mod.Instance
    var instance: ScalingWealth? = this

    @Mod.EventHandler
    override fun preInit(e: FMLPreInitializationEvent) {
        logger = e.modLog

        configLocation = File(e.modConfigurationDirectory, "scalingwealth").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        configFile = File(configLocation, "config.json").also {
            if (!it.exists()) {
                it.createNewFile()
                it.writeText(gson.toJson(config, ConfigOptions::class.java))
            } else {
                config = gson.fromJson(it.readText(), ConfigOptions::class.java)
            }
        }

        dropsFile = File(configLocation, "drops.json").also {
            if (!it.exists()) {
                it.createNewFile()
            }
        }

        MinecraftForge.EVENT_BUS.register(proxy)
        proxy.preInit(e)
    }

    @Mod.EventHandler
    override fun init(e: FMLInitializationEvent) {
        proxy.init(e)
        drops = DefaultData
        dropsFile.writeText(gson.toJson(drops))
        println("DROPS")
        println(gson.toJson(drops))
    }

    @Mod.EventHandler
    override fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
    }

    //@Mod.EventHandler
    //fun serverLoad(e: FMLServerStartingEvent) = e.registerServerCommand(BountyCommand())

}
