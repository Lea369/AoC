package aoc

import org.junit.jupiter.api.Nested
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class Day1 {

    private val numbers: Map<String, Int> = mapOf(
        "one" to 1,
        "1" to 1,
        "two" to 2,
        "2" to 2,
        "three" to 3,
        "3" to 3,
        "four" to 4,
        "4" to 4,
        "five" to 5,
        "5" to 5,
        "six" to 6,
        "6" to 6,
        "seven" to 7,
        "7" to 7,
        "eight" to 8,
        "8" to 8,
        "nine" to 9,
        "9" to 9
    )

    @Nested
    internal inner class Day1Test {

        private val day1: Day1 = Day1()

        @Test
        fun testCalibration_file() {
            val expected = 54087
            assertEquals(
                expected, day1.calibrate(
                    "/home/lea/yseop/AoC/src/test/resources/input1"
                )
            )
        }

        @Test
        fun testSingleLineCalibrationP1() {
            val expected = 13
            assertEquals(
                expected, day1.calibrateOneLinePart1("1two383")
            )
        }

        @Test
        fun testSingleLineCalibration() {
            val expected = 13
            assertEquals(
                expected, day1.calibrateOneLine("t1wo383")
            )
        }
    }

    private fun calibrate(filename: String): Int {
        var result = 0
        File(filename).forEachLine { line -> result += calibrateOneLine(line) }
        return result;
    }

    private fun calibrateOneLinePart1(line: String): Int {

        val digits = line.toList().filter { it.isDigit() }
        val firstDigit = digits.first()
        val lastDigit = digits.last()

        return firstDigit.digitToInt() * 10 + lastDigit.digitToInt()
    }

    private fun calibrateOneLine(line: String): Int {

        val firstDigit = numbers.filterKeys { line.contains(it) }
            .map { line.indexOf(it.key) to it.value }.minByOrNull { it.first }!!.second
        val lastDigit = numbers.filterKeys { line.contains(it) }
            .map { line.lastIndexOf(it.key) to it.value }.maxByOrNull { it.first }!!.second

        return firstDigit * 10 + lastDigit
    }
}