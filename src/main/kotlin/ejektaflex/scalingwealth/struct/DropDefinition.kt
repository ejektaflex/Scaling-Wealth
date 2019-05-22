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
        @Expose val mustBeBlight: Boolean? = null,
        @Expose @SerializedName("nbt")
        val jsonNBT: JsonElement? = null
) {

    @SerializedName("amount")
    @Expose var amount: Interval? = null

    val amtRange: IntRange
        get() {
            return (amount ?: Interval("[1, 1]")).let { it.coords.first..it.coords.second }
        }

    fun toItemStack(): ItemStack? {
        val stack = item.toItemStack(amtRange.random())
        jsonNBT?.let {
            stack?.tagCompound = JsonToNBT.getTagFromJson(jsonNBT.toString())
        }
        return stack
    }

}