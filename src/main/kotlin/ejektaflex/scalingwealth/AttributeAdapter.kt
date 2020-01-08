package ejektaflex.scalingwealth

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import net.minecraft.entity.SharedMonsterAttributes
import net.minecraft.entity.ai.attributes.IAttribute
import java.io.IOException

internal class AttributeAdapter : TypeAdapter<IAttribute>() {

    private val attributeMap = mapOf(
            "maxHealth" to SharedMonsterAttributes.MAX_HEALTH,
            "attackDamage" to SharedMonsterAttributes.ATTACK_DAMAGE
    )

    override fun read(reader: JsonReader): IAttribute {
        return attributeMap[reader.nextString()!!] ?: error("Not in adapter attribute map")
    }

    override fun write(writer: JsonWriter, student: IAttribute?) {
        val attrString = attributeMap.entries.first { it.value == student }.key
        writer.value(attrString)
    }
}