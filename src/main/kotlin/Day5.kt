import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class Day5 {
    data class Conversion(val range: LongRange, val diff: Long)

    fun solveP1(s: String): Long {
        val rawLines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        val inputNumbers: List<Long> =
            rawLines[0].split(":")[1].split(" ").filter { it != "" }.map { it.toLong() }
        val maps: List<List<Conversion>> = rawLines.filterIndexed() { i, _ -> i != 0 }.filter { it.isNotEmpty() }
            .fold(mutableListOf<MutableList<Conversion>>()) { acc, rawline ->
                if (acc.isEmpty() || !rawline[0].isDigit()) {
                    acc.add(mutableListOf())
                } else {
                    val rawlist = rawline.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                    acc.last().add(Conversion(rawlist[1]..rawlist[1] + rawlist[2], rawlist[0] - rawlist[1]))
                    acc.last().sortBy { it.range.first }
                }
                acc
            }

        return inputNumbers.map {
            var result: Long = it
            maps.forEach { result = processNumber(result, it) }
            result
        }.min()
    }

    private fun processNumber(result: Long, conversions: List<Conversion>): Long {
        val conversion = conversions.filter { result in it.range }
        return if (conversion.size == 1) {
            result + conversion[0].diff
        } else result
    }

    fun solveP2(s: String): Long {
        val rawLines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())

        val inputRanges: List<LongRange> = rawLines[0].split(":")[1].split(" ")
            .filter { it != "" }
            .map { it.toLong() }
            .chunked(2)
            .map { it.first()..it.first() + it.last() }

        val maps: List<List<Conversion>> = rawLines.filterIndexed() { i, _ -> i != 0 }.filter { it.isNotEmpty() }
            .fold(mutableListOf<MutableList<Conversion>>()) { acc, rawline ->
                if (acc.isEmpty() || !rawline[0].isDigit()) {
                    acc.add(mutableListOf())
                } else {
                    val rawlist = rawline.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                    acc.last().add(Conversion(rawlist[1]..<rawlist[1] + rawlist[2], rawlist[0] - rawlist[1]))
                }
                acc
            }

        return inputRanges.minOf { range ->
            processRanges(listOf(range), maps)
        }
    }

    private fun processRanges(inputRanges: List<LongRange>, maps: List<List<Conversion>>): Long {
        var result: List<LongRange> = inputRanges
        maps.forEach { map -> result = result.flatMap { range -> processOneRangeInOneMap(range, map) } }
        return result.minOf { it.first }
    }

    private fun processOneRangeInOneMap(range: LongRange, conversions: List<Conversion>): List<LongRange> {
        val boundaries =
            (conversions.flatMap { listOf(it.range.first, it.range.last + 1) } + listOf(
                range.first,
                range.last + 1
            )).sorted().filter { it >= range.first && it <= range.last + 1 }

        return boundaries.asSequence()
            .mapIndexed { index, number -> if (index < boundaries.size - 1) number..<boundaries[index + 1] else 0L..0L }
            .filter { it != 0L..0L }
            .map { r ->
                val diff =
                    conversions.filter { conv -> r.first in conv.range }.getOrElse(0) { Conversion(0L..0L, 0L) }.diff
                (r.first + diff)..<(r.last + diff)
            }.toList()
    }
}