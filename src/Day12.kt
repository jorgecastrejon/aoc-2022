import java.util.*

fun main() {

    fun part1(input: List<String>): Int {
        val (graph, start) = parseInput(input, 'S')

        val path = bfs(
            graph = graph,
            start = start,
            isAdjacentValid = { current, adj -> adj.value - current.value <= 1 },
            isDestination = { current -> current.char == 'E' }
        )

        return path.size
    }

    fun part2(input: List<String>): Int {
        val (graph, start) = parseInput(input, 'E')

        val path = bfs(
            graph = graph,
            start = start,
            isAdjacentValid = { current, adj -> current.value - adj.value <= 1 },
            isDestination = { current -> current.value == 0 }
        )

        return path.size
    }

    val input = readInput("Day12")
    println(part1(input))
    println(part2(input))
}

private typealias Graph = Map<Int, MutableMap<Int, Node>>

private data class Node(val x: Int, val y: Int, val value: Int, val char: Char)

private fun parseInput(input: List<String>, start: Char): Pair<Graph, Node> {
    val nodes = mutableMapOf<Int, MutableMap<Int, Node>>()
    var startNode: Node? = null

    input.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val node = when (c) {
                'S' -> Node(x = x, y = y, char = c, value = 0)
                'E' -> Node(x = x, y = y, char = c, value = range.indexOf('z'))
                else -> Node(x = x, y = y, char = c, value = range.indexOf(c))
            }
            if (node.char == start) startNode = node
            nodes.getOrPut(y) { mutableMapOf() }[x] = node
        }
    }

    return nodes to startNode!!
}

private fun bfs(
    graph: Graph,
    start: Node,
    isAdjacentValid: (Node, Node) -> Boolean,
    isDestination: (Node) -> Boolean
): List<Node> {
    val queue = LinkedList<Node>()
    val visited = mutableSetOf<Node>()
    val predecessors = mutableMapOf<Node, Node?>()
    val distances = mutableMapOf<Node, Int>()

    queue.push(start)
    predecessors[start] = null
    distances[start] = 0

    while (queue.isNotEmpty()) {
        val node = queue.pop()
        val nodeDistance = distances[node]!!
        visited.add(node)

        graph.getPossibleAdjacentNodes(node, isValid = isAdjacentValid)
            .filter { adj -> !visited.contains(adj) }
            .forEach { adj ->
                val distance = distances[adj]

                if (distance == null || distance > nodeDistance + 1) {
                    predecessors[adj] = node
                    distances[adj] = nodeDistance + 1
                    queue.add(adj)
                }
            }
    }
    val path = mutableListOf<Node>()
    val destination = graph.values.flatMap { it.values }
        .filter { isDestination(it) }
        .minBy { distances[it] ?: Integer.MAX_VALUE }

    var current = destination

    while (predecessors[current] != null) {
        path.add(predecessors[current]!!);
        current = predecessors[current]!!
    }

    return path
}

private fun Map<Int, MutableMap<Int, Node>>.getPossibleAdjacentNodes(
    node: Node,
    isValid: (Node, Node) -> Boolean
): List<Node> =
    listOfNotNull(
        get(node.y - 1)?.get(node.x),
        get(node.y)?.get(node.x + 1),
        get(node.y + 1)?.get(node.x),
        get(node.y)?.get(node.x - 1)
    ).filter { adj -> isValid(node, adj) }

private val range = CharRange('a', 'z')
