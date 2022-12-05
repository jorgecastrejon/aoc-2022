fun main() {

    fun part1(input: List<String>): String {
        val (stacks, movementSet) = input.parse()

        movementSet.forEach { (amount, from, to) ->
            repeat(amount) { stacks[from]?.removeLast()?.let { stacks[to]?.addLast(it) } }
        }

        return stacks.values.map(ArrayDeque<Char>::removeLast).joinToString("")
    }

    fun part2(input: List<String>): String {
        val (stacks, movementSet) = input.parse()

        movementSet.forEach { (amount, from, to) ->
            (0 until amount).map { stacks[from]!!.removeLast() }.reversed()
                .forEach { stacks[to]?.addLast(it) }
        }

        return stacks.values.map(ArrayDeque<Char>::removeLast).joinToString("")
    }


    val input = readInput("Day05")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.parse(): Pair<Map<Int, ArrayDeque<Char>>, List<Triple<Int, Int, Int>>> {
    val index = indexOfFirst { it.trim().first().isDigit() }

    return subList(0, index).getStacks() to subList(index + 2, size).getMovements()
}

private fun List<String>.getStacks(): Map<Int, ArrayDeque<Char>> {
    val stacks = mutableMapOf<Int, ArrayDeque<Char>>()

    reversed().map { it.drop(1).dropLast(1) }
        .forEach { line ->
            (line.indices step 4).asSequence()
                .forEachIndexed { index, i ->
                    line[i].takeUnless(Char::isWhitespace)?.let {
                        stacks.getOrPut(index) { ArrayDeque() }.run { addLast(it) }
                    }
                }

        }

    return stacks
}

private fun List<String>.getMovements(): List<Triple<Int, Int, Int>> =
    map { line -> line.filter { char -> char.isDigit() || char.isWhitespace() }.split(" ").filter(String::isNotBlank) }
        .map { Triple(it[0].toInt(), it[1].toInt() - 1, it[2].toInt() - 1) }
