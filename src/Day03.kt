fun main() {

    fun part1(input: List<String>): Int =
        input.sumOf { rucksack -> findMisplacedItem(rucksack)?.priority ?: 0 }


    fun part2(input: List<String>): Int =
        input.windowed(3, 3).sumOf { group -> findBadge(group)?.priority ?: 0 }


    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

enum class Item { a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z }

val Char.priority: Int
    get() = (Item.values().find { it.name == lowercase() }?.ordinal ?: 0) + 1 + if (isUpperCase()) 26 else 0

fun findMisplacedItem(rucksack: String): Char? {
    val firstSet = hashSetOf<Char>()
    val secondSet = hashSetOf<Char>()
    val maxIterations = rucksack.length / 2

    for (index in 0 until maxIterations) {
        val firstItem = rucksack[index]
        val secondItem = rucksack[index + maxIterations]

        if (!secondSet.contains(firstItem)) firstSet.add(firstItem) else return firstItem
        if (!firstSet.contains(secondItem)) secondSet.add(secondItem) else return secondItem
    }

    return null
}

fun findBadge(group: List<String>): Char? {
    val hash = hashMapOf<Char, Int>()
    val temp = hashSetOf<Char>()

    group.forEach { rucksack ->
        temp.clear()
        rucksack.forEach { item ->
            if (!temp.contains(item)) {
                temp.add(item)
                hash[item] = hash.getOrDefault(item, 0) + 1
            }
        }
    }


    return hash.keys.find { hash[it] == 3 }
}
