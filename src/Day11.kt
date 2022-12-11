fun main() {

    fun part1(input: List<String>): Long {

        return solve(monkeys = input.asMonkeys(), iterations = 20, reliefOp = { x -> x / 3 })
    }

    fun part2(input: List<String>): Long {
        val monkeys = input.asMonkeys()
        val moduloOp = monkeys.fold(1) { acc, b -> acc * b.divisibleBy }

        return solve(monkeys = input.asMonkeys(), iterations = 10000, reliefOp = { x -> x % moduloOp })
    }

    val input = readInput("Day11")
    println(part1(input))
    println(part2(input))
}

private fun List<String>.asMonkeys(): List<Monkey> =
    chunked(7)
        .map {
            val items = it[1].substringAfter(Items_Prefix).split(", ").map(String::toLong).toMutableList()
            val operationData = it[2].substringAfter(Operation_Prefix).split(" ")
            val amount = operationData.last().toLongOrNull()
            val operation = if (operationData.first() == "*")
                { x: Long -> x * (amount ?: x) }
            else
                { x: Long -> x + (amount ?: x) }
            val testOp = it[3].substringAfter(Test_Prefix).toInt()
            val trueIndex = it[4].substringAfter(Test_True_Prefix).toInt()
            val falseIndex = it[5].substringAfter(Test_False_Prefix).toInt()
            val getForwardTo = { x: Long -> if (x % testOp == 0L) trueIndex else falseIndex }

            Monkey(items = items, divisibleBy = testOp, operation = operation, getForwardTo = getForwardTo)
        }

fun solve(monkeys: List<Monkey>, iterations: Int, reliefOp: (Long) -> Long): Long {
    repeat(iterations) {
        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                val newValue = reliefOp(monkey.evaluate(item))
                val to = monkey.getForwardTo(newValue)
                monkeys[to].items.add(newValue)
            }
            monkey.items.clear()
        }
    }

    return monkeys.map(Monkey::evaluations).sortedDescending().take(2).reduce { a, b -> a * b }
}

const val Items_Prefix = "Starting items: "
const val Operation_Prefix = "Operation: new = old "
const val Test_Prefix = "Test: divisible by "
const val Test_True_Prefix = "If true: throw to monkey "
const val Test_False_Prefix = "If false: throw to monkey "

data class Monkey(
    val items: MutableList<Long>,
    val divisibleBy: Int,
    private val operation: (Long) -> (Long),
    val getForwardTo: (Long) -> Int
) {
    var evaluations: Long = 0L

    fun evaluate(value: Long): Long {
        evaluations++
        return operation(value)
    }
}
