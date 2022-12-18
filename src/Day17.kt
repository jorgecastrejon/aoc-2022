fun main() {

    fun part1(input: List<String>): Int {
        val directions = input.first()
        val pushTo: Array<Direction> = Array(directions.length) { index -> Direction.from(directions[index]) }
        var windIndex = 0
        val types = Array(5) { index -> PieceType.from(index) }
        var typeIndex = 0
        val map = mutableSetOf<Pair<Int, Int>>()
        var bottom = 0
        var pieces = 0
        var collision: Boolean

        while (pieces < 2022) {
            if (typeIndex == types.size) typeIndex = 0
            val type = types[typeIndex++]
            val piece = Piece(type = type, topLeft = type.placeFrom(left = 2, bottom = bottom + 3))
            collision = false

            while (!collision) {
                if (windIndex == pushTo.size) windIndex = 0

                val windDirection = pushTo[windIndex++]

                if (!piece.wouldCollisionIn(map, dx = windDirection.op, dy = 0)) {
                    piece.move(x = windDirection.op, y = 0)
                }

                if (!piece.wouldCollisionIn(map, dx = 0, dy = -1)) {
                    piece.move(x = 0, y = -1)
                } else {
                    map.settle(piece)
                    val above = piece.topLeft.second + 1
                    bottom = if (bottom > above) bottom else above
                    collision = true
                }
            }

            pieces++
        }

        return bottom
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day17")
    println(part1(input))
    println(part2(input))
}

private enum class Direction(val op: Int) {
    Left(-1), Right(1);

    companion object {
        fun from(char: Char): Direction = if (char == '<') Left else Right
    }
}

private enum class PieceType {
    HBar, Plus, L, VBar, Box;

    companion object {
        fun from(index: Int): PieceType = values()[index]
    }

    fun placeFrom(left: Int, bottom: Int): Pair<Int, Int> =
        when (this) {
            HBar -> left to bottom
            Plus -> left to bottom + 2
            L -> left to bottom + 2
            VBar -> left to bottom + 3
            Box -> left to bottom + 1
        }

    fun pointsAt(topLeft: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (x, y) = topLeft

        return when (this) {
            HBar -> listOf(x to y, x + 1 to y, x + 2 to y, x + 3 to y)
            Plus -> listOf(x + 1 to y, x to y - 1, x + 1 to y - 1, x + 2 to y - 1, x + 1 to y - 2)
            L -> listOf(x + 2 to y, x + 2 to y - 1, x + 2 to y - 2, x + 1 to y - 2, x to y - 2)
            VBar -> listOf(x to y, x to y - 1, x to y - 2, x to y - 3)
            Box -> listOf(x to y, x + 1 to y, x to y - 1, x + 1 to y - 1)
        }
    }
}

private class Piece(val type: PieceType, var topLeft: Pair<Int, Int>) {

    val points: List<Pair<Int, Int>> get() = type.pointsAt(topLeft)

    fun move(x: Int, y: Int): Piece {
        if (type.pointsAt(topLeft.first + x to topLeft.second + y).all { (x1, _) -> x1 in 0..6 }) {
            topLeft = (topLeft.first + x) to (topLeft.second + y)
        }

        return this
    }

    fun wouldCollisionIn(map: Set<Pair<Int, Int>>, dx: Int, dy: Int): Boolean =
        type.pointsAt(topLeft.first + dx to topLeft.second + dy)
            .any { map.contains(it) || it.second < 0 }
}

private fun MutableSet<Pair<Int, Int>>.settle(piece: Piece) {
    piece.points.forEach(this::add)
}