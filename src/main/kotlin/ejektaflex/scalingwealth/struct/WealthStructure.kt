package ejektaflex.scalingwealth.struct

import com.google.gson.annotations.Expose

open class WealthStructure {
    @Expose val entities = mutableMapOf<String, MutableMap<Interval, MutableList<DropDefinition>>>()

}