package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day12024 {

    @Nested
    internal inner class Day1Test {

        private val day1: Day12024 = Day12024()

        @Test
        fun test_should_read_input_file() {
            val expected: Pair<List<Long>, List<Long>> = Pair(listOf(3L, 4L), listOf(3L, 4L))
            assertEquals(
                expected, day1.readInputFile(
                    "./src/test/resources/2024/input1"
                )
            )
        }

        @Test
        fun test_should_computeTotalDistance() {
            assertEquals(
                3L, day1.computeTotalDistance(Pair(listOf(3L, 4L), listOf(5L, 5L)))
            )
        }

        @Test
        fun test_should_computeSimilarity() {
            assertEquals(
                6L, day1.computeSimilarity(Pair(listOf(3L, 4L), listOf(3L, 3L)))
            )
        }

        @Test
        fun test_should_SolveP1() {
            assertEquals(
                2057374L, day1.solveP1()
            )
        }

        @Test
        fun test_should_SolveP2() {
            assertEquals(
                2057374L, day1.solveP2()
            )
        }
    }

    private fun solveP1(): Long {
        return computeTotalDistance(readInputFile("./src/test/resources/2024/input1_total"))
    }

    private fun solveP2(): Long {
        return computeSimilarity(readInputFile("./src/test/resources/2024/input1_total"))
    }

    private fun computeTotalDistance(input: Pair<List<Long>, List<Long>>): Long {
        return input.first.mapIndexed { index, line -> abs(input.second[index] - line) }.sum()
    }

    private fun computeSimilarity(input: Pair<List<Long>, List<Long>>): Long {
        return input.first.map { line -> line* input.second.filter { it == line }.count() }.sum()
    }

    private fun readInputFile(filename: String): Pair<List<Long>, List<Long>> {
        val firstColumn: List<Long> = Files.lines(Paths.get(filename)).collect(Collectors.toList())
            .map { line -> line.split("   ")[0].toLong() }
            .sorted()
        val secondColumn: List<Long> = Files.lines(Paths.get(filename)).collect(Collectors.toList())
            .map { line -> line.split("   ")[1].toLong() }
            .sorted()
        return Pair(firstColumn, secondColumn)
    }

}