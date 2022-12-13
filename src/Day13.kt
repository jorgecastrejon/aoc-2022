fun main() {

    fun part1(input: List<String>): Int =
        input.windowed(2, 3).mapNotNull {
            (createPacket(it.first()) as Packet.Composed compare createPacket(it.last()) as Packet.Composed)
        }.foldRightIndexed(0) { index, isRight, acc -> if (isRight) acc + (index + 1) else acc }

    fun part2(input: List<String>): Int {
        val d1 = createPacket("[[2]]") as Packet.Composed
        val d2 = createPacket("[[6]]") as Packet.Composed

        val list = input.windowed(2, 3)
            .map { listOf(createPacket(it.first()) as Packet.Composed, createPacket(it.last()) as Packet.Composed) }
            .flatten()
            .let { it + listOf(d1, d2) }
            .sortedWith(comparator)

        return (list.indexOf(d1) + 1) * (list.indexOf(d2) + 1)
    }

    val input = readInput("Day13")
    println(part1(input))
    println(part2(input))
}

private val comparator = Comparator<Packet.Composed> { p1, p2 ->
    when (p1 compare p2) {
        null -> 0
        true -> -1
        false -> 1
    }
}

private infix fun Packet.Composed.compare(another: Packet.Composed): Boolean? {
    var solution: Boolean? = null
    var index = 0

    while (solution == null) {
        val x = value.getOrNull(index)
        val y = another.value.getOrNull(index)
        if (x == null) return true
        if (y == null) return false

        if (x is Packet.Composed && y is Packet.Composed) {
            if (x != y) {
                solution = x compare y
            }
        } else if (x is Packet.Simple && y is Packet.Composed) {
            solution = Packet.Composed(listOf(x)) compare y
        } else if (x is Packet.Composed && y is Packet.Simple) {
            solution = x compare Packet.Composed(listOf(y))
        } else if (x is Packet.Simple && y is Packet.Simple) {
            if (x.value != y.value) {
                solution = x.value < y.value
            }
        } else {
            throw IllegalArgumentException("no other option")
        }
        index++
    }

    return solution

}

fun createPacket(string: String): Packet {
    if (string == "[]") return Packet.Composed(emptyList())
    if (string.toIntOrNull() != null) return Packet.Simple(string.toInt())

    val content = string.substring(1, string.length - 1)
    var deep = 0
    val elements = mutableListOf<String>()
    var item = ""

    for (char in content) {
        when {
            char == '[' -> {
                deep++
                item += char
            }
            char == ']' -> {
                deep--
                item += char
            }
            char == ',' && deep == 0 -> {
                elements.add(item)
                item = ""
            }
            else -> item += char
        }
    }
    elements.add(item)
    return Packet.Composed(elements.map(::createPacket))
}

sealed class Packet {
    data class Simple(val value: Int) : Packet()
    data class Composed(val value: List<Packet>) : Packet()
}
