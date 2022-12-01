fun main() {
    fun part1(input: List<String>): Int =
        input.joinToString(",")
            .split(",,")
            .maxOf { it.split(",").sumOf(String::toInt) }


    fun part2(input: List<String>): Int =
        input.joinToString(",")
            .split(",,")
            .map { it.split(",").sumOf(String::toInt) }
            .sortedDescending()
            .take(3)
            .sum()


    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}
