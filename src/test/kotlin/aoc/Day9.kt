package aoc_2023

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals

class Day9 {

    @Nested
    internal inner class Day9Test {

        private val day9: Day9 = Day9()

        @Test
        fun testPart1() {
            assertEquals(1938731307, day9.solveP1("./src/test/resources/input9"))
        }

        @Test
        fun testPart2() {
            assertEquals(948, day9.solveP2("./src/test/resources/input9"))
        }

        @Test
        fun testNextNumber() {
            assertEquals(28, day9.findNext(listOf(1,3 ,6, 10, 15, 21)))
        }

        @Test
        fun testPrevNumber() {

            assertEquals(5, day9.findPrev(listOf(10, 13, 16, 21, 30, 45)))
        }

    }

    private fun findNext(numbers: List<Int>): Int {
        val nextLine = numbers.mapIndexed { i, n -> if(i ==0) 0 else n-numbers[i-1] }
        if(nextLine.size>1 && nextLine.last() == nextLine[nextLine.size-2]){
            return numbers.last() + nextLine.last()
        }
        return numbers.last() + findNext(nextLine)
    }

    private fun findPrev(numbers: List<Int>): Int {
        val nextLine = numbers.mapIndexed { i, n -> if(i ==numbers.size-1) 0 else numbers[i+1] -n }.filterIndexed { index, _ -> index != numbers.size-1 }
        if(nextLine.filter { it == 0 }.size == nextLine.size){
            return numbers.first() - nextLine.first()
        }
        return numbers.first() - findPrev(nextLine)
    }

    private fun solveP1(s: String): Int {
        val rawLines: List<List<Int>> = Files.lines(Paths.get(s)).collect(Collectors.toList())
            .map { line-> line.split(" ").map { number-> number.toInt() }}

        return rawLines.sumOf { findNext(it) }
    }

    private fun solveP2(s: String): Int {
        val rawLines: List<List<Int>> = Files.lines(Paths.get(s)).collect(Collectors.toList())
            .map { line-> line.split(" ").map { number-> number.toInt() }}

        return rawLines.sumOf { findPrev(it) }
    }


}