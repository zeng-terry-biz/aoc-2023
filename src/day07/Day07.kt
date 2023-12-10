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

private enum class Card { None, J0, N2, N3, N4, N5, N6, N7, N8, N9, N10, J, Q, K, A }
private enum class HandType { HighCard, OnePair, TwoPair, ThreeOfKind, FullHouse, FourOfKind, FiveOfKind }

private data class Hand(private val repString: String, val bid: Int, val jAsJoker: Boolean = false) :
        Comparable<Hand> {
    private val cards: List<Card>
    private val type: HandType

    init {
        val map = mutableMapOf<Card, Int>()

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
                'J' -> if (jAsJoker) Card.J0 else Card.J
                'Q' -> Card.Q
                'K' -> Card.K
                'A' -> Card.A
                else -> Card.None
            }
            map[card] = map[card]?.plus(1) ?: 1
            card
        }

        val wildCount = map[Card.J0] ?: 0

        type = when (wildCount) {
            0 -> when {
                map.any { it.value == 5 } -> HandType.FiveOfKind
                map.any { it.value == 4 } -> HandType.FourOfKind
                map.count { it.value == 3 } == 1 && map.count { it.value == 2 } == 1 -> HandType.FullHouse
                map.count { it.value == 3 } == 1 -> HandType.ThreeOfKind
                map.count { it.value == 2 } == 2 -> HandType.TwoPair
                map.count { it.value == 2 } == 1 -> HandType.OnePair
                else -> HandType.HighCard
            }

            1 -> when {
                map.any { it.value == 4 } -> HandType.FiveOfKind
                map.any { it.value == 3 } -> HandType.FourOfKind
                map.count { it.value == 2 } == 2 -> HandType.FullHouse
                map.count { it.value == 2 } == 1 -> HandType.ThreeOfKind
                else -> HandType.OnePair
            }

            2 -> when {
                map.any { it.value == 3 } -> HandType.FiveOfKind
                map.any { it.key != Card.J0 && it.value == 2 } -> HandType.FourOfKind
                else -> HandType.ThreeOfKind
            }

            3 -> when {
                map.any { it.value == 2 } -> HandType.FiveOfKind
                else -> HandType.FourOfKind
            }

            4 -> HandType.FiveOfKind
            5 -> HandType.FiveOfKind
            else -> HandType.HighCard // Can't reach
        }

        println("$repString, $type")
    }

    override fun compareTo(other: Hand): Int {
        if (type > other.type) return 1
        else if (this.type < other.type) return -1
        else {
            for ((index, card) in cards.withIndex()) {
                return if (card > other.cards[index]) 1
                else if (card < other.cards[index]) -1
                else continue
            }
            return 0
        }
    }
}
