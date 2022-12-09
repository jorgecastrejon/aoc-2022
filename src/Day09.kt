import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val moves = input.map { move -> move.split(" ").let { it.first() to it.last().toInt() } }

        return moves.resolve(chainLength = 2).size
    }

    fun part2(input: List<String>): Int {
        val moves = input.map { move -> move.split(" ").let { it.first() to it.last().toInt() } }

        return moves.resolve(chainLength = 10).size
    }

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}


fun List<Pair<String, Int>>.resolve(chainLength: Int): Set<PointXY> =
    fold(Array(chainLength) { PointXY() } to mutableSetOf(PointXY())) { (chain, visitedPosition), (dir, amount) ->
        repeat(amount) { chain.move(dir).also { visitedPosition.add(it.last()) } }

        chain to visitedPosition
    }.second


private fun Array<PointXY>.move(dir: String): Array<PointXY> {
    val diff = when (dir) {
        "R" -> 1 to 0
        "L" -> -1 to 0
        "U" -> 0 to -1
        else -> 0 to 1
    }
    this[0] = get(0).plus(x = diff.first, y = diff.second)

    for (i in 1 until this.size) {
        this[i] = get(i) follows this[i - 1]
    }

    return this
}

private infix fun PointXY.follows(head: PointXY): PointXY {
    val dx = head.x - this.x
    val dy = head.y - this.y

    return if (abs(dx) <= 1 && abs(dy) <= 1) {
        this
    } else {
        plus(x = if (abs(dx) > 1) dx / 2 else dx, y = if (abs(dy) > 1) dy / 2 else dy)
    }
}
