fun main() {

    fun part1(input: List<String>): Int {
        val instructions = getInstructions(input)
        var x = 1
        var cycle = 1
        val strength = mutableListOf<Int>()

        instructions.forEach { (instruction, value) ->

            if (cycle == 20) {
                strength.add(x * cycle)
            }

            if (cycle > 20 &&(cycle -20) % 40 == 0) {
                strength.add(x * cycle)
            }

            if (instruction == "addx" && value != null) {
                x += value
            }

            cycle++
        }

        return strength.sum()
    }

    fun part2(input: List<String>): Int {
        val instructions = getInstructions(input)
        var x = 1
        var cycle = 1
        var row = 0
        val pixels = mutableMapOf<Int, MutableList<String>>()

        instructions.forEach { (instruction, value) ->
            val pixel = if ((cycle % 40) in (x until x + 3) ) "#" else "."
            pixels.getOrPut(row) { mutableListOf() }.add(pixel)

            if (cycle % 40 == 0) {
                row++
            }

            if (instruction == "addx" && value != null) {
                x += value
            }

            cycle++
        }
        pixels.values.forEach { println(it.joinToString("")) }

        return 0
    }

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}

private fun getInstructions(input: List<String>): MutableList<Pair<String, Int?>> {
    val instructions = mutableListOf<Pair<String, Int?>>()

    for (line in input) {
        val split = line.split(" ")

        if (split.first() == "addx") {
            instructions.add(split.first() to null)
            instructions.add(split.first() to split.last().toIntOrNull())
        } else {
            instructions.add(split.first() to null)
        }
    }

    return instructions
}
