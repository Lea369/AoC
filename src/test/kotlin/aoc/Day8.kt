package aoc_2023

import org.junit.jupiter.api.Nested
import java.math.BigInteger
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.test.Test
import kotlin.test.assertEquals

class Day8 {

    data class Noeud(val origin: String, val destination: Pair<String, String>)
    data class NextExit(val index: BigInteger, val originExit: String)
    data class Periodicity(val start: Long, val period: Long)

    @Nested
    internal inner class Day8Test {

        private val day8: Day8 = Day8()

        @Test
        fun testPart1() {
            assertEquals(6, day8.solveP1("./src/test/resources/2023/inputExample8"))
        }

        @Test
        fun testPart2() {
            assertEquals(BigInteger.valueOf(6L), day8.solveP2("./src/test/resources/2023/input8"))
        }

    }

    private fun solveP1(s: String): Int {
        val instructions: String = Files.lines(Paths.get(s)).collect(Collectors.toList())[0]
        val nodes: List<Noeud> =
            Files.lines(Paths.get(s)).collect(Collectors.toList()).filterIndexed { index, _ -> index >= 2 }
                .map {
                    Noeud(
                        it.split(" = ")[0],
                        it.split(" = (")[1].split(", ")[0] to it.split(" = (")[1].split(", ")[1].replace(")", "")
                    )
                }

        var result = 0
        var origin = "AAA"

        while (origin.last() != 'Z') {
            val instruction = instructions[result % instructions.length]
            origin = if (instruction == 'L') nodes.first { it.origin == origin }.destination.first
            else nodes.first { it.origin == origin }.destination.second
            result++

        }
        return result
    }

    private fun solveP2(s: String): BigInteger {
        val instructions: String = Files.lines(Paths.get(s)).collect(Collectors.toList())[0]
        val nodes: List<Noeud> =
            Files.lines(Paths.get(s)).collect(Collectors.toList()).filterIndexed { index, _ -> index >= 2 }
                .map {
                    Noeud(
                        it.split(" = ")[0],
                        it.split(" = (")[1].split(", ")[0] to it.split(" = (")[1].split(", ")[1].replace(")", "")
                    )
                }
        //return BigInteger.valueOf(22411)* BigInteger.valueOf(18727)* BigInteger.valueOf(24253)* BigInteger.valueOf(14429)* BigInteger.valueOf(16271)* BigInteger.valueOf(20569)

        return findLCMOfListOfNumbers(nodes.filter { it.origin.last() == 'A' }.map {findNextExit(instructions, nodes, NextExit(BigInteger.ZERO, it.origin)).index })
    }

    private fun findNextExit(instructions: String, nodes: List<Noeud>, originExit: NextExit): NextExit {
        var result = originExit.index
        var origin = originExit.originExit

        while (origin.last() != 'Z' || result == originExit.index) {
            val instruction = instructions[result.toInt() % instructions.length]
            origin = if (instruction == 'L') nodes.first { it.origin == origin }.destination.first
            else nodes.first { it.origin == origin }.destination.second
            result++
        }
        return NextExit(result, origin)
    }

    fun findLCM(a: BigInteger, b: BigInteger): BigInteger {
        val larger = if (a > b) a else b
        val maxLcm = a * b
        var lcm = larger
        while (lcm <= maxLcm) {
            if (lcm % a == BigInteger.ZERO && lcm % b == BigInteger.ZERO) {
                return lcm
            }
            lcm += larger
        }
        return maxLcm
    }

    fun findLCMOfListOfNumbers(numbers: List<BigInteger>): BigInteger {
        var result = numbers[0]
        for (i in 1 until numbers.size) {
            result = findLCM(result, numbers[i])
        }
        return result
    }
}