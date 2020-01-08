package ejektaflex.scalingwealth.struct

import com.google.gson.annotations.Expose
import net.minecraft.entity.SharedMonsterAttributes

open class DataStructure {
    @Expose val groups = mutableMapOf<String, MutableList<String>>()
    @Expose val entities = mutableMapOf<String, MutableMap<Interval, MutableList<DropDefinition>>>()
    @Expose val fx = mutableMapOf(
            "thebetweenlands:peat_mummy" to mapOf(
                    SharedMonsterAttributes.MAX_HEALTH to 0.25,
                    SharedMonsterAttributes.ATTACK_DAMAGE to 0.08
            ),
            "thebetweenlands:dreadful_mummy" to mapOf(
                    SharedMonsterAttributes.ATTACK_DAMAGE to 0.08
            )
    )
}