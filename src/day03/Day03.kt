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
    private val schematic = parseSchematic(input)

    fun solve(): Int {
        return schematic.numberNodes.filter { it.adjacentToAny(schematic.symbolNodes) }.sumOf { it.value }
    }
}

class Part2(input: List<String>) {
    private val schematic = parseSchematic(input)

    fun solve(): Int {
        return schematic.symbolNodes.filter { it.isGear }.sumOf { it.gearRatio(schematic.numberNodes) }
    }
}

data class NumberNode(val value: Int, val row: Int, val col: Int, val len: Int = 1) {
    fun adjacentToAny(symbolNodes: List<SymbolNode>): Boolean {
        return symbolNodes.any { adjacentTo(it) }
    }

    fun adjacentTo(symbolNode: SymbolNode): Boolean {
        return symbolNode.row in row - 1..row + 1 &&
                symbolNode.col in col - 1..col + len
    }
}

data class SymbolNode(val value: Char, val row: Int, val col: Int) {
    val isGear = value == '*'

    fun gearRatio(numberNodes: List<NumberNode>): Int {
        if (!isGear) return 0

        val adjacentNumbers = numberNodes.filter { it.adjacentTo(this) }

        return if (adjacentNumbers.size == 2) adjacentNumbers.fold(1) { product, node -> product * node.value } else 0
    }
}

data class Schematic(val numberNodes: List<NumberNode>, val symbolNodes: List<SymbolNode>)

private fun parseSchematic(input: List<String>): Schematic {
    return input.mapIndexed { idx, line -> line.parseNodes(idx) }
        .reduce { merged, next ->
            Schematic(
                merged.numberNodes + next.numberNodes,
                merged.symbolNodes + next.symbolNodes
            )
        }
}

private fun String.parseNodes(row: Int): Schematic {
    val numberNodes = mutableListOf<NumberNode>()
    val symbolNodes = mutableListOf<SymbolNode>()

    var start = -1
    for ((idx, c) in this.withIndex()) {
        when {
            c == '.' -> {
                if (start != -1) {
                    numberNodes.add(NumberNode(substring(start, idx).toInt(), row, start, idx - start))
                    start = -1
                }
            }

            c.isDigit() -> {
                if (start == -1) {
                    start = idx
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
