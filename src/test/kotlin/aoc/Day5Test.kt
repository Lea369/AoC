package aoc

import Day5
import kotlin.test.Test
import kotlin.test.assertEquals
import org.assertj.core.api.Assertions.assertThat

class Day5Test {
    private val day5 = Day5()

    @Test
    fun should_convertRawLinesToSeedLocationChain() {
        val rawLinesForSeedToSoil: List<String> = listOf(" ", "seed-to-soil map:", "50 98 3", "52 50 48")

        val actualSeedToSoilChain = day5.convertRawLinesToSeedLocationChain(rawLinesForSeedToSoil);

        val expectedSeedToSoilChain =  Day5.CropParameterCompatibilityMapping(
            cropParameter = mutableListOf(
                Day5.Conversion(sourceRange = 98L..100L, offset = -48),
                Day5.Conversion(sourceRange = 50L..97L, offset = 2),
            )
        )
        assertThat(actualSeedToSoilChain).containsExactly(expectedSeedToSoilChain);
    }

    @Test
    fun should_findMinimumLocationForSeed_FromSimpleSeed() {
        val expected = 107430936L
        assertEquals(
            expected, day5.solveForSimpleSeedInput(
                "./src/test/resources/input5"
            )
        )
    }

    @Test
    fun should_findMinimumLocationForSeed_FromSeedRange() {
        val expected = 46L
        assertEquals(
            expected, day5.solveP2(
                "./src/test/resources/inputExample5"
            )
        )
    }
}