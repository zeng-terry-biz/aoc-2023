package day02

import println
import readInput

const val DIR = "Day02"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>) {
    fun solve(): Int {
        return input.sumOf { it.qualifiedGameIndex() }
    }

    private fun String.qualifiedGameIndex(): Int {
        val subStrings = split(":", " ", ",", ";")

        var qualified = false
        for ((idx, s) in subStrings.withIndex()) {
            qualified = when (s) {
                "red" -> subStrings[idx - 1].toInt() <= Limit.RED.value
                "green" -> subStrings[idx - 1].toInt() <= Limit.GREEN.value
                "blue" -> subStrings[idx - 1].toInt() <= Limit.BLUE.value
                else -> true
            }

            if (!qualified) {
                break
            }
        }

        return if (qualified) subStrings[1].toInt() else 0
    }
}

class Part2(private val input: List<String>) {
    fun solve(): Int {
        return input.sumOf { it.gamePower() }
    }

    private fun String.gamePower(): Int {
        val subStrings = split(":", " ", ",", ";")

        var maxRed = 0
        var maxGreen = 0
        var maxBlue = 0

        for ((idx, s) in subStrings.withIndex()) {
            when (s) {
                "red" -> maxRed = higher(subStrings[idx - 1], maxRed)
                "green" -> maxGreen = higher(subStrings[idx - 1], maxGreen)
                "blue" -> maxBlue = higher(subStrings[idx - 1], maxBlue)
            }
        }

        return maxRed * maxGreen * maxBlue
    }

    private fun higher(s: String, n: Int): Int {
        val sn = s.toInt()
        return if (sn > n) sn else n
    }
}

enum class Limit(val value: Int) {
    RED(12),
    GREEN(13),
    BLUE(14),
}
