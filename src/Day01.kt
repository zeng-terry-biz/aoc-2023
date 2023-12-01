fun main() {

    // PART 1

    fun String.getNumber() = first{it.isDigit()}.digitToInt() * 10 + last{it.isDigit()}.digitToInt()

    fun part1(input: List<String>): Int {
        var sum = 0

        for (line in input){
            sum += line.getNumber()
        }

        return sum
    }

    var testInput = readInput("Day01_01test")
    check(part1(testInput) == 142)

    // PART 2

    val strToInt= mapOf(
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

    fun getStrNumber(line: String) : Int{
        val firstFound = line.findAnyOf(strToInt.keys)
        val firstNum = if(firstFound != null) strToInt[firstFound.second] else 0

        val lastFound = line.findLastAnyOf(strToInt.keys)
        val lastNum = if(lastFound != null) strToInt[lastFound.second] else 0

        check(firstNum != null && lastNum != null)
        return firstNum * 10 + lastNum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (line in input){
            sum += getStrNumber(line)
        }

        return sum
    }

    testInput = readInput("Day01_02test")
    check(part2(testInput) == 281)

    // PARTS RUN

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
