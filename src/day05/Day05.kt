package day05

import println
import readInput

const val DIR = "Day05"

fun main() {
    Part1(readInput("$DIR/test_part")).solve().println()
    Part1(readInput("$DIR/input")).solve().println()

    Part2(readInput("$DIR/test_part")).solve().println()
    Part2(readInput("$DIR/input")).solve().println()
}

class Part1(input: List<String>) {
    private val seeds = parseSeeds(input[0])
    private val gardnerMaps = parseMaps(input.subList(2, input.size))

    fun solve(): Long {
        return seeds.minOfOrNull { it.seedToLocation(gardnerMaps) } ?: -1
    }

    private fun parseSeeds(line: String): List<Long> {
        val cleanLine = line.dropWhile { it != ':' }.removePrefix(": ")
        return cleanLine.split(' ').map { it.toLong() }
    }
}

class Part2(input: List<String>) {
    private val seedRanges = parseSeedRanges(input[0])
    private val gardnerMaps = parseMaps(input.subList(2, input.size))

    fun solve(): Long {
        return seedRanges.minOfOrNull {
            var min = Long.MAX_VALUE

            for (n in it.first..<it.first + it.second) {
                min = min.coerceAtMost(n.seedToLocation(gardnerMaps))
            }
            min
        } ?: -1
    }

    private fun parseSeedRanges(line: String): List<Pair<Long, Long>> {
        val cleanLine = line.dropWhile { it != ':' }.removePrefix(": ")

        return cleanLine.split(' ').map { it.toLong() }
            .chunked(2).filter { it.size == 2 }
            .map { Pair(it[0], it[1]) }
    }
}

private fun parseMaps(input: List<String>): List<GardnerMap> {
    val regex = """([a-z]+)-to-([a-z]+) map:""".toRegex()

    var start = false
    var sourceName = ""
    var destName = ""
    val mappingRanges = mutableListOf<MappingRange>()

    val gardnerMaps = mutableListOf<GardnerMap>()

    for (line in input) {
        when {
            regex.matches(line) -> {
                val match = regex.find(line)
                sourceName = match?.groups?.get(1)?.value ?: "Invalid"
                destName = match?.groups?.get(2)?.value ?: "Invalid"
                start = true
            }

            line.isEmpty() && start -> {
                gardnerMaps.add(GardnerMap(sourceName, destName, mappingRanges.toList()))
                start = false
                mappingRanges.clear()
            }

            else -> {
                val numbers = line.split(' ').map { it.toLong() }
                mappingRanges.add(MappingRange(numbers[1], numbers[0], numbers[2]))
            }
        }
    }

    // end of file
    if (start) {
        gardnerMaps.add(GardnerMap(sourceName, destName, mappingRanges.toList()))
    }

    return gardnerMaps
}

private fun Long.seedToLocation(gardnerMaps: List<GardnerMap>): Long {
    var sourceName = "seed"
    var source = this

    while (sourceName != "location") {
        val gardenerMap = gardnerMaps.firstOrNull { it.sourceName == sourceName }

        if (gardenerMap == null) {
            println("Cannot find map from $sourceName")
            break
        }

        sourceName = gardenerMap.destName
        source = gardenerMap.getDestination(source)
    }

    return source
}

private data class GardnerMap(val sourceName: String, val destName: String, val mappingRanges: List<MappingRange>) {
    fun getDestination(source: Long): Long {
        val range = mappingRanges.firstOrNull { it.covers(source) }

        return range?.getDestination(source) ?: source
    }
}

private data class MappingRange(val sourceStart: Long, val destStart: Long, val length: Long) {
    fun covers(source: Long) = source >= sourceStart && source - sourceStart < length

    /** Only call when function [covers] returns true */
    fun getDestination(source: Long): Long {
        return source - sourceStart + destStart
    }
}
