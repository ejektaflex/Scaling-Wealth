package ejektaflex.scalingwealth.proxy

import ejektaflex.scalingwealth.ScalingWealth
import ejektaflex.scalingwealth.ext.registryName
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI

open class CommonProxy : IProxy {

    @SubscribeEvent
    fun entityDrops(e: LivingDropsEvent) {
        println("Entity: ${e.entity.name} is dropping: ${e.drops.joinToString { it.displayName.toString() }}")
        val diff = ScalingHealthAPI.getEntityDifficulty(e.entityLiving)
        println("ERN: ${e.entityLiving.registryName}")
        val entityKey = e.entityLiving.registryName.toString()
        if (entityKey in ScalingWealth.drops.entities) {
            val dropTable = ScalingWealth.drops.entities[entityKey]!!
            val validPools = dropTable.filter { diff in it.key }
            for (pool in validPools) {
                println("POOL:")
                println(pool)
            }
        }
    }

}