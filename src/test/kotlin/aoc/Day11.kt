package aoc_2023

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day11 {

    @Nested
    internal inner class Day11Test {

        private val day11: Day11 = Day11()

        @Test
        fun testPart1() {
            assertEquals(374L, day11.solveP1("./src/test/resources/2023/input11"))
        }

    }

    private fun distance(galaxy1: Galaxy, galaxy2: Galaxy, galaxies: List<Galaxy>): Long {
        return abs(galaxy1.col - galaxy2.col) +
                abs(galaxy1.line - galaxy2.line) +
                numberInflatedColumns(galaxy1.col, galaxy2.col, galaxies)*999999L +
                numberInflatedLines(galaxy1.line, galaxy2.line, galaxies)*999999L
    }

    private fun numberInflatedLines(line1: Int, line2: Int, galaxies: List<Galaxy>): Int {
        val start = line1.coerceAtMost(line2)
        val end = line1.coerceAtLeast(line2)

        return (start  .. end).filter { int -> !galaxies.map { it.line }.contains(int) }.size
    }

    private fun numberInflatedColumns(col1: Int,col2: Int, galaxies: List<Galaxy>): Int {
        val start = col1.coerceAtMost(col2)
        val end = col1.coerceAtLeast(col2)

        return (start  .. end).filter { int -> !galaxies.map { it.col }.contains(int) }.size
    }

    data class Galaxy(val line: Int, val col: Int)

    private fun solveP1(s: String): Long {
        val galaxies: List<Galaxy> = Files.lines(Paths.get(s)).collect(Collectors.toList())
            .flatMapIndexed { line: Int, rawLine: String ->
                rawLine.toList().mapIndexed { col, c -> if (c == '#') Galaxy(line, col) else null }
            }.filterNotNull()

       return galaxies
           .flatMap { galaxy ->
               galaxies.filter { it != galaxy }
                   .map { if (galaxies.indexOf(galaxy) < galaxies.indexOf(it)) galaxy to it else it to galaxy }
           }
           .toSet().sumOf { pair -> distance(pair.first, pair.second, galaxies) }
    }

}