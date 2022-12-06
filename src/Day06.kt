fun main() {

    fun part1(input: List<String>): Int =
        input.first().windowed(4).first { it.toSet().size == 4 }.let { input.first().indexOf(it) + 4 }

    fun part2(input: List<String>): Int =
        input.first().windowed(14).first { it.toSet().size == 14 }.let { input.first().indexOf(it) + 14 }

    val input = readInput("Day06")

    println(part1(input))
    println(part2(input))
}
