package utils

data class Position(var first: Int, var second: Int) {
    operator fun plus(other: Position) = Position(first + other.first, second + other.second)

    operator fun minus(other: Position): Position = Position(first - other.first, second - other.second)

    fun isOutOfBounds() = (first < 0 || first > 7) || (second < 0 || second > 7)
}