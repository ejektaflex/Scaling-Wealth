package ejektaflex.scalingwealth.struct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class Interval(@Expose @SerializedName("range") var rangeRaw: String = "[0,0]") {

    private enum class IntervalType(val left: Char, val right: Char) {
        OPEN('(', ')'),
        CLOSED('[', ']')
    }

    val coords: Pair<Int, Int> by lazy {
        rangeRaw.drop(1).dropLast(1).split(',').map { it.toInt() }.let {
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

    operator fun contains(n: Int): Boolean {
        return when (startBounds) {
            IntervalType.OPEN -> coords.first < n
            IntervalType.CLOSED -> coords.first <= n
        } && when (endBounds) {
            IntervalType.OPEN -> n < coords.second
            IntervalType.CLOSED -> n <= coords.second
        }
    }

    override fun toString(): String {
        return rangeRaw
    }

}