package aoc

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals

class Day2 {

    data class Round(val id: Int, val draws: List<Draw>)
    data class Draw(val blue: Int, val red: Int, val green: Int)


    @Nested
    internal inner class Day2Test {

        private val day2: Day2 = Day2()

        @Test
        fun testPart1() {
            val expected = 8
            assertEquals(
                expected, day2.solveP1(
                    "./src/test/resources/inputExample2"
                )
            )
        }

        @Test
        fun testPart2() {
            val expected = 2286
            assertEquals(
                expected, day2.solveP2(
                    "./src/test/resources/inputExample2"
                )
            )
        }

        @Test
        fun test_file() {
            val expected = 66909
            assertEquals(
                expected, day2.solveP2(
                    "./src/test/resources/input2"
                )
            )
        }

        @Test
        fun testParsing() {
            val expected = Round(1, listOf(Draw(3, 4, 0), Draw(6, 1, 2), Draw(0, 0, 2)))
            assertEquals(
                expected, toRound(
                    "Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green"
                )
            )
        }

    }

    private fun solveP2(s: String): Int {
        val lines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        return lines.map { toRound(it) }.sumOf { round -> power(round) }
    }

    private fun power(round: Round): Int {
        return round.draws.maxOf { it.red } * round.draws
            .maxOf { it.blue } * round.draws.maxOf { it.green }
    }

    private fun solveP1(s: String): Int {
        val lines: List<String> = Files.lines(Paths.get(s)).collect(Collectors.toList())
        return lines.map { toRound(it) }.filter { round -> isValid(round) }.sumOf { it.id }
    }

    private fun isValid(round: Round): Boolean {
        val blueOk: Boolean = round.draws.all { it.blue <= 14 }
        val greenOk: Boolean = round.draws.all { it.green <= 13 }
        val redOk: Boolean = round.draws.all { it.red <= 12 }

        return blueOk && greenOk && redOk
    }

    private fun toRound(line: String): Round {
        val id = line.split(": ")[0].split(" ")[1].toInt()
        val draws: List<Draw> = line.split(": ")[1]
            .split("; ")
            .map { rawDraw ->
                rawDraw.split(", ")
                    .map { it.split(" ")[1] to it.split(" ")[0].toInt() }
            }
            .map { numberColor ->
                Draw(
                    numberColor.getOrElse(numberColor.indexOfFirst { pair -> pair.first == "blue" }) { "" to 0 }.second,
                    numberColor.getOrElse(numberColor.indexOfFirst { pair -> pair.first == "red" }) { "" to 0 }.second,
                    numberColor.getOrElse(numberColor.indexOfFirst { pair -> pair.first == "green" }) { "" to 0 }.second
                )
            }.toList()

        return Round(id, draws)
    }
}