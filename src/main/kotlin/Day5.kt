import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors

class Day5 {
    fun findMinimumLocationIdForSeed(inputFilePath: String): Long {
        val rawLines: List<String> = readCropFile(inputFilePath)
        val seedIds: List<Long> = extractSeedIds(rawLines)

        val associationTableChain: List<AssociationTable> = extractAssociationTables(rawLines)

        return seedIds.map {
            var result: Long = it
            associationTableChain.forEach { result = matchLink(result, it) }
            result
        }.min()
    }

    private fun extractAssociationTables(rawLines: List<String>): List<AssociationTable> {
        val associationTableChain: List<AssociationTable> =
            rawLines.filterIndexed() { i, _ -> i != 0 }.filter { it.isNotEmpty() }
                .fold(mutableListOf()) { tableChain, rawline ->
                    if (tableChain.isEmpty() || !rawline[0].isDigit()) {
                        tableChain.add(AssociationTable(mutableListOf()))
                    } else {
                        val rawlist = rawline.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                        tableChain.last().values.add(
                            Association(
                                rawlist[1]..rawlist[1] + rawlist[2],
                                rawlist[0] - rawlist[1]
                            )
                        )
                        tableChain.last().values.sortBy { it.compatibilityRange.first }
                    }
                    tableChain
                }
        return associationTableChain
    }

    data class Association(val compatibilityRange: LongRange, val offset: Long)

    data class AssociationTable(val values: MutableList<Association>)

    private fun matchLink(result: Long, associations: AssociationTable): Long {
        val conversion = associations.values.filter { result in it.compatibilityRange }
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

        val maps: List<List<Association>> = rawLines.filterIndexed() { i, _ -> i != 0 }.filter { it.isNotEmpty() }
            .fold(mutableListOf<MutableList<Association>>()) { acc, rawline ->
                if (acc.isEmpty() || !rawline[0].isDigit()) {
                    acc.add(mutableListOf())
                } else {
                    val rawlist = rawline.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
                    acc.last().add(Association(rawlist[1]..<rawlist[1] + rawlist[2], rawlist[0] - rawlist[1]))
                }
                acc
            }

        return inputRanges.minOf { range ->
            processRanges(listOf(range), maps)
        }
    }

    private fun extractSeedIds(rawLines: List<String>) =
        rawLines[0].split(":")[1].split(" ").filter { it != "" }.map { it.toLong() }

    private fun readCropFile(inputFilePath: String): List<String> {
        val rawLines: List<String> = Files.lines(Paths.get(inputFilePath)).collect(Collectors.toList())
        return rawLines
    }

    private fun processRanges(inputRanges: List<LongRange>, maps: List<List<Association>>): Long {
        var result: List<LongRange> = inputRanges
        maps.forEach { map -> result = result.flatMap { range -> processOneRangeInOneMap(range, map) } }
        return result.minOf { it.first }
    }

    private fun processOneRangeInOneMap(range: LongRange, associations: List<Association>): List<LongRange> {
        val boundaries =
            (associations.flatMap { listOf(it.compatibilityRange.first, it.compatibilityRange.last + 1) } + listOf(
                range.first,
                range.last + 1
            )).sorted().filter { it >= range.first && it <= range.last + 1 }

        return boundaries.asSequence()
            .mapIndexed { index, number -> if (index < boundaries.size - 1) number..<boundaries[index + 1] else 0L..0L }
            .filter { it != 0L..0L }
            .map { r ->
                val diff =
                    associations.filter { conv -> r.first in conv.compatibilityRange }.getOrElse(0) { Association(0L..0L, 0L) }.offset
                (r.first + diff)..<(r.last + diff)
            }.toList()
    }
}