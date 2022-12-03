fun main() {

    fun part1(input: List<String>): Int =
        input.sumOf { rucksack -> findMisplacedItem(rucksack).priority }


    fun part2(input: List<String>): Int =
        input.chunked(3).sumOf { group -> findBadge(group).priority }


    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

val letters = CharRange('a','z')

val Char.priority: Int
    get() = letters.indexOf(lowercaseChar()) + 1 + if (isUpperCase()) 26 else 0

fun findMisplacedItem(rucksack: String): Char =
    rucksack.chunked(rucksack.length / 2, CharSequence::toSet).reduce(::intersect).first()

fun findBadge(group: List<String>): Char =
    group.map(CharSequence::toSet).reduce(::intersect).first()

fun intersect(one: Set<Char>, another: Set<Char>): Set<Char> = one.intersect(another)
