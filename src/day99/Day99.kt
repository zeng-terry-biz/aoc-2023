package day99

import println
import readInput

const val DIR = "Day99"

fun main() {
    Part1(readInput("$DIR/test_part1")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part2")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>){
    fun solve() : Int{
        return input.size
    }
}

class Part2(private val input: List<String>){
    fun solve() : Int{
        return input.size
    }
}
