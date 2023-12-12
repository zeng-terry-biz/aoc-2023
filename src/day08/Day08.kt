package day08

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import println
import readInput
import kotlin.math.max
import kotlin.math.min

const val DIR = "Day08"

fun main() {
    Part1(readInput("$DIR/test_part1")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part2")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val moves: String = input.first()
    private val nodeMap: Map<String, Node> = parseNodes(input.drop(2))

    fun solve(): Int {
        var step = 0
        var current = "AAA"

        while (current != "ZZZ") {
            current = when (moves[step % moves.length]) {
                'L' -> nodeMap[current]!!.left
                'R' -> nodeMap[current]!!.right
                else -> current  // not reachable
            }

            step++
        }

        return step
    }
}

class Part2(input: List<String>) {
    private val moves: String = input.first()
    private val nodeMap: Map<String, Node> = parseNodes(input.drop(2))

    fun solve(): Long {
        val currents = nodeMap.keys.filter { it.endsWith('A') }

        val steps = runBlocking(Dispatchers.Default) {
            currents.map {
                async {
                    var step = 0
                    var cur = it
                    while (!cur.endsWith('Z')) {
                        cur = when (moves[(step % moves.length)]) {
                            'L' -> nodeMap[cur]!!.left
                            'R' -> nodeMap[cur]!!.right
                            else -> cur  // not reachable
                        }
                        step++
                    }
                    step.toLong()
                }
            }.awaitAll()
        }

        return steps.reduce { acc, next -> findLCM(acc, next) }
    }
}

private data class Node(val name: String, val left: String, val right: String)

private fun parseNodes(lines: List<String>): Map<String, Node> {
    return lines.map {
        val (p, l, r) = it.split(" = (", ", ", ")")
        Node(p, l, r)
    }.associateBy {
        it.name
    }
}

private fun findLCM(a: Long, b: Long): Long {
    val larger = max(a, b)
    val smaller = min(a, b)
    val maxLCM = a * b

    var lcm = larger

    while (lcm <= maxLCM) {
        if (lcm % smaller == 0L) return lcm
        lcm += larger
    }

    return maxLCM
}
