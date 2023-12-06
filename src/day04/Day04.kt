package day04

import println
import readInput
import kotlin.math.pow

const val DIR = "Day04"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val cards = parseCards(input)

    fun solve(): Int {
        return cards.sumOf { it.points.toInt() }
    }
}

class Part2(input: List<String>) {
    private val cards = parseCards(input)

    fun solve(): Int {
        for ((idx, card) in cards.withIndex()) {
            for (k in idx + 1..(idx + card.matchingNumbersCount).coerceAtMost(cards.size - 1)) {
                cards[k].addCopies(card.count)
            }
        }

        return cards.sumOf { it.count }
    }
}

private fun parseCards(input: List<String>): List<Card> {
    return buildList {
        for (line in input) {
            val cleanLine = line.dropWhile { it != ':' }.removePrefix(": ")
            val numberStrings = cleanLine.split('|')
            this.add(
                Card(numberStrings[0].split(' ').filter { it.isNotEmpty() }.map { it.toInt() },
                    numberStrings[1].split(' ').filter { it.isNotEmpty() }.map { it.toInt() })
            )
        }
    }
}

private data class Card(val winningNumbers: List<Int>, val scratchedNumbers: List<Int>, var count: Int = 1) {
    val matchingNumbersCount = scratchedNumbers.intersect(winningNumbers.toSet()).count()
    val points = 2.0F.pow(matchingNumbersCount - 1)

    fun addCopies(n: Int) {
        count += n
    }
}
