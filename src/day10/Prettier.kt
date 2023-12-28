package day10

import readInput
import kotlin.io.path.Path
import kotlin.io.path.writeLines


fun main() {
    val fileName = "input"
    val prettier = Prettier(readInput("day10/$fileName"))
    Path("src/day10/${fileName}_pretty.txt").writeLines(prettier.output)
}

class Prettier(val input: List<String>) {
    val output = input.map {
        it.map { c ->
            when (c) {
                '|' -> '\u2502'
                '-' -> '\u2500'
                'L' -> '\u2514'
                'J' -> '\u2518'
                '7' -> '\u2510'
                'F' -> '\u250C'
                '.' -> '.'
                'S' -> 'S'
                else -> '.'
            }
        }
    }.map { String(it.toCharArray()) }
}
