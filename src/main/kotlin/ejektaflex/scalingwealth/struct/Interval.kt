package ejektaflex.scalingwealth.struct

import com.google.gson.TypeAdapter
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

data class Interval(@Expose @SerializedName("range") val rangeRaw: String = "[0,0]") {

    val coords: Pair<Int, Int> by lazy {
        rangeRaw.drop(1).dropLast(1).split(',').map { it.trim().toInt() }.let {
            it[0] to it[1]
        }
    }

    private enum class IntervalType(
            val left: Char,
            val right: Char,
            val checkStart: Interval.(it: Double) -> Boolean,
            val checkEnd: Interval.(it: Double) -> Boolean,
            val isReversed: Boolean = false
    ) {
        OPEN('(', ')', { coords.first < it }, { it < coords.second }),
        CLOSED('[', ']', { coords.first <= it }, { it <= coords.second }),
        OPEN_REVERSED(')', '(', { coords.first > it }, { it > coords.second }, true),
        CLOSED_REVERSED(']', '[', { coords.first >= it }, { it >= coords.second }, true)
    }

    class IntervalBoundsException(invalidMarker: Char) :
            Exception("Bounds created using invalid bracket symbol: '$invalidMarker'")

    private val startBounds: IntervalType by lazy {
        IntervalType.values().firstOrNull { it.left == rangeRaw.first() }
                ?: throw IntervalBoundsException(rangeRaw.first())
    }

    private val endBounds: IntervalType by lazy {
        IntervalType.values().firstOrNull { it.right == rangeRaw.last() }
                ?: throw IntervalBoundsException(rangeRaw.last())
    }

    operator fun contains(d: Double): Boolean {
        return if (startBounds.isReversed && endBounds.isReversed) {
            startBounds.checkStart(this, d) || endBounds.checkEnd(this, d)
        } else {
            startBounds.checkStart(this, d) && endBounds.checkEnd(this, d)
        }
    }

    operator fun contains(n: Int) = contains(n.toDouble())

    override fun toString(): String {
        return rangeRaw
    }

    // Nice and easy JSON bidi serialization
    class IntervalAdapter : TypeAdapter<Interval>() {

        override fun write(out: JsonWriter, value: Interval?) {
            out.value(value?.toString())
        }

        override fun read(input: JsonReader): Interval? {
            return Interval(input.nextString())
        }

    }

    companion object {
        fun from(range: IntRange): Interval {
            return Interval("[${range.first},${range.last}]")
        }
    }

}