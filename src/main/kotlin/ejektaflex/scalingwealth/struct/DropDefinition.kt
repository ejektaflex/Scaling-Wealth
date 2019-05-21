package ejektaflex.scalingwealth.struct

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ejektaflex.scalingwealth.ext.toItemStack
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT

data class DropDefinition(
        @Expose val chance: Double = 1.0,
        @Expose val item: String = "minecraft:dirt",
        @Expose @SerializedName("nbt")
        val jsonNBT: JsonElement? = null
) {

    @SerializedName("min")
    @Expose var amtMin = 1
    @Expose @SerializedName("max")
    var amtMax = 1

    val amtRange: IntRange
        get() = amtMin..amtMax


    fun toItemStack(): ItemStack? {
        val stack = item.toItemStack
        jsonNBT?.let {
            stack?.tagCompound = JsonToNBT.getTagFromJson(jsonNBT.toString())
        }
        return stack
    }

}