package ejektaflex.scalingwealth.struct

import com.google.gson.TypeAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter


class Interval(@Expose @SerializedName("range") var rangeRaw: String = "[0,0]") {

    private enum class IntervalType(val left: Char, val right: Char) {
        OPEN('(', ')'),
        CLOSED('[', ']')
    }

    val coords: Pair<Int, Int> by lazy {
        rangeRaw.drop(1).dropLast(1).split(',').map { it.trim().toInt() }.let {
            it[0] to it[1]
        }
    }

    class IntervalBoundsException(invalidMarker: Char) :
            Exception("Bounds created using invalid bracket symbol: '$invalidMarker'")

    private val startBounds: IntervalType
        get() = IntervalType.values().firstOrNull { it.left == rangeRaw.first() }
                ?: throw IntervalBoundsException(rangeRaw.first())

    private val endBounds: IntervalType
        get() = IntervalType.values().firstOrNull { it.right == rangeRaw.last() }
                ?: throw IntervalBoundsException(rangeRaw.last())

    operator fun contains(d: Double): Boolean {
        return when (startBounds) {
            IntervalType.OPEN -> coords.first < d
            IntervalType.CLOSED -> coords.first <= d
        } && when (endBounds) {
            IntervalType.OPEN -> d < coords.second
            IntervalType.CLOSED -> d <= coords.second
        }
    }

    operator fun contains(n: Int) = contains(n.toDouble())

    override fun toString(): String {
        return rangeRaw
    }

    // Nice and easy JSON bidi serialization
    class IntervalAdapter : TypeAdapter<Interval>() {

        override fun write(out: JsonWriter, value: Interval) {
            out.value(value.toString())
        }

        override fun read(input: JsonReader): Interval {
            return Interval(input.nextString())
        }

    }

}