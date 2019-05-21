package ejektaflex.scalingwealth.proxy

import ejektaflex.scalingwealth.ScalingWealth
import ejektaflex.scalingwealth.ext.registryName
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDeathEvent
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI
import kotlin.random.Random

open class CommonProxy : IProxy {

    override fun postInit(e: FMLPostInitializationEvent) {
        ScalingWealth.warnErrors()
    }

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
                for (definition in pool.value) {
                    val stack = definition.toItemStack()
                    if (stack == null) {
                        ScalingWealth.logger.warn("Item not found!: $definition")
                    } else {
                        if (Random.nextDouble() <= definition.chance) {
                            e.entityLiving.entityDropItem(stack, 0.5f)
                        } else {
                            ScalingWealth.logger.debug("Did not drop ${stack.displayName}, not not meet chance")
                        }
                    }
                }
            }
        }
    }

}