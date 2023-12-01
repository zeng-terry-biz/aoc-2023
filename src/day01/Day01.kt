package day01

import println
import readInput

const val DIR = "Day01"

fun main() {
    Part1(readInput("$DIR/test_part1")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part2")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(private val input: List<String>){
    fun solve() : Int{
        return input.sumOf { it.getNumber() }
    }

    private fun String.getNumber() = first{it.isDigit()}.digitToInt() * 10 + last{it.isDigit()}.digitToInt()
}

class Part2(private val input: List<String>){
    fun solve() : Int{
        return input.sumOf { it.getNumber() }
    }

    private val strToInt= mapOf(
        "1" to 1,
        "2" to 2,
        "3" to 3,
        "4" to 4,
        "5" to 5,
        "6" to 6,
        "7" to 7,
        "8" to 8,
        "9" to 9,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9,
    )

    private fun String.getNumber() : Int{
        val firstFound = findAnyOf(strToInt.keys)
        val lastFound = findLastAnyOf(strToInt.keys)

        check(firstFound != null && lastFound != null)
        return strToInt[firstFound.second]!! * 10 + strToInt[lastFound.second]!!
    }
}
