package ejektaflex.scalingwealth.config

import com.google.gson.annotations.SerializedName

class ConfigOptions {

    @SerializedName("Generate Defaults - Should this mod generate default data?")
    var genDefaults = true

    @SerializedName("Default maximum range for entities if in the #+ format")
    var maxRangeDefault = 250

}