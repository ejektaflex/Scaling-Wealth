package ejektaflex.scalingwealth.struct

import com.google.gson.JsonElement
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import ejektaflex.scalingwealth.ext.toItemStack
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraft.nbt.JsonToNBT
import net.minecraftforge.oredict.OreDictionary

data class DropDefinition(
        @Expose val chance: Double = 1.0,
        @Expose val item: String = "minecraft:dirt",
        @Expose val mustBeBlight: Boolean? = null,
        @Expose val numDrops: String? = null,
        @Expose val scaleChance: Double = 0.0,
        @Expose @SerializedName("nbt")
        val jsonNBT: JsonElement? = null
) {

    @SerializedName("amount")
    @Expose var amount: Interval? = null

    val amtRange: IntRange
        get() {
            return (amount ?: Interval(numDrops ?: "[1, 1]")).let { it.coords.first..it.coords.second }
        }

    fun toItemStack(): ItemStack? {
        val stack = if (item.startsWith("ore:")) {
            val tag = item.substringAfter("ore:")
            OreDictionary.getOres(tag).first() ?: Items.APPLE.defaultInstance.copy()
        } else {
            item.toItemStack(amtRange.random())
        }
        jsonNBT?.let {
            stack?.tagCompound = JsonToNBT.getTagFromJson(jsonNBT.toString())
        }
        return stack
    }

}