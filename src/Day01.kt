fun main() {
    fun String.getNumber() = first{it.isDigit()}.digitToInt() * 10 + last{it.isDigit()}.digitToInt()

    fun part1(input: List<String>): Int {
        var sum = 0

        for (line in input){
            sum += line.getNumber()
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    var testInput = readInput("Day01_01test")
    check(part1(testInput) == 142)

    val strToInt= mapOf(
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
        val firstPos = line.indexOfFirst { it.isDigit() }
        var firstNum = if(firstPos == -1) -1 else line[firstPos].digitToInt()

        val firstFound = line.findAnyOf(strToInt.keys)
        if(firstFound != null && (firstNum == -1 || firstFound.first < firstPos)){
                firstNum = strToInt[firstFound.second]!!
        }

        val lastPos = line.indexOfLast { it.isDigit() }
        var lastNum = if(lastPos == -1) -1 else line[lastPos].digitToInt()

        val lastFound = line.findLastAnyOf(strToInt.keys)
        if(lastFound != null && (lastNum == -1 || lastFound.first > lastPos)){
            lastNum = strToInt[lastFound.second]!!
        }

        check(firstNum != -1 && lastNum != -1)
        return firstNum * 10 + lastNum
    }

    fun part2(input: List<String>): Int {
        var sum = 0

        for (line in input){
            sum += getStrNumber(line)
        }

        return sum
    }

    // test if implementation meets criteria from the description, like:
    testInput = readInput("Day01_02test")
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
