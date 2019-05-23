package ejektaflex.scalingwealth.dsl

import ejektaflex.scalingwealth.struct.DataStructure
import ejektaflex.scalingwealth.struct.DropDefinition
import ejektaflex.scalingwealth.struct.Interval

class DataDSL {
    val root = DataStructure()

    fun forMob(mobName: String, func: MobDSL.() -> Unit) {
        if (mobName !in root.entities.keys) {
            root.entities[mobName] = mutableMapOf()
        }

        MobDSL(root.entities[mobName]!!).apply(func)
    }

    inner class MobDSL(val entry: MutableMap<Interval, MutableList<DropDefinition>>) {

        fun forRange(range: IntRange, func: DiffRangeDSL.() -> Unit) {
            val interval = Interval.from(range)
            if (interval !in entry.keys) {
                entry[interval] = mutableListOf()
            }
            DiffRangeDSL(entry[interval]!!).apply(func)
        }

        fun above(minDiff: Int, func: DiffRangeDSL.() -> Unit) {
            val interval = Interval.from(minDiff..250)
            if (interval !in entry.keys) {
                entry[interval] = mutableListOf()
            }
            DiffRangeDSL(entry[interval]!!).apply(func)
        }

        fun drop(minDiff: Int, item: String, chance: Double? = 1.0, amt: IntRange? = null) {
            above(minDiff) {
                loot(item, chance, amt)
            }
        }

    }

    inner class DiffRangeDSL(val list: MutableList<DropDefinition>) {

        fun loot(item: String, chance: Double? = 1.0, amt: IntRange? = 1..1) {
            val drop = DropDefinition(chance ?: 1.0, item).apply {
                if (amt != null) {
                    amount = Interval.from(amt)
                }
            }
            list.add(drop)
        }

    }


}