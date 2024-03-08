package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals

class Day3 {

    @Nested
    internal inner class Day3Test {

        private val day3: Day3 = Day3()

        @Test
        fun testPart1() {
            val expected = 467835
            assertEquals(expected, day3.solveP2("./src/test/resources/inputExample3"))
        }

        @Test
        fun testFindNumbers() {
            val expected: List<IntRange> = listOf(0..2, 5..7)
            assertEquals(expected, day3.findNumbers("467..114.."))
        }

        @Test
        fun testFilterNumbers() {
            assertEquals(false, day3.isIsland(0, 0..2, listOf("467..114..", "...*......")))
            assertEquals(true, day3.isIsland(0, 5..7, listOf("467..114..", "...*......")))
        }

        @Test
        fun testGearRatio() {
            assertEquals(16345, day3.gearRatio(1, 3, listOf("467..114..", "...*......", "..35..633.")))
        }


    }

    private fun gearRatio(lineIndex: Int, colIndex: Int, ref: List<String>): Int {
        val numbersPrevLine: List<Int> = findNumbers(ref.getOrElse(lineIndex-1){""})
            .filter { colIndex in it || colIndex+1 in it || colIndex-1 in it }
            .map { ref.getOrElse(lineIndex-1){""}.substring(it).toInt() }
        val numbersNextLine: List<Int> = findNumbers(ref.getOrElse(lineIndex+1){""})
            .filter { colIndex in it || colIndex+1 in it || colIndex-1 in it }
            .map { ref.getOrElse(lineIndex+1){""}.substring(it).toInt() }
        val numbersLine: List<Int> = findNumbers(ref.getOrElse(lineIndex){""})
            .filter { colIndex+1 in it || colIndex-1 in it }
            .map { ref.getOrElse(lineIndex){""}.substring(it).toInt() }
        val allNumbers = numbersLine + numbersPrevLine + numbersNextLine

        if (allNumbers.size == 2) return allNumbers.first() * allNumbers.last()
        return 0
    }

    private fun solveP2(filename: String): Int {
        val lines: List<String> = Files.lines(Paths.get(filename)).collect(Collectors.toList())
        return lines.flatMapIndexed { index, line ->
            line.mapIndexed { i, c -> i to c }.filter { it.second == '*' }.map { index to it.first }
        }.sumOf { gearRatio(it.first, it.second, lines) }
    }

    private fun solveP1(filename: String): Int {
        val lines: List<String> = Files.lines(Paths.get(filename)).collect(Collectors.toList())
        return lines.flatMapIndexed { index, line -> findNumbers(line).map { index to it } }
            .filter { !isIsland(it.first, it.second, lines) }
            .sumOf { lines.get(it.first).substring(it.second).toInt() }
    }

    private fun isIsland(lineIndex: Int, intRange: IntRange, ref: List<String>): Boolean {
        val nextLine = ref.getOrElse(lineIndex + 1) { "" }
            .filterIndexed { i, _ -> i in intRange.first - 1..intRange.last + 1 }
            .all { it.isDigit() || it == '.' }
        val prevLine = ref.getOrElse(lineIndex - 1) { "" }
            .filterIndexed { i, _ -> i in intRange.first - 1..intRange.last + 1 }
            .all { it.isDigit() || it == '.' }
        val thisLine = ref.getOrElse(lineIndex) { "" }
            .filterIndexed { i, _ -> i in intRange.first - 1..intRange.last + 1 }
            .all { it.isDigit() || it == '.' }
        return prevLine && nextLine && thisLine
    }

    private fun findNumbers(line: String): List<IntRange> {
        return line.toList()
            .mapIndexed { index, c -> index to c }
            .filter { it.second.isDigit() }
            .map { it.first }
            .fold(mutableListOf<MutableList<Int>>()) { acc, i ->
                if (acc.isEmpty() || acc.last().last() != i - 1) {
                    acc.add(mutableListOf(i))
                } else acc.last().add(i)
                acc
            }
            .map { listofIndexes -> IntRange(listofIndexes.first(), listofIndexes.last()) }
            .toList()
    }

}