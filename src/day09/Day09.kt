package day09

import println
import readInput

const val DIR = "Day09"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>) {
    fun solve(): Int {
        return input.map { line -> line.split(' ').map { it.toInt() } }
                .map { Processor(it) }
                .sumOf { it.nextValue() }
    }
}

class Part2(private val input: List<String>) {
    fun solve(): Int {
        return input.map { line -> line.split(' ').map { it.toInt() } }
                .map { Processor(it) }
                .sumOf { it.prevValue() }
    }
}

private class Processor(private val initial: List<Int>) {
    val dissections: List<List<Int>> = buildList {

        var current = initial

        while (current.any { it != 0 }) {
            this.add(current)

            current = buildList {
                for ((index, n) in current.withIndex()) {
                    if (index > 0) this.add(n - current[index - 1])
                }
            }
        }
    }

    fun nextValue(): Int = dissections.sumOf { it.last() }

    fun prevValue(): Int = dissections.map { it.first() }.mapIndexed { index, n -> if (index % 2 == 0) n else -n }.sum()
}
