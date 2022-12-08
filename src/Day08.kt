fun main() {

    fun part1(input: List<String>): Int {
        val trees = input.map { line -> line.split("").filter(String::isNotBlank).map(String::toInt) }
        val visibleTrees = mutableListOf<Int>()

        for (y in trees.indices) {
            val row = trees[y]

            for (x in row.indices) {
                if (x == 0 || x == row.lastIndex || y == 0 || y == trees.lastIndex || isVisible(trees, x, y)) {
                    visibleTrees.add(row[x])
                }
            }
        }

        return visibleTrees.count()
    }

    fun part2(input: List<String>): Int {
        val trees = input.map { line -> line.split("").filter(String::isNotBlank).map(String::toInt) }
        var max = 0

        for (y in trees.indices) {
            val row = trees[y]

            for (x in row.indices) {
                max = calculateScore(trees, x, y).takeIf { it > max } ?: max
            }
        }

        return max
    }

    val input = readInput("Day08")

    println(part1(input))
    println(part2(input))
}

private fun isVisible(trees: List<List<Int>>, x: Int, y: Int): Boolean {
    val row = trees[y]
    val height = row[x]

    val above = (0 until y).all { trees[it][x] < height }
    val below = (y + 1 until trees.size).all { trees[it][x] < height }
    val left = (0 until x).all { trees[y][it] < height }
    val right = (x + 1 until row.size).all { trees[y][it] < height }

    return above || below || left || right
}

private fun calculateScore(trees: List<List<Int>>, x: Int, y: Int): Int {
    if (x == 0 || x == trees[y].lastIndex || y == 0 || y == trees.lastIndex) return 0

    val row = trees[y]
    val height = row[x]

    val above = (y - 1 downTo 0).takeWhile { it > 0 && trees[it][x] < height }.count() + 1
    val below = (y + 1 until trees.size).takeWhile { it < trees.lastIndex && trees[it][x] < height }.count() + 1
    val left = (x - 1 downTo 0).takeWhile { it > 0 && trees[y][it] < height }.count() + 1
    val right = (x + 1 until row.size).takeWhile { it < row.lastIndex && trees[y][it] < height }.count() + 1

    return above * below * left * right
}