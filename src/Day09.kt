import kotlin.math.abs

fun main() {

    fun part1(input: List<String>): Int {
        val moves = input.map { move -> move.split(" ").let { it.first() to it.last().toInt() } }

        var head = 0 to 0
        var tail = 0 to 0
        val visitedPosition = mutableSetOf(head)

        for (move in moves) {
            when (move.first) {
                "R" -> repeat(move.second) {
                    head = head.first + 1 to head.second
                    tail = tail follows head
                    visitedPosition.add(tail)
                }
                "U" -> repeat(move.second) {
                    head = head.first to head.second - 1
                    tail = tail follows head
                    visitedPosition.add(tail)

                }
                "L" -> repeat(move.second) {
                    head = head.first - 1 to head.second
                    tail = tail follows head
                    visitedPosition.add(tail)

                }
                else -> repeat(move.second) {
                    head = head.first to head.second + 1
                    tail = tail follows head
                    visitedPosition.add(tail)
                }
            }
        }

        return visitedPosition.size
    }

    fun part2(input: List<String>): Int {
        val moves = input.map { move -> move.split(" ").let { it.first() to it.last().toInt() } }

        var head = 0 to 0
        var nodes = Array(9) { (0 to 0) }
        val visitedPosition = mutableSetOf(head)

        for (move in moves) {
            when (move.first) {
                "R" -> repeat(move.second) {
                    head = head.first + 1 to head.second

                    for (i in nodes.indices) {
                        if (i == 0) {
                            nodes[i] = nodes[i] follows head
                        } else {
                            nodes[i] = nodes[i] follows nodes[i - 1]
                        }

                        if (i == nodes.lastIndex) {
                            visitedPosition.add(nodes[i])
                        }
                    }
                }
                "U" -> repeat(move.second) {
                    head = head.first to head.second + 1
                    for (i in nodes.indices) {
                        if (i == 0) {
                            nodes[i] = nodes[i] follows head
                        } else {
                            nodes[i] = nodes[i] follows nodes[i - 1]
                        }

                        if (i == nodes.lastIndex) {
                            visitedPosition.add(nodes[i])
                        }
                    }
                }
                "L" -> repeat(move.second) {
                    head = head.first - 1 to head.second
                    for (i in nodes.indices) {
                        if (i == 0) {
                            nodes[i] = nodes[i] follows head
                        } else {
                            nodes[i] = nodes[i] follows nodes[i - 1]
                        }

                        if (i == nodes.lastIndex) {
                            visitedPosition.add(nodes[i])
                        }
                    }

                }
                else -> repeat(move.second) {
                    head = head.first to head.second - 1
                    for (i in nodes.indices) {
                        if (i == 0) {
                            nodes[i] = nodes[i] follows head
                        } else {
                            nodes[i] = nodes[i] follows nodes[i - 1]
                        }

                        if (i == nodes.lastIndex) {
                            visitedPosition.add(nodes[i])
                        }
                    }
                }
            }
        }

        return visitedPosition.size
    }

    val input = readInput("Day09")

    println(part1(input))
    println(part2(input))
}

private infix fun Pair<Int, Int>.follows(head: Pair<Int, Int>): Pair<Int, Int> {
    val dx = head.first - this.first
    val dy = head.second - this.second

    return if (abs(dx) <= 1 && abs(dy) <= 1) {
        this
    } else {
        val y = if (abs(dy) > 1) dy / 2 else dy
        val x = if (abs(dx) > 1) dx / 2 else dx
        this.first + x to this.second + y
    }
}
