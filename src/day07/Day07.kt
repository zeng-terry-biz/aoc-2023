package day07

import println
import readInput

const val DIR = "Day07"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>) {
    fun solve(): Int {
        return input.asSequence()
                .map {
                    val (repString, bidString) = it.split(' ')
                    Hand(repString, bidString.toInt())
                }.sorted()
                .withIndex()
                .map {
                    (it.index + 1) * it.value.bid
                }.sum()
    }
}

class Part2(private val input: List<String>) {
    fun solve(): Int {
        return input.asSequence()
                .map {
                    val (repString, bidString) = it.split(' ')
                    Hand(repString, bidString.toInt(), true)
                }.sorted()
                .withIndex()
                .map {
                    (it.index + 1) * it.value.bid
                }.sum()
    }
}

private enum class Card { None, Joker, N2, N3, N4, N5, N6, N7, N8, N9, N10, J, Q, K, A }
private enum class HandType { HighCard, OnePair, TwoPair, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind }

private data class Hand(private val repString: String, val bid: Int, val jAsJoker: Boolean = false) :
        Comparable<Hand> {
    private val cards: List<Card>
    private val type: HandType

    init {
        cards = repString.map {
            val card = when (it) {
                '2' -> Card.N2
                '3' -> Card.N3
                '4' -> Card.N4
                '5' -> Card.N5
                '6' -> Card.N6
                '7' -> Card.N7
                '8' -> Card.N8
                '9' -> Card.N9
                'T' -> Card.N10
                'J' -> if (jAsJoker) Card.Joker else Card.J
                'Q' -> Card.Q
                'K' -> Card.K
                'A' -> Card.A
                else -> Card.None
            }
            card
        }

        val wildCount = cards.count { it == Card.Joker }
        val counts = cards.filterNot { it == Card.Joker }.groupingBy { it }.eachCount().values

        val baseStrength = when {
            5 in counts -> HandType.FiveOfKind
            4 in counts -> HandType.FourOfKind
            3 in counts && 2 in counts -> HandType.FullHouse
            3 in counts -> HandType.ThreeOfKind
            counts.count { it == 2 } == 2 -> HandType.TwoPair
            counts.count { it == 2 } == 1 -> HandType.OnePair
            else -> HandType.HighCard
        }

        type = when (wildCount) {
            1 -> when (baseStrength) {
                HandType.FourOfKind -> HandType.FiveOfKind
                HandType.ThreeOfKind -> HandType.FourOfKind
                HandType.TwoPair -> HandType.FullHouse
                HandType.OnePair -> HandType.ThreeOfKind
                else -> HandType.OnePair
            }

            2 -> when (baseStrength) {
                HandType.ThreeOfKind -> HandType.FiveOfKind
                HandType.OnePair -> HandType.FourOfKind
                else -> HandType.ThreeOfKind
            }

            3 -> when (baseStrength) {
                HandType.OnePair -> HandType.FiveOfKind
                else -> HandType.FourOfKind
            }

            4 -> HandType.FiveOfKind
            5 -> HandType.FiveOfKind
            else -> baseStrength
        }
    }

    override fun compareTo(other: Hand): Int {
        val typeComparison = type.compareTo(other.type)

        return if (typeComparison != 0) typeComparison else {
            val differentPair = cards.zip(other.cards).firstOrNull { it.first != it.second }
            differentPair?.first?.compareTo(differentPair.second) ?: 0
        }
    }
}
