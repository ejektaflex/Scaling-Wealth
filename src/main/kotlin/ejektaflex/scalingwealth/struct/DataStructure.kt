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
    @Expose val validTorches = mapOf<String, Map<Int, Array<Float>>>(
            "minecraft:torch" to mapOf(
                    11 to arrayOf(5f, 0f)    // Resistance
            ),


            "minecraft:redstone_torch" to mapOf(
                    3 to arrayOf(5f, 0f)        // Haste
            ),
            "quark:lit_lamp" to mapOf(
                    3 to arrayOf(4f, 1f)        // Haste
            ),


            "thebetweenlands:sulfur_torch" to mapOf(
                    90 to arrayOf(5f, 0f)       // Learning
            ),

            "minecraft:sea_lantern" to mapOf(
                    13 to arrayOf(5f, 0f)       // Water Breathing
            ),
            "quark:elder_sea_lantern" to mapOf(
                    13 to arrayOf(10f, 1f)      // Water Breathing
            ),


            "rustic:golden_lantern" to mapOf(
                    10 to arrayOf(2f, 0f),       // Regeneration,
                    9 to arrayOf(2f, 0f)         // Nausea
            ),
            "rustic:iron_lantern" to mapOf(
                    10 to arrayOf(1.4f, 0f)    // Regeneration,
            ),


            "quark:paper_lantern" to mapOf(
                    64 to arrayOf(5f, 0f)    // EB Wizardry: Ward
            ),
            "quark:paper_lantern:1" to mapOf(
                    64 to arrayOf(5f, 1f)    // EB Wizardry: Ward
            ),


            "rustic:candle" to mapOf(
                    26 to arrayOf(5f, 0f)     // Luck
            ),
            "rustic:candle_gold" to mapOf(
                    26 to arrayOf(4f, 1f)     // Luck
            )
    )
}