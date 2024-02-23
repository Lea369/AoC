import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class Day5 {
    data class Conversion(val sourceRange: LongRange, val offset: Long) {
        companion object {
            fun fromXToLocationBounds(bounds: XToLocationBounds): Conversion {
                return Conversion(bounds.beginSource..< bounds.beginSource + bounds.rangeLength, bounds.beginDestination - bounds.beginSource)
            }
        }
    }
    data class ChainLink(val link: MutableList<Conversion>)

    data class XToLocationBounds(val beginDestination: Long, val beginSource: Long, val rangeLength: Long) {
        companion object {
            fun fromLine(rawline: String): XToLocationBounds {
                val rawlist: List<Long> = rawline.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                return XToLocationBounds(rawlist[0], rawlist[1], rawlist[2])
            }
        }
    }

    fun solveForSimpleSeedInput(s: String): Long {
        val rawLines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        val inputSeeds: List<Long> =
            rawLines[0].split(":")[1].split(" ").filter { it != "" }.map { it.toLong() }
        val seedToLocationChain: List<ChainLink> = convertRawLinesToSeedLocationChain(rawLines)

        return inputSeeds.map {
            var result: Long = it
            seedToLocationChain.forEach { result = processNumber(result, it) }
            result
        }.min()
    }

    fun convertRawLinesToSeedLocationChain(rawLines: List<String>): List<ChainLink> {
        val seedToLocationChain: List<ChainLink> =
            rawLines.filterIndexed() { i, _ -> i != 0 }.filter { it.isNotEmpty() }
                .fold(mutableListOf()) { acc, rawline ->
                    if (acc.isEmpty() || !rawline[0].isDigit()) {
                        acc.add(ChainLink(link = mutableListOf()))
                    } else {
                        val bounds = XToLocationBounds.fromLine(rawline)
                        acc.last().link.add(Conversion.fromXToLocationBounds(bounds))
                    }
                    acc
                }
        return seedToLocationChain
    }

    private fun processNumber(result: Long, conversions: ChainLink): Long {
        val conversion = conversions.link.filter { result in it.sourceRange }
        return if (conversion.size == 1) {
            result + conversion[0].offset
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
            (conversions.flatMap { listOf(it.sourceRange.first, it.sourceRange.last + 1) } + listOf(
                range.first,
                range.last + 1
            )).sorted().filter { it >= range.first && it <= range.last + 1 }

        return boundaries.asSequence()
            .mapIndexed { index, number -> if (index < boundaries.size - 1) number..<boundaries[index + 1] else 0L..0L }
            .filter { it != 0L..0L }
            .map { r ->
                val diff =
                    conversions.filter { conv -> r.first in conv.sourceRange }.getOrElse(0) { Conversion(0L..0L, 0L) }.offset
                (r.first + diff)..<(r.last + diff)
            }.toList()
    }
}