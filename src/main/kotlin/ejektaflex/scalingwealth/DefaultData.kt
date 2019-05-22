package ejektaflex.scalingwealth

import ejektaflex.scalingwealth.struct.DropDefinition
import ejektaflex.scalingwealth.struct.Interval
import ejektaflex.scalingwealth.struct.DataStructure

object DefaultData : DataStructure() {
    init {
        entities["minecraft:zombie"] = mutableMapOf(
                Interval("[0, 100]") to mutableListOf(
                        DropDefinition(
                                chance = 0.5,
                                item = "minecraft:cobblestone"
                        )
                ),
                Interval("[50, 150]") to mutableListOf(
                        DropDefinition(
                                chance = 0.25,
                                item = "minecraft:stone"
                        )
                )
        )
    }
}