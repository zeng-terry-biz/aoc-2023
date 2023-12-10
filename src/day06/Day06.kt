package day06

import println
import readInput

const val DIR = "Day06"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>) {
    fun solve(): Int {
        return parseRaceRecords(input).map { it.betterCount() }.reduce(Int::times)
    }

    private fun parseRaceRecords(input: List<String>): List<RaceRecord> {
        val times =
            input.first().substringAfter("Time:").split("  ").filter { it.isNotBlank() }.map { it.trim().toLong() }
        val distances =
            input.last().substringAfter("Distance:").trim().split("  ").filter { it.isNotBlank() }
                .map { it.trim().toLong() }

        return times.zip(distances) { time, distance -> RaceRecord(time, distance) }
    }
}

class Part2(private val input: List<String>) {
    fun solve(): Int {
        return parseRaceRecord(input).also { it.println() }.betterCount()
    }

    private fun parseRaceRecord(input: List<String>) = RaceRecord(
        input.first().filter { it.isDigit() }.toLong(),
        input.last().filter { it.isDigit() }.toLong()
    )
}

private data class RaceRecord(val time: Long, val distance: Long) {
    fun betterCount(): Int {
        return (1..time).mapNotNull {
            if (it * (time - it) > distance) it else null
        }.count()
    }
}
