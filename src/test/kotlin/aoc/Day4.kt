package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.pow
import kotlin.test.Test
import kotlin.test.assertEquals

class Day4 {

    @Nested
    internal inner class Day4Test {

        private val day4: Day4 = Day4()

        @Test
        fun testPart1() {
            val expected = 13
            assertEquals(
                expected, day4.solveP1(
                    "/home/lea/yseop/AoC/src/test/resources/inputExample4"
                )
            )
        }
        @Test
        fun testPart2() {
            val expected = 30
            assertEquals(
                expected, day4.solveP2(
                    "/home/lea/yseop/AoC/src/test/resources/inputExample4"
                )
            )
        }

    }

    private fun solveP1(s: String): Int {
        val lines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        return lines.sumOf { line -> 2.0.pow(winningNumbers(line) - 1).toInt() }
    }

    private fun solveP2(s: String): Int {
        val lines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        val numberOfCards: List<MutableList<Int>> =List(lines.size) { mutableListOf(1) }
        lines.forEachIndexed { i, line ->
           numberOfCards.forEachIndexed { index, ints -> if(index in  i + 1..i+winningNumbers(line)) ints.add(numberOfCards[i].sum())}
        }
        return numberOfCards.sumOf { it.sum() }
    }

    private fun winningNumbers(line: String): Int {
        val winning: List<Int> = line.split(":")[1].split("|")[0].split(" ").filter { it != "" }.map { it.toInt() }
        val real: List<Int> = line.split(":")[1].split("|")[1].split(" ").filter { it != "" }.map { it.toInt() }
        return winning.intersect(real.toSet()).count()
    }

}