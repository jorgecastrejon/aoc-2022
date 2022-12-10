fun main() {

    fun part1(input: List<String>): Int =
        getInstructions(input).foldIndexed(1 to mutableListOf<Int>()) { index, (x, strengths), (instruction, value) ->
            if (((index + 1) - 20) % 40 == 0) strengths.add(x * (index + 1))
            val x1 = if (instruction == "addx" && value != null) x + value else x

            x1 to strengths
        }.second.sum()

    fun part2(input: List<String>): Int {
        getInstructions(input).foldIndexed(1 to mutableListOf<String>()) { index, (x, pixels), (instruction, value) ->
            pixels.add(if (((index + 1) % 40) in (x until x + 3)) "#" else ".")
            val x1 = if (instruction == "addx" && value != null) x + value else x

            x1 to pixels
        }.second.chunked(40).forEach { line -> println(line.joinToString("")) }

        return 0
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun getInstructions(input: List<String>): List<Pair<String, Int?>> =
    input.map { line ->
        line.split(" ").let {
            if (it.first() == "addx") {
                listOf(it.first() to null, it.first() to it.last().toIntOrNull())
            } else {
                listOf(it.first() to null)
            }
        }
    }.flatten()
