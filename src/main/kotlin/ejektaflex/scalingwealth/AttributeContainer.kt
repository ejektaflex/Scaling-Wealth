package ejektaflex.scalingwealth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.IAttribute

class AttributeContainer(val key: String) {

    val attr: IAttribute
        get() = attributeMap[key] ?: error("Not a valid attribute bro: $key")

    class AttributeAdapter() : TypeAdapter<AttributeContainer>() {



        override fun write(out: JsonWriter, item: AttributeContainer) {
            out.value(item.toString())
        }

        override fun read(reader: JsonReader): AttributeContainer? {
            return AttributeContainer(reader.nextString())
        }

    }

    override fun toString(): String {
        return key
    }

    companion object {
        val attributeMap = mapOf(
                "maxHealth" to SharedMonsterAttributes.MAX_HEALTH,
                "attackDamage" to SharedMonsterAttributes.ATTACK_DAMAGE,
                "speed" to SharedMonsterAttributes.MOVEMENT_SPEED,
                "attackSpeed" to SharedMonsterAttributes.ATTACK_SPEED,
                "knockback" to SharedMonsterAttributes.KNOCKBACK_RESISTANCE,
                "armor" to SharedMonsterAttributes.ARMOR,
                "toughness" to SharedMonsterAttributes.ARMOR_TOUGHNESS
        )



    }

}