package utils

data class Position(var first: Int, var second: Int) {
    operator fun plus(other: Position) = Position(first + other.first, second + other.second)

    operator fun minus(other: Position): Position = Position(first - other.first, second - other.second)
}