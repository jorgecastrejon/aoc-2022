fun main() {

    fun part1(input: List<String>): Int =
        input.parse(calculateOwnPlay = { _, own -> own.asPlay() })
            .sumOf { (opponent, your) -> your.vs(opponent).bonus + your.bonus }

    fun part2(input: List<String>): Int =
        input.parse(calculateOwnPlay = { opponent, own -> own.matchPlay(opponent) })
            .sumOf { (opponent, your) -> your.vs(opponent).bonus + your.bonus }

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

enum class Play { Rock, Paper, Scissors }

val Play.bonus: Int
    get() = ordinal + 1

val Play.winnableOpponent: Play
    get() = when (this) {
        Play.Rock -> Play.Scissors
        Play.Paper -> Play.Rock
        Play.Scissors -> Play.Paper
    }

enum class Result { Win, Loose, Draw }

val Result.bonus: Int
    get() = when (this) {
        Result.Win -> 6
        Result.Loose -> 0
        Result.Draw -> 3
    }

fun String.asPlay(): Play =
    when (this) {
        "A", "X" -> Play.Rock
        "B", "Y" -> Play.Paper
        else -> Play.Scissors
    }

fun String.matchPlay(opponent: Play): Play =
    when (asResult()) {
        Result.Win -> Play.values().first { it !in listOf(opponent, opponent.winnableOpponent) }
        Result.Loose -> opponent.winnableOpponent
        Result.Draw -> opponent
    }

fun String.asResult(): Result =
    when (this) {
        "X" -> Result.Loose
        "Y" -> Result.Draw
        else -> Result.Win
    }

fun List<String>.parse(calculateOwnPlay: (Play, String) -> Play) =
    map { round -> round.split(" ").run { first().asPlay().let { it to calculateOwnPlay(it, last()) } } }

fun Play.vs(other: Play): Result =
    when {
        this == other -> Result.Draw
        this.winnableOpponent == other -> Result.Win
        else -> Result.Loose
    }

