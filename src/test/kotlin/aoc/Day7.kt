package aoc_2023

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals

class Day7 {

    data class Hand(val cards: String, val bet: Int)

    private val possibleCards: List<Char> = listOf('A', 'K', 'Q', 'T', '9', '8', '7', '6', '5', '4', '3', '2', 'J')

    @Nested
    internal inner class Day7Test {

        private val day7: Day7 = Day7()

        @Test
        fun testPart1() {
            val expected = 251195607
            assertEquals(expected, day7.solveP1("./src/test/resources/input7"))
        }

    }

    private fun solveP1(s: String): Int {
        val hands: List<Hand> =
            Files.lines(Paths.get(s)).map { Hand(it.split(" ")[0].trim(), it.split(" ")[1].trim().toInt()) }
                .collect(Collectors.toList())
        return hands.sortedWith(handComparator).mapIndexed { i, hand -> (i + 1) * hand.bet }.sum()
    }

    val handComparator = Comparator<Hand> { hand1, hand2 ->
        if (getType(hand1) != getType(hand2)) {
            getType(hand2) - getType(hand1)
        } else {
            hand1.cards.toList()
                .mapIndexed { i, char -> possibleCards.indexOf(hand2.cards[i]) - possibleCards.indexOf(char) }
                .firstOrNull { it != 0 } ?: 0
        }
    }

    private fun getType(hand: Hand): Int {
        val jokers = hand.cards.count { it == 'J' }
        val mapCharToOccurence: List<Int> = hand.cards.toSet().filter { it != 'J' }.map { char -> hand.cards.count { it == char } }
        val actualisedOccurences: List<Int> = listOf((mapCharToOccurence.firstOrNull { it == mapCharToOccurence.max() }
            ?: 0) + jokers) + mapCharToOccurence.filterIndexed{ i, _ -> i != mapCharToOccurence.indexOf(mapCharToOccurence.max()) }

        if (actualisedOccurences.max() == 5) {
            return 0
        }
        if (actualisedOccurences.max() == 4) {
            return 1
        }
        if (actualisedOccurences.containsAll(listOf(3, 2))) {
            return 2
        }
        if (actualisedOccurences.max() == 3) {
            return 3
        }
        if (actualisedOccurences.count { it == 2 } == 2) {
            return 4
        }
        if (actualisedOccurences.max() == 2) {
            return 5
        }
        return 6
    }

}