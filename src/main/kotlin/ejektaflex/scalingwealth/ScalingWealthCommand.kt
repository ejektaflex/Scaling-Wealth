package ejektaflex.scalingwealth

import ejektaflex.scalingwealth.ext.sendMessage
import net.minecraft.command.CommandException
import net.minecraft.command.ICommand
import net.minecraft.command.ICommandSender
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos
import kotlin.reflect.KMutableProperty0
import kotlin.system.measureTimeMillis


class ScalingWealthCommand : ICommand {

    override fun compareTo(other: ICommand): Int {
        return 0
    }

    override fun getName(): String {
        return "scalingwealth"
    }

    override fun getUsage(sender: ICommandSender): String {
        return "/scalingwealth"
    }

    override fun getAliases(): List<String> {
        val aliases = ArrayList<String>()
        aliases.add("scw")
        return aliases
    }

    private fun hasBasicPerms(sender: ICommandSender): Boolean {
        val playerProfile = sender.server?.playerList?.onlinePlayerProfiles?.find { sender.name == it.name }
        return playerProfile != null && sender.server?.playerList?.canSendCommands(playerProfile) == true
    }

    fun trySet(sender: ICommandSender, input: String, thing: KMutableProperty0<Double>) {
        try {
            thing.set(input.toDouble())
            sender.sendMessage("You changed ${thing.name} to ${thing.get()}")
        } catch (e: Exception) {
            sender.sendMessage("Oops: ${e.message}")
            e.printStackTrace()
        }
    }

    @Throws(CommandException::class)
    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {

        if (args.isNotEmpty()) {
            when (val curr: String = args[0]) {

                // "/bo bounties" & "/bo rewards" are only meant to be used in dev right now
                "reload" -> {
                    if (hasBasicPerms(sender)) {
                        sender.sendMessage("Loading JSON files...")
                        try {
                            val loadTime = measureTimeMillis {
                                ScalingWealth.loadFiles()
                            }
                            sender.sendMessage("Files loaded successfully from disk. (${loadTime}ms)")
                        } catch (e: Exception) {
                            sender.sendMessage("File loading failed: Check log for error details.")
                            ScalingWealth.logger.error("Exception while loading JSON files:")
                            println(e.message)
                        }
                    }
                }

                // Spawn Chance
                "sc" -> trySet(sender, args[1], ScalingWealth::spawnChance)
                // Death Gain per Kill
                "dg" -> trySet(sender, args[1], ScalingWealth::deathGain)
                // Death Loss per Player Death
                "dl" -> trySet(sender, args[1], ScalingWealth::deathLoss)

            }
        } else {
            sender.sendMessage("Valid commands: '/scw reload")
        }

    }

    override fun checkPermission(server: MinecraftServer, sender: ICommandSender): Boolean {
        return sender is EntityPlayer
    }

    override fun getTabCompletions(server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?): MutableList<String> {
        return mutableListOf()
    }


    override fun isUsernameIndex(args: Array<String>, index: Int): Boolean {
        return false
    }

}

