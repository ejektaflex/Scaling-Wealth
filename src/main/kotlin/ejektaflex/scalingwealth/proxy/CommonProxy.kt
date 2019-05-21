package ejektaflex.bountiful.proxy

import ejektaflex.bountiful.ScalingWealth
import ejektaflex.bountiful.ScalingWealthInfo
import ejektaflex.bountiful.api.events.PopulateBountyBoardEvent
import ejektaflex.bountiful.api.ext.ifHasCapability
import ejektaflex.bountiful.cap.*
import ejektaflex.bountiful.config.BountifulIO
import ejektaflex.bountiful.data.DefaultData
import ejektaflex.bountiful.item.ItemBounty
import ejektaflex.bountiful.logic.BountyChecker
import ejektaflex.bountiful.registry.BountyRegistry
import ejektaflex.bountiful.registry.RewardRegistry
import ejektaflex.bountiful.api.stats.BountifulStats
import ejektaflex.bountiful.data.BountyData
import ejektaflex.bountiful.logic.BountyCreator
import ejektaflex.bountiful.worldgen.VillageBoardComponent
import ejektaflex.bountiful.worldgen.VillageBoardCreationHandler
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.ResourceLocation
import net.minecraft.world.World
import net.minecraft.world.gen.structure.MapGenStructureIO
import net.minecraftforge.common.capabilities.CapabilityManager
import net.minecraftforge.event.AttachCapabilitiesEvent
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
import net.minecraftforge.fml.common.registry.VillagerRegistry

open class CommonProxy : IProxy {

    // Update mob bounties
    @SubscribeEvent
    fun entityLivingDeath(e: LivingDeathEvent) {
        if (e.source.trueSource is EntityPlayer) {
            val player = e.source.trueSource as EntityPlayer
            val bountyStacks = player.inventory.mainInventory.filter { it.item is ItemBounty }
            if (bountyStacks.isNotEmpty()) {
                bountyStacks.forEach { stack ->
                    val data = BountyData.from(stack)
                    BountyChecker.tryTakeEntities(player, data, stack, e.entityLiving)
                }
            }
        }
    }

    // Update global bounties
    @SubscribeEvent
    fun onWorldTick(e: TickEvent.WorldTickEvent) {
        if (!e.world.isRemote && ScalingWealth.config.globalBounties && e.phase == TickEvent.Phase.END) {
            e.world.ifHasCapability(CapManager.CAP_BOARD!!) {
                holder.update(e.world, null)
            }
        }
    }

    // Attach global bounty inventory to world
    @SubscribeEvent
    fun attachCaps(e: AttachCapabilitiesEvent<World>) {
        e.addCapability(ResourceLocation(ScalingWealthInfo.MODID, "GlobalData"), GlobBoardProvider())
    }

    @SubscribeEvent
    fun onBoardPost(e: PopulateBountyBoardEvent) {
        // Don't post bounties without data to the bounty board.
        if (!e.stack.hasTagCompound()) {
            e.isCanceled = true
        }
        // Cancel first posting to board on board creation (as first update is immediate after placement)
        e.board?.let {
            if (it.newBoard) {
                it.newBoard = false
                e.isCanceled = true
            }
        }
    }

    override fun postInit(e: FMLPostInitializationEvent) {
        // Populate entries, fill if none exist
        "bounties.json".let {
            BountifulIO.populateConfigFolder(ScalingWealth.configDir, DefaultData.entries.items, it)
            val invalids = BountifulIO.hotReloadBounties()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/bounties.json' contains one or more invalid bounties. Invalid bounty objectives: $invalids")
            }
            val minObjectives = ScalingWealth.config.bountyAmountRange.last
            if (BountyRegistry.items.size < minObjectives) {
                throw Exception("Config file needs more bounties! Must have at least $minObjectives bounty objectives to choose from, according to the current config.")
            }
        }

        // Same for rewards
        "rewards.json".let {
            BountifulIO.populateConfigFolder(ScalingWealth.configDir, DefaultData.rewards.items.map { item ->
                item.genericPick
            }, it)
            val invalids = BountifulIO.hotReloadRewards()
            if (invalids.isNotEmpty()) {
                throw Exception("'bountiful/rewards.json' contains one or more invalid rewards. Invalid rewards: $invalids")
            }
        }

        BountifulStats.register()

        println("Bounties: ${BountyRegistry.items.size}")
        BountyRegistry.items.forEach { println(it) }

        println("Rewards: ${RewardRegistry.items.size}")
        RewardRegistry.items.forEach { println(it) }
    }

}