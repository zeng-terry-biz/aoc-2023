package day06

import println
import readInput
import kotlin.math.sqrt

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
        return parseRaceRecord(input).betterCount()
    }

    private fun parseRaceRecord(input: List<String>) = RaceRecord(
            input.first().filter { it.isDigit() }.toLong(),
            input.last().filter { it.isDigit() }.toLong()
                                                                 )
}

private data class RaceRecord(val time: Long, val distance: Long) {
    fun betterCount(): Int {
        // For the record distance, its time t satisfies t^2 - time * t + distance = 0.
        // We use the smaller of the two roots as the time use to produce the distance
        val t1 = ((time - sqrt(time * time - 4.0 * distance)) / 2.0).toInt()

        // The longest distance is produced by time/2 (tip).
        // Each second between (t1, tip] and [tip, t2) will produce a better distance.
        // Note that tip only counts for one better distance if time is an even number.
        val offset = if (time % 2 == 0L) 1 else 0
        return ((time / 2 - t1) * 2 - offset).toInt()
    }
}
