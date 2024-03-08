package aoc

import Day5
import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test
import kotlin.test.assertEquals

class Day5Test {
    private val day5 = Day5()

    @Test
    fun should_convertRawLinesToAssociationTableChain() {
        val rawLinesForSeedToSoil: List<String> = listOf(" ", "seed-to-soil map:", "50 98 3", "52 50 48")

        val actualSeedToSoilChain = day5.extractAssociationTables(rawLinesForSeedToSoil);

        val expectedSeedToSoilLink =
            Day5.AssociationTable(mutableListOf(
                Day5.Association(compatibilityRange = 50L..97L, offset = 2),
                Day5.Association(compatibilityRange = 98L..100L, offset = -48),
            ))

        assertThat(actualSeedToSoilChain).containsExactly(expectedSeedToSoilLink);
    }

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