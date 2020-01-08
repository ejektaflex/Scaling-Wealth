package ejektaflex.scalingwealth.struct

import com.google.gson.annotations.Expose
import ejektaflex.scalingwealth.AttributeContainer
import net.minecraft.entity.SharedMonsterAttributes

open class DataStructure {

    fun attr(key: String) = AttributeContainer(key)

    @Expose val groups = mutableMapOf<String, MutableList<String>>()
    @Expose val entities = mutableMapOf<String, MutableMap<Interval, MutableList<DropDefinition>>>()
    @Expose val fx = mutableMapOf(
            "thebetweenlands:peat_mummy" to mutableMapOf(
                    attr("maxHealth") to 0.25,
                    attr("attackDamage") to 0.08
            ),
            "thebetweenlands:dreadful_mummy" to mutableMapOf(
                    attr("attackDamage") to 0.08
            ),
            "mowziesmobs_ferrous_wroughtnaut" to mutableMapOf(
                    attr("speed") to 2.0
            )
    )
    @Expose val doNotDespawn = mutableListOf(
            "mowziesmobs:naga",
            "thebetweenlands:chiromaw"
    )
    @Expose val forcePlayerTargeting = mutableListOf(
            "twilightforest:fire_beetle",
            "twilightforest:slime_beetle"
    )
}