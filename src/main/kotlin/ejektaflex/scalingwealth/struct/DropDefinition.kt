package ejektaflex.scalingwealth.struct

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DropDefinition(
        @Expose val chance: Double = 1.0,
        @Expose val item: String = "minecraft:dirt",
        @Expose @SerializedName("amount")
        val amountString: String = "1-1",
        @Expose val nbt: JsonElement? = null
) {

    val amountRange: IntRange by lazy {
        val amts = amountString.split('-')
        amts[0].toInt()..amts[1].toInt()
    }

    fun toItemStack() {

    }

}