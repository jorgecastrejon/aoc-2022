import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val map = getMapFromInput(input)

        return map.startDropping(stopWhen = { _, y -> y > map.bottom })
    }

    fun part2(input: List<String>): Int {
        val map = getMapFromInput(input)

        return map.startDropping(stopWhen = { _, _ -> map.contains(500 to 0) }, limit = map.bottom + 2)
    }

    val input = readInput("Day14")
    println(part1(input))
    println(part2(input))
}

private fun MutableSet<Pair<Int, Int>>.startDropping(stopWhen: (Int, Int) -> Boolean, limit: Int? = null): Int {
    var x = 500
    var y = 0
    var count = 0

    while (true) {

        when {
            stopWhen(x, y) -> return count
            y + 1 == limit -> {
                count++
                add(x to y)
                x = 500
                y = 0
            }
            x to y + 1 !in this -> y++
            x - 1 to y + 1 !in this -> {
                x--; y++
            }
            x + 1 to y + 1 !in this -> {
                x++; y++
            }
            else -> {
                count++
                add(x to y)
                x = 500
                y = 0
            }
        }
    }
}

private fun getMapFromInput(input: List<String>): MutableSet<Pair<Int, Int>> {
    val set: MutableSet<Pair<Int, Int>> = mutableSetOf()

    input.forEach { path ->
        path.split(" -> ")
            .zipWithNext()
            .forEach { (start, end) ->
                val (x1, y1) = start.split(",").map(String::toInt)
                val (x2, y2) = end.split(",").map(String::toInt)

                val x = x2.compareTo(x1)
                val y = y2.compareTo(y1)

                val diffX = abs(x2 - x1)
                val diffY = abs(y2 - y1)

                for (i in 0..maxOf(diffX, diffY)) {
                    set.add(x1 + (x * i) to y1 + (y * i))
                }
            }
    }

    return set
}

private val Set<Pair<Int, Int>>.bottom: Int get() = maxOf { (_, y) -> y }
