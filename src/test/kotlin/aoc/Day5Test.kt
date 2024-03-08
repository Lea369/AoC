package aoc

import Day5
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    private val day5 = Day5()

    @Test
    fun should_findMinimumLocationIdForSeed_FromSimpleSeedIds() {
        val expectedLocationId = 107430936L

        assertEquals(
            expectedLocationId, day5.findMinimumLocationIdForSeed(
                "./src/test/resources/input5"
            )
        )
    }

    @Test
    fun should_findMinimumLocationIdForSeed_FromSeedRanges() {
        val expectedLocationId = 46L

        assertEquals(
            expectedLocationId, day5.solveP2(
                "./src/test/resources/inputExample5"
            )
        )
    }
}