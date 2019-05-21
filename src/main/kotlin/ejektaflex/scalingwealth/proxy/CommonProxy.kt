package ejektaflex.scalingwealth.proxy

import ejektaflex.scalingwealth.ScalingWealth
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

    }

}