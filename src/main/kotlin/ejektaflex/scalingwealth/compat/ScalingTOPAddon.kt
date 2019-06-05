package ejektaflex.scalingwealth.compat

import ejektaflex.scalingwealth.ScalingWealth
import ejektaflex.scalingwealth.ext.registryName
import mcjty.theoneprobe.api.IProbeHitEntityData
import mcjty.theoneprobe.api.IProbeInfo
import mcjty.theoneprobe.api.IProbeInfoEntityProvider
import mcjty.theoneprobe.api.ProbeMode
import net.minecraft.entity.Entity
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.world.World
import net.silentchaos512.scalinghealth.api.ScalingHealthAPI
import net.minecraft.entity.EntityLiving


class ScalingTOPAddon : IProbeInfoEntityProvider {

    override fun getID() = "scalingwealth:scalingdiff"

    override fun addProbeEntityInfo(mode: ProbeMode?, probeInfo: IProbeInfo, player: EntityPlayer, world: World?, entity: Entity, data: IProbeHitEntityData?) {

        if (entity is EntityLiving) {
            val eName = entity.registryName.toString()
            val mobDiff = ScalingHealthAPI.getEntityDifficulty(entity).toInt()
            //probeInfo.progress(mobDiff, 250)
            probeInfo.text("Difficulty: $mobDiff")
            if (player.isSneaking && eName in ScalingWealth.drops.entities.keys) {
                val playerDiff = ScalingHealthAPI.getPlayerDifficulty(player)
                val extraDrops = ScalingWealth.drops.entities[eName]!!
                val numValid = extraDrops.filter { playerDiff.toInt() in it.key }.size
                probeInfo.text("Drops @ Lvl: $numValid/${extraDrops.size}")
            }
        }
    }

}