package day10

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import println
import readInput

const val DIR = "Day10"

fun main() {
    //    Part1(readInput("$DIR/test_part1")).solve().println()
    //    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part2")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val sketch = Sketch(input)

    fun solve(): Int {
        return (sketch.findLoops().maxOf { it.size }) / 2
    }
}

class Part2(input: List<String>) {
    private val sketch = Sketch(input)

    fun solve(): Int {
        val loop = sketch.findLoops().reduce { cur, next -> if (cur.size < next.size) next else cur }
        return calculateEnclosed(loop)
    }

    private fun calculateEnclosed(loop: List<Pipe>): Int {
        val locList = loop.map { it.loc }

        // Find the top-left corner of the loop
        val top = locList.map { it.row }.min()
        val topLeft = locList.filter { it.row == top }.reduce { cur, next -> if (cur.col < next.col) cur else next }

        // Re-arrange the list to start in the top-left corner
        val startIndex = locList.indexOf(topLeft)
        val normLoop = locList.subList(startIndex, locList.size) + locList.subList(0, startIndex)

        // Determine the looping direction
        val clockwise = normLoop[1].row == topLeft.row

        val enclosed = buildSet<Loc> {
            for ((i, loc) in normLoop.withIndex()) {
                val prev = if (i == 0) normLoop.last() else normLoop[i - 1]
                val next = if (i == normLoop.size - 1) normLoop.last() else normLoop[i + 1]

                // Taking both come and go into consideration.  A straight line will have two
                // identical directions to search, while a corner will have two different ones.
                val connects = listOf<Connect>(
                        getEdgeOffDirection(prev, loc, clockwise),
                        getEdgeOffDirection(loc, next, clockwise)
                                              )

                for (connect in connects) {
                    // Traverse from the edge towards inside, until reaching another edge or a previously visited tile
                    var current: Loc? = loc
                    while (current != null) {
                        current = current.neighbor(connect, sketch.maxRow, sketch.maxCol)
                        if (current == null || current in normLoop) break

                        this.add(current)
                    }

                    // Error checking.  Current should never be null (meaning reaching the edge of sketch)
                    if (current == null) {
                        println("Error: Should never reach sketch edge but just did.")
                        break
                    }
                }
            }
        }

        return enclosed.size
    }

    private fun getEdgeOffDirection(from: Loc, to: Loc, clockwise: Boolean): Connect {
        if (clockwise) return when {
            from.row < to.row -> Connect.Left
            from.row > to.row -> Connect.Right
            from.col < to.col -> Connect.Down
            else -> Connect.Up
        } else
            return when {
                from.row < to.row -> Connect.Right
                from.row > to.row -> Connect.Left
                from.col < to.col -> Connect.Up
                else -> Connect.Down
            }
    }


}

private class Sketch(input: List<String>) {
    private val sketch: List<List<Pipe>> = input.mapIndexed { i, line -> line.mapIndexed { j, c -> Pipe(c, i, j) } }
    private val start: Pipe = sketch.flatten().first { it.type == 'S' }
    val maxRow: Int = sketch.size - 1
    val maxCol: Int = sketch[0].size - 1

    fun findLoops(): List<List<Pipe>> {
        val startingConnections = start.connects.mapNotNull { findNext(start, it) }

        return runBlocking(Dispatchers.Default) {
            startingConnections.map {
                async {
                    buildList {
                        this.add(start)
                        var current: PipeConnect? = it

                        do {
                            this.add(current!!.pipe)
                            current = findNext(current.pipe, current.outward)

                        } while (current != null && current.pipe.type != 'S')

                        if (current == null) {
                            //                            println("Clearing list")
                            this.clear()
                        }
                    }
                }
            }.awaitAll()
        }
    }

    private fun findNext(pipe: Pipe, outward: Connect): PipeConnect? {
        val nextLoc = pipe.loc.neighbor(outward, maxRow, maxCol) ?: return null

        val nextPipe = sketch[nextLoc.row][nextLoc.col]
        val nextOutward = nextPipe.connectOut(outward)

        return if (nextOutward != Connect.None) PipeConnect(nextPipe, nextOutward) else null
    }
}

private enum class Connect {
    None,
    Up,
    Down,
    Left,
    Right
}

private data class Loc(val row: Int, val col: Int) {
    fun neighbor(connect: Connect, maxRow: Int, maxCol: Int): Loc? =
        when (connect) {
            Connect.Up -> if (row > 0) Loc(row - 1, col) else null
            Connect.Down -> if (row < maxRow) Loc(row + 1, col) else null
            Connect.Left -> if (col > 0) Loc(row, col - 1) else null
            Connect.Right -> if (col < maxCol) Loc(row, col + 1) else null
            else -> null
        }

    override fun toString(): String {
        return "(r:$row, c:$col)"
    }
}

private data class Pipe(val type: Char, val row: Int, val col: Int) {
    val loc = Loc(row, col)
    val connects = when (type) {
        '|' -> listOf(Connect.Up, Connect.Down)
        '-' -> listOf(Connect.Left, Connect.Right)
        'L' -> listOf(Connect.Up, Connect.Right)
        'J' -> listOf(Connect.Up, Connect.Left)
        '7' -> listOf(Connect.Left, Connect.Down)
        'F' -> listOf(Connect.Right, Connect.Down)
        '.' -> emptyList()
        'S' -> listOf(Connect.Up, Connect.Down, Connect.Left, Connect.Right)
        else -> emptyList()
    }

    fun connectOut(inward: Connect): Connect {
        // Find connect that would take the inward
        val index = when (inward) {
            Connect.Up -> connects.indexOf(Connect.Down)
            Connect.Down -> connects.indexOf(Connect.Up)
            Connect.Left -> connects.indexOf(Connect.Right)
            Connect.Right -> connects.indexOf(Connect.Left)
            else -> -1
        }

        // Return the other connect as the outward
        return when (index) {
            0 -> connects[1]
            1 -> connects[0]
            2 -> connects[0] // For 'S' only,
            3 -> connects[0] // any none-None value would work
            else -> Connect.None
        }
    }
}

private data class PipeConnect(val pipe: Pipe, val outward: Connect)
