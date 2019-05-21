package ejektaflex.scalingwealth.struct

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class DropDefinition(
        @Expose val chance: Double = 1.0,
        @Expose val item: String = "minecraft:dirt",
        @Expose val nbt: JsonElement? = null
) {

    @SerializedName("min")
    @Expose var amtMin = 1
    @Expose @SerializedName("max")
    var amtMax = 1

    val amtRange: IntRange
        get() = amtMin..amtMax

    fun toItemStack() {

    }

}