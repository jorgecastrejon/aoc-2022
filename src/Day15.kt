import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val list = parseInput(input)
        val intervals = mutableListOf<IntRange>()

        for ((sX, sY, bX, bY) in list) {
            val currentDistance = abs(bX - sX) + abs(bY - sY)
            val distanceToRow = abs(2000000 - sY)

            val diff = currentDistance - distanceToRow

            if (diff >= 0) {
                intervals.add((sX - diff)..(sX + diff))
            }
        }

        return intervals.reduce { acc, next -> minOf(acc.first, next.first)..maxOf(acc.last, next.last) }
            .let { it.last - it.first }
    }

    fun part2(input: List<String>): Long {
        val size = 4000000
        val list = parseInput(input)

        return list.firstNotNullOf { (sX, sY, bX, bY) ->
            outerBounds(sX, sY, d = abs(bX - sX) + abs(bY - sY) + 1)
                .asSequence()
                .filter { (x, y) -> x in 0..size && y in 0..size }
                .firstOrNull { (x, y) ->
                    list.all { (s1, s2, b1, b2) ->
                        val d1 = abs(b1 - s1) + abs(b2 - s2)
                        val d2 = abs(x - s1) + abs(y - s2)

                        d2 > d1
                    }
                }
        }.let { it.first * 4000000L + it.second }
    }

    val input = readInput("Day15")
    println(part1(input))
    println(part2(input))
}

fun outerBounds(x: Int, y: Int, d: Int): List<Pair<Int, Int>> {
    val vertices = listOf(x + d to y, x to y + d, x - d to y, x to y - d)
    val iterations = listOf(-1 to 1, -1 to -1, 1 to -1, 1 to 1)

    return vertices.zip(iterations)
        .map { (vertex, iteration) ->
            (0 until d).map { vertex.first + iteration.first * it to vertex.second + iteration.second * it }
        }.flatten()
}

private fun parseInput(input: List<String>): List<List<Int>> =
    input.map { line ->
        line.replace(":", ",")
            .filter { it.isDigit() || it == ',' || it == '-' }
            .split(",").map(String::toInt)
    }