package ejektaflex.scalingwealth

import com.google.gson.GsonBuilder
import ejektaflex.scalingwealth.proxy.IProxy
import ejektaflex.scalingwealth.struct.Interval
import ejektaflex.scalingwealth.struct.DataStructure
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.SidedProxy
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.event.FMLServerStartingEvent
import org.apache.logging.log4j.Logger
import java.io.File


@Mod(modid = ScalingWealthInfo.MODID, name = ScalingWealthInfo.NAME, version = ScalingWealthInfo.VERSION, modLanguageAdapter = ScalingWealthInfo.ADAPTER, dependencies = ScalingWealthInfo.DEPENDS)
object ScalingWealth : IProxy {

    @SidedProxy(clientSide = ScalingWealthInfo.CLIENT, serverSide = ScalingWealthInfo.SERVER)
    @JvmStatic lateinit var proxy: IProxy

    var gson = GsonBuilder()
            .excludeFieldsWithoutExposeAnnotation()
            .setPrettyPrinting()
            .registerTypeAdapter(Interval::class.java, Interval.IntervalAdapter())
            .create()

    lateinit var configLocation: File
    lateinit var configFile: File
    lateinit var dropsFile: File
    lateinit var logger: Logger

    var config = ConfigOptions()
    var drops = DataStructure()

    @Mod.Instance
    var instance: ScalingWealth? = this

    fun loadFiles() {
        logger.info("Loading ScalingWealth JSON files for data population..")
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
                drops = DefaultData
                it.createNewFile()
                it.writeText(gson.toJson(drops))
            } else {
                drops = gson.fromJson(it.readText(), DataStructure::class.java)
            }
        }
    }

    @Mod.EventHandler
    override fun preInit(e: FMLPreInitializationEvent) {
        logger = e.modLog

        configLocation = File(e.modConfigurationDirectory, "scalingwealth").also {
            if (!it.exists()) {
                it.mkdirs()
            }
        }

        loadFiles()

        MinecraftForge.EVENT_BUS.register(proxy)
        proxy.preInit(e)
    }

    @Mod.EventHandler
    override fun init(e: FMLInitializationEvent) {
        proxy.init(e)
    }

    @Mod.EventHandler
    override fun postInit(e: FMLPostInitializationEvent) {
        proxy.postInit(e)
    }

    fun warnErrors(warningMethod: String.() -> Unit = { logger.warn(this) }) {
        // Warn user of erroring items in console
        for ((_, emap) in ScalingWealth.drops.entities) {
            for ((interval, pool) in emap) {
                for (definition in pool) {
                    if (definition.toItemStack() == null) {
                        warningMethod("Definition does not exist!: $definition")
                    }
                }
            }
        }
    }

    @Mod.EventHandler
    fun serverLoad(e: FMLServerStartingEvent) = e.registerServerCommand(ScalingWealthCommand())

}
