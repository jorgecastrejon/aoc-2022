fun main() {

    fun part1(input: List<String>): Int =
        input.map { pairs -> pairs.getAssignments() }
            .count { (a, b, c, d) -> a in c..d && b in c..d || c in a..b && d in a..b }


    fun part2(input: List<String>): Int =
        input.map { pairs -> pairs.getAssignments() }
            .count { (a, b, c, d) -> (a..b).intersect(c..d).isNotEmpty() }


    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

fun String.getAssignments(): Assignment =
    replace(",", "-").split("-")
        .let { elements ->
            Assignment(elements[0].toInt(), elements[1].toInt(), elements[2].toInt(), elements[3].toInt())
        }

data class Assignment(val a: Int, val b: Int, val c: Int, val d: Int)