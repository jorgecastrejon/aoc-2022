import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val start = "AA"
        val maxTime = 30
        val (rates, tunnels) = parseInput(input)
        val options = rates.filter { (name, rate) -> name == start || rate > 0 }
            .map { (name, _) -> name to calculateOptions(name, rates, tunnels) }
            .toMap()

        val finished = mutableListOf<Solution>()
        val solutions = LinkedList<Solution>().apply { add(Solution(latest = start)) }

        while (solutions.isNotEmpty()) {
            val solution = solutions.removeLast()

            options[solution.latest]?.forEach { (next, distance) ->
                val moveTime = solution.time + distance + 1

                if (moveTime < maxTime && !solution.valves.contains(next)) {
                    val valveFinalPressure = (maxTime - moveTime) * rates.getOrDefault(next, 0)
                    solutions.add(
                        Solution(
                            pressure = solution.pressure + valveFinalPressure,
                            time = moveTime,
                            latest = next,
                            valves = solution.valves + next
                        )
                    )
                } else if (solution.valves.size == options.keys.size || moveTime > maxTime) {
                    finished.add(solution)
                }
            }
        }

        return finished.maxBy(Solution::pressure).pressure
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}

private fun calculateOptions(
    start: String,
    rates: Map<String, Int>,
    tunnels: Map<String, List<String>>
): List<Pair<String, Int>> {
    val result = mutableListOf<Pair<String, Int>>()
    val visited = mutableSetOf(start)

    val queue = LinkedList<Pair<String, Int>>()
    queue.add(start to 0)

    while (queue.isNotEmpty()) {
        val (current, distance) = queue.removeLast()
        tunnels[current]
            ?.filter { it !in visited }
            ?.forEach { next ->
                visited.add(next)
                if (rates.getOrDefault(next, 0) > 0) {
                    result.add(next to distance + 1)
                }
                queue.push(next to distance + 1)
            }
    }

    return result
}

private fun parseInput(input: List<String>): Pair<Map<String, Int>, Map<String, List<String>>> {
    val rates = mutableMapOf<String, Int>()
    val tunnels = mutableMapOf<String, List<String>>()

    input.forEach { line ->
        val valve = line.split(" ", limit = 3)[1]
        val rate = line.filter(Char::isDigit).toInt()
        val flows = line.split(" to ").last().split(" ", limit = 2).last().split(", ")

        rates[valve] = rate
        tunnels[valve] = flows
    }

    return rates to tunnels
}

data class Solution(
    val pressure: Int = 0,
    val time: Int = 0,
    val latest: String,
    val valves: Set<String> = setOf(latest)
)