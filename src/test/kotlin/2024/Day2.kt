package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals

class Day22024 {

    @Nested
    internal inner class Day1Test {

        private val day2: Day22024 = Day22024()

        @Test
        fun test_should_read_input_file() {
            val expected: List<List<Long>> = listOf(listOf(7L, 6L, 4L, 2L, 1L), listOf(1L, 2L, 7L, 8L, 9L))
            assertEquals(
                expected, day2.readInputFile(
                    "./src/test/resources/2024/input2"
                )
            )
        }

        @Test
        fun test_should_check_test_safe_ascending() {
            assertEquals(
                true, day2.isTestSafe(
                    listOf(7L, 6L, 4L, 2L, 1L)
                )
            )
        }

        @Test
        fun test_should_check_test_safe_descending() {
            assertEquals(
                true, day2.isTestSafe(
                    listOf(1L, 3L, 4L, 5L, 7L)
                )
            )
        }

        @Test
        fun test_should_check_test_unsafe_when_not_ascending() {
            assertEquals(
                false, day2.isTestSafe(
                    listOf(7L, 6L, 4L, 2L, 11L)
                )
            )
        }

        @Test
        fun test_should_check_test_unsafe_when_ascending_but_large_difference() {
            assertEquals(
                false, day2.isTestSafe(
                    listOf(17L, 6L, 4L, 2L, 1L)
                )
            )
        }

        @Test
        fun test_solveP1() {
            assertEquals(
                490, day2.solveP1()
            )
        }

        @Test
        fun test_should_check_test_safe_when_1_off() {
            assertEquals(
                false, day2.isTestSafeWithOneOffMoreThan4Diff(
                    listOf(1L, 2L, 7L, 8L, 9L)
                )
            )
        }

        @Test
        fun test_should_check_test_safe_when_1_off_desc() {
            assertEquals(
                false, day2.isTestSafeWithOneOffNotDescending(
                    listOf(1L, 2L, 7L, 8L, 9L)
                )
            )
        }

        @Test
        fun test_should_check_test_safe_when_1_off_asc() {
            assertEquals(
                false, day2.isTestSafeWithOneOffNotAscending(
                    listOf(1L, 2L, 7L, 8L, 9L)
                )
            )
        }


        @Test
        fun test_solveP2() {
            assertEquals(
                536, day2.solveP2()
            )
        }

    }

    private fun solveP2(): Int {
        return readInputFile("./src/test/resources/2024/input2_total")
            .count { isTestSafe(it)
                    || isTestSafeWithOneOffMoreThan4Diff(it)
                    || isTestSafeWithOneOffNotAscending(it)
                    || isTestSafeWithOneOffNotDescending(it)}
    }

    private fun solveP1(): Int {
        return readInputFile("./src/test/resources/2024/input2_total").count { isTestSafe(it) }
    }

    private fun isTestSafe(test: List<Long>): Boolean {
        return isNotMoreThanFourAppart(test) && (isAscending(test) || isDescending(test))
    }

    private fun isTestSafeWithOneOffMoreThan4Diff(test: List<Long>): Boolean {
        return indexOfLevelsMoreThanFourAppart(test).map { test.filterIndexed { index, _ -> index != it }.toList() }.any { isTestSafe(it) }
    }

    private fun isTestSafeWithOneOffNotAscending(test: List<Long>): Boolean {
        return indexOfLevelsNotAscending(test).map { test.filterIndexed { index, _ -> index != it }.toList() }.any { isTestSafe(it) }
    }

    private fun isTestSafeWithOneOffNotDescending(test: List<Long>): Boolean {
        return indexOfLevelsNotDescending(test).map { test.filterIndexed { index, _ -> index != it }.toList() }.any { isTestSafe(it) }
    }

    private fun isAscending(test: List<Long>): Boolean {
        return test.asSequence().zipWithNext { a, b -> a < b }.all { it }
    }

    private fun isDescending(test: List<Long>): Boolean {
        return test.asSequence().zipWithNext { a, b -> a > b }.all { it }
    }

    private fun isNotMoreThanFourAppart(test: List<Long>): Boolean {
        return test.asSequence().zipWithNext { a, b -> abs(a - b) < 4 }.all { it }
    }

    private fun indexOfLevelsMoreThanFourAppart(test: List<Long>): List<Int> {
        return test.asSequence().zipWithNext { a, b -> abs(a - b) >= 4 }.mapIndexed { index, b -> if(b) sequenceOf(index, index+1) else null }.filterNotNull().flatten().toList()
    }

    private fun indexOfLevelsNotAscending(test: List<Long>): List<Int> {
        return test.asSequence().zipWithNext { a, b -> a >= b }.mapIndexed { index, b -> if(b) sequenceOf(index, index+1) else null }.filterNotNull().flatten().toList()
    }

    private fun indexOfLevelsNotDescending(test: List<Long>): List<Int> {
        return test.asSequence().zipWithNext { a, b -> a <= b }.mapIndexed { index, b -> if(b) sequenceOf(index, index+1) else null }.filterNotNull().flatten().toList()
    }

    private fun readInputFile(filename: String): List<List<Long>> {
        return Files.lines(Paths.get(filename)).collect(Collectors.toList())
            .map { line -> line.split(" ").map { it.toLong() }.toList() }
    }

}