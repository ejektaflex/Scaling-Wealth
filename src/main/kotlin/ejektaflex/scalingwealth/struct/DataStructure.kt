package ejektaflex.scalingwealth.struct

import com.google.gson.annotations.Expose

open class DataStructure {
    @Expose val entities = mutableMapOf<String, MutableMap<Interval, MutableList<DropDefinition>>>()
}