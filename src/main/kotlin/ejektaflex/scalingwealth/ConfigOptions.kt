package ejektaflex.scalingwealth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfigOptions {

    @Expose
    @SerializedName("Warn Erroring Items on Startup?")
    var warnErroringItemsOnStartup = true

    @Expose
    @SerializedName("If [true], loot is based on player difficulty. If [false], loot is based on killed entity difficulty.")
    var difficultyBasedOnPlayer = true


}