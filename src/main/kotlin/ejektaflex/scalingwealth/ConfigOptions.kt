package ejektaflex.scalingwealth

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ConfigOptions {

    @Expose
    @SerializedName("Warn Erroring Items on Startup?")
    var warnErroringItemsOnStartup = true


}