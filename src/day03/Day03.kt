package day03

import println
import readInput

const val DIR = "Day03"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val rowSchemes = input.mapIndexed { idx, line -> line.parseNodes(idx) }

    fun solve(): Int {
        val partNumbers = buildList {
            // First row
            this.addAll(
                rowSchemes[0].numberNodes
                    .filter { it.adjacentToAny(rowSchemes[0].symbolNodes + rowSchemes[1].symbolNodes) }
                    .map { it.value })

            // Sliding window of 3 rows
            for (threeRows in rowSchemes.windowed(3)) {
                val surroundingSymbols = threeRows.map { it.symbolNodes }.flatten()
                this.addAll(
                    threeRows[1].numberNodes
                        .filter { it.adjacentToAny(surroundingSymbols) }
                        .map { it.value })
            }

            // Last row
            val lastRow = rowSchemes.size - 1
            this.addAll(
                rowSchemes[lastRow].numberNodes
                    .filter { it.adjacentToAny(rowSchemes[lastRow - 1].symbolNodes + rowSchemes[lastRow].symbolNodes) }
                    .map { it.value })
        }

        return partNumbers.sumOf { it }
    }
}

class Part2(input: List<String>) {
    private val rowSchemes = input.mapIndexed { idx, line -> line.parseNodes(idx) }

    fun solve(): Int {
        val gearRatios = buildList {
            // First row
            this.addAll(
                rowSchemes[0].symbolNodes
                    .filter { it.isGear }
                    .map { it.gearRatio(rowSchemes[0].numberNodes + rowSchemes[1].numberNodes) })

            // Sliding window of 3 rows
            for (threeRows in rowSchemes.windowed(3)) {
                val surroundingNumbers = threeRows.map { it.numberNodes }.flatten()
                this.addAll(
                    threeRows[1].symbolNodes
                        .filter { it.isGear }
                        .map { it.gearRatio(surroundingNumbers) })
            }

            // Last row
            val lastRow = rowSchemes.size - 1
            this.addAll(
                rowSchemes[lastRow].symbolNodes
                    .filter { it.isGear }
                    .map { it.gearRatio(rowSchemes[lastRow - 1].numberNodes + rowSchemes[lastRow].numberNodes) })
        }

        return gearRatios.sumOf { it }
    }
}

data class NumberNode(val value: Int, val row: Int, val col: Int, val len: Int = 1) {
    private val rowRange = row - 1..row + 1
    private val colRange = col - 1..col + len

    fun adjacentToAny(symbolNodes: List<SymbolNode>): Boolean {
        return symbolNodes.any { adjacentTo(it) }
    }

    fun adjacentTo(symbolNode: SymbolNode) = symbolNode.row in rowRange && symbolNode.col in colRange
}

data class SymbolNode(val value: Char, val row: Int, val col: Int) {
    val isGear = value == '*'

    fun gearRatio(numberNodes: List<NumberNode>): Int {
        if (!isGear) return 0

        val adjacentNumbers = numberNodes.filter { it.adjacentTo(this) }

        return if (adjacentNumbers.size == 2) adjacentNumbers.map { it.value }.reduce(Int::times) else 0
    }
}

data class Schematic(val numberNodes: List<NumberNode>, val symbolNodes: List<SymbolNode>)

private fun String.parseNodes(row: Int): Schematic {
    val numberNodes = mutableListOf<NumberNode>()
    val symbolNodes = mutableListOf<SymbolNode>()

    var start = -1
    for ((idx, c) in this.withIndex()) {
        when {
            c.isDigit() -> {
                if (start == -1) {
                    start = idx
                }
            }

            c == '.' -> {
                if (start != -1) {
                    numberNodes.add(NumberNode(substring(start, idx).toInt(), row, start, idx - start))
                    start = -1
                }
            }

            else -> {
                if (start != -1) {
                    numberNodes.add(NumberNode(substring(start, idx).toInt(), row, start, idx - start))
                    start = -1
                }
                symbolNodes.add(SymbolNode(c, row, idx))
            }
        }
    }

    // The line ends with a number
    if (start != -1) {
        val idx = length
        numberNodes.add(NumberNode(substring(start, idx).toInt(), row, start, idx - start))
    }

    return Schematic(numberNodes, symbolNodes)
}
