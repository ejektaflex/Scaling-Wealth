package ejektaflex.scalingwealth.proxy

import ejektaflex.scalingwealth.ScalingWealth
import ejektaflex.scalingwealth.ext.registryName
import net.minecraft.entity.player.EntityPlayer
import net.minecraftforge.event.entity.living.LivingDropsEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI
import kotlin.random.Random

open class CommonProxy : IProxy {

    override fun postInit(e: FMLPostInitializationEvent) {
        ScalingWealth.warnErrors()
    }

    @SubscribeEvent
    fun entityDrops(e: LivingDropsEvent) {

        if (e.entity?.world?.isRemote == true) {
            return
        }

        // Choose how to calculate difficulty
        val diff = when (ScalingWealth.config.difficultyBasedOnPlayer && e.source.trueSource is EntityPlayer) {
            true -> ScalingHealthAPI.getPlayerDifficulty(e.source.trueSource as EntityPlayer)
            false -> ScalingHealthAPI.getEntityDifficulty(e.entityLiving)
        }

        val entityKey = e.entityLiving.registryName.toString()
        if (entityKey in ScalingWealth.drops.entities) {
            val dropTable = ScalingWealth.drops.entities[entityKey]!!
            val validPools = dropTable.filter { diff in it.key }
            for (pool in validPools) {
                for (definition in pool.value) {

                    // API does not include blight detection, too lazy to figure it out via caps right now
                    /*
                    if (definition.mustBeBlight && ScalingHealthAPI.isBlight(e.entityLiving)) {

                    }
                     */

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