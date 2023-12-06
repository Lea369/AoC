package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.sqrt
import kotlin.test.Test
import kotlin.test.assertEquals

class Day6 {

    data class Race(val time: Int, val record: Int)

    @Nested
    internal inner class Day6Test {

        private val day6: Day6 = Day6()

        @Test
        fun testPart1() {
            val expected = 288
            assertEquals(
                expected, day6.solveP1(
                    "./src/test/resources/inputExample6"
                )
            )
        }

        @Test
        fun testPart2() {
            val expected = 71503L
            assertEquals(
                expected, day6.solveP2(
                    "./src/test/resources/inputExample6"
                )
            )
        }
    }

    private fun solveP1(s: String): Int {
        val rawLines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        val rawTimes = rawLines[0].split(":")[1].split(" ").filter { it != "" }.map { it.toInt() }
        val rawRecords = rawLines[1].split(":")[1].split(" ").filter { it != "" }.map { it.toInt() }
        val races = rawTimes.mapIndexed { i, time -> Race(time, rawRecords[i]) }

        return races.map { race -> List(race.time + 1) { i -> i * (race.time - i) }.count { time -> time > race.record } }
            .reduce { acc, i -> acc * i }
    }

    private fun solveP2(s: String): Long {
        val rawLines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        val time = rawLines[0].split(":")[1].replace(" ", "").toLong()
        val record = rawLines[1].split(":")[1].replace(" ", "").toLong()

        val solution1 = 0.5 * (time - sqrt((time * time - 4 * record).toDouble()))
        val solution2 = 0.5 * (time + sqrt((time * time - 4 * record).toDouble()))

        return solution2.toLong() - solution1.toLong()
    }
}