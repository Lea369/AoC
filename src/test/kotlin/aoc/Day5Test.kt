package aoc

import Day5
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    private val day5 = Day5()

    @Test
    fun testPart1() {
        val expected = 107430936L
        assertEquals(
            expected, day5.solveP1(
                "./src/test/resources/input5"
            )
        )
    }

    @Test
    fun testPart2() {
        val expected = 46L
        assertEquals(
            expected, day5.solveP2(
                "./src/test/resources/inputExample5"
            )
        )
    }
}