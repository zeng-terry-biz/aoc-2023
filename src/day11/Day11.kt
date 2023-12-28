package day11

import println
import readInput
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

const val DIR = "Day11"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val sky = NearSky(input)

    fun solve() = sky.getSumPaths()
}

class Part2(input: List<String>) {
    private val sky = FarSky(input, 1000000)

    fun solve() = sky.getSumPaths()
}

private data class Loc(val row: Int, val col: Int)

private class NearSky(input: List<String>) {
    val skyMap = input.map { line -> line.map { it }.toMutableList() }.toMutableList()

    init {
        val nCols = skyMap[0].size

        // Expand rows
        skyMap.mapIndexed { i, line -> if (line.any { it == '#' }) -1 else i }.filter { it != -1 }
                .forEachIndexed { i, row -> skyMap.add(i + row, MutableList(nCols) { '.' }) }

        // Expand columns
        (0 ..< nCols).filter { j -> skyMap.all { line -> line[j] != '#' } }
                .forEachIndexed { j, col -> skyMap.forEach { line -> line.add(j + col, '.') } }
    }

    private fun getGalaxies(): List<Loc> {
        return skyMap.mapIndexed { i, line -> line.mapIndexed { j, c -> if (c == '#') Loc(i, j) else null } }.flatten()
                .filterNotNull()
    }

    fun getSumPaths(): Int {
        val galaxies = getGalaxies()
        return galaxies.sumOf { me ->
            galaxies.sumOf { other ->
                abs(me.row - other.row) + abs(me.col - other.col)
            }
        } / 2
    }
}

private class FarSky(input: List<String>, val expansionFactor: Long) {
    val skyMap = input.map { line -> line.map { it } }
    val emptyRows: List<Int> =
        skyMap.mapIndexed { i, line -> if (line.any { it == '#' }) -1 else i }.filter { it != -1 }
    val emptyCols: List<Int> = (0 ..< skyMap[0].size).filter { j -> skyMap.all { line -> line[j] == '.' } }

    private fun getGalaxies(): List<Loc> {
        return skyMap.mapIndexed { i, line -> line.mapIndexed { j, c -> if (c == '#') Loc(i, j) else null } }.flatten()
                .filterNotNull()
    }

    fun getSumPaths(): Long {
        val galaxies = getGalaxies()

        return galaxies.sumOf { me ->
            galaxies.sumOf { other ->
                val eRows = emptyRows.count { it in min(me.row, other.row) .. max(me.row, other.row) }
                val eCols = emptyCols.count { it in min(me.col, other.col) .. max(me.col, other.col) }

                abs(me.row - other.row) + abs(me.col - other.col) - eRows - eCols +
                        eRows * expansionFactor +
                        eCols * expansionFactor
            }
        } / 2
    }
}
