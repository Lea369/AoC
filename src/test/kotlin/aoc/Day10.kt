package aoc_2023

import org.junit.jupiter.api.Nested
import java.nio.file.Files
import java.nio.file.Paths
import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class Day10 {

    @Nested
    internal inner class Day10Test {

        private val day10: Day10 = Day10()

        @Test
        fun testPart1() {
            assertEquals(6690, day10.solveP1("./src/test/resources/input10"))
        }

        @Test
        @Ignore("Test too long to execute")
        fun testPart2() {
            assertEquals(525, day10.solveP2("./src/test/resources/input10"))
        }
    }

    data class Noeud(val line: Int, val col: Int, val char: Char)

    data class Square(val line: Int, val col: Int, val cardinals: List<Noeud>)

    private fun solveP2(s: String): Int {
        val nodes: List<List<Noeud>> = Files.lines(Paths.get(s)).collect(Collectors.toList())
            .mapIndexed { index: Int, rawLine: String -> rawLine.toList().mapIndexed { i, c -> Noeud(index, i, c) } }

        val nodesOfPipe: List<Noeud> = frontierNodes(nodes.flatten())
        val startNode: Noeud = nodes.flatten().first { it.char == 'S' }
        val squares: List<Square> = nodes.squares()
        
        val neSquare: Square = squares.first { it.line == startNode.line - 1 && it.col == startNode.col }
        val clusterFromNE: Set<Square> = findConnexeSquares(squares, nodesOfPipe, neSquare)


        return clusterFromNE.flatMap { it.cardinals }.toSet().filter { !nodesOfPipe.contains(it) }.size
    }

    private fun findConnexeSquares(squares: List<Square>, nodesOfPipe: List<Noeud>, startSquare: Square): Set<Square> {
        var cluster: MutableList<Square> = mutableListOf()
        var newSquares: Set<Square> = setOf(startSquare)

        while (newSquares.isNotEmpty() && !cluster.containsAll(newSquares)) {
            cluster.addAll(newSquares)
            newSquares = nextSquaresForCluster(newSquares, nodesOfPipe, squares)
        }

        return cluster.toSet()
    }

    private fun nextSquaresForCluster(startSquares: Set<Square>, nodesOfPipe: List<Noeud>, squares: List<Square>): Set<Square> {

        val result = startSquares.map { square -> nextSquares(square, nodesOfPipe, squares) }

        if (result.any { it.isEmpty() }) {
            return emptySet()
        }

        return result.flatten().toSet()
    }

    private fun nextSquares(startSquare: Square, nodesOfPipe: List<Noeud>, squares: List<Square>): List<Square> {

        val result : MutableList<Square> = mutableListOf()

        if (startSquare.northOpen(nodesOfPipe)) {
            val nextNorthSquare = squares.firstOrNull { it.line == startSquare.line - 1 && it.col == startSquare.col }
            if (nextNorthSquare == null) return emptyList()
            else result.add(nextNorthSquare)
        }
        if (startSquare.eastOpen(nodesOfPipe)) {
            val nextEastSquare = squares.firstOrNull { it.line == startSquare.line && it.col == startSquare.col + 1 }
            if (nextEastSquare == null) return emptyList()
            else result.add(nextEastSquare)
        }
        if (startSquare.southOpen(nodesOfPipe)) {
            val nextSouthSquare = squares.firstOrNull { it.line == startSquare.line+1 && it.col == startSquare.col  }
            if (nextSouthSquare == null) return emptyList()
            else result.add(nextSouthSquare)
        }
        if (startSquare.westOpen(nodesOfPipe)) {
            val nextWestSquare = squares.firstOrNull { it.line == startSquare.line && it.col == startSquare.col - 1 }
            if (nextWestSquare == null) return emptyList()
            else result.add(nextWestSquare)
        }
        return result
    }

    private fun List<List<Noeud>>.squares() = sequence {
        for (i in 0..<(size - 1)) {
            for (j in 0..<get(0).size - 1) {
                yield(Square(i, j, listOf<Noeud>(get(i)[j], get(i)[j + 1], get(i + 1)[j + 1], get(i + 1)[j])))
            }
        }
    }.toList()

    private fun Square.northOpen(nodesOfPipe: List<Noeud>): Boolean {
        return !nodesOfPipe.contains(this.cardinals[0]) ||
                !nodesOfPipe.contains(this.cardinals[1]) ||
                (abs(nodesOfPipe.indexOf(this.cardinals[0]) - nodesOfPipe.indexOf(this.cardinals[1])) != 1 &&
                        abs(nodesOfPipe.indexOf(this.cardinals[0]) - nodesOfPipe.indexOf(this.cardinals[1])) != nodesOfPipe.size - 1)
    }

    private fun Square.eastOpen(nodesOfPipe: List<Noeud>): Boolean {
        return !nodesOfPipe.contains(this.cardinals[1]) ||
                !nodesOfPipe.contains(this.cardinals[2]) ||
                (abs(nodesOfPipe.indexOf(this.cardinals[1]) - nodesOfPipe.indexOf(this.cardinals[2])) != 1 &&
                        abs(nodesOfPipe.indexOf(this.cardinals[1]) - nodesOfPipe.indexOf(this.cardinals[2])) != nodesOfPipe.size - 1)
    }

    private fun Square.southOpen(nodesOfPipe: List<Noeud>): Boolean {
        return !nodesOfPipe.contains(this.cardinals[2]) ||
                !nodesOfPipe.contains(this.cardinals[3]) ||
                (abs(nodesOfPipe.indexOf(this.cardinals[2]) - nodesOfPipe.indexOf(this.cardinals[3])) != 1 &&
                        abs(nodesOfPipe.indexOf(this.cardinals[2]) - nodesOfPipe.indexOf(this.cardinals[3])) != nodesOfPipe.size - 1)
    }

    private fun Square.westOpen(nodesOfPipe: List<Noeud>): Boolean {
        return !nodesOfPipe.contains(this.cardinals[0]) ||
                !nodesOfPipe.contains(this.cardinals[3]) ||
                (abs(nodesOfPipe.indexOf(this.cardinals[0]) - nodesOfPipe.indexOf(this.cardinals[3])) != 1 &&
                        abs(nodesOfPipe.indexOf(this.cardinals[0]) - nodesOfPipe.indexOf(this.cardinals[3])) != nodesOfPipe.size - 1)
    }

    private fun frontierNodes(nodes: List<Noeud>): List<Noeud> {
        val nodesOfPipe: MutableList<Noeud> = mutableListOf()
        var currentNode: Noeud = nodes.first { it.char == 'S' }

        while (!(currentNode.char == 'S' && nodesOfPipe.size != 0)) {
            val nextNode = findNextNode(currentNode, nodesOfPipe.lastOrNull(), nodes)!!
            nodesOfPipe.add(currentNode)
            currentNode = nextNode
        }
        return nodesOfPipe
    }

    private fun solveP1(s: String): Int {
        val nodes: List<Noeud> = Files.lines(Paths.get(s)).collect(Collectors.toList())
            .flatMapIndexed { index: Int, rawLine: String ->
                rawLine.toList().mapIndexed { i, c -> Noeud(index, i, c) }
            }

        return frontierNodes(nodes).size / 2
    }

    private fun findNextNode(currentNode: Noeud, lastNode: Noeud?, nodes: List<Noeud>): Noeud? {
        val possibleNodes = findPossibleNodes(nodes, currentNode)

        return possibleNodes.firstOrNull { it != lastNode && findPossibleNodes(nodes, it).contains(currentNode) }
    }

    private fun findPossibleNodes(nodes: List<Noeud>, currentNode: Noeud): List<Noeud> {
        val northNode = nodes.firstOrNull { it.line == currentNode.line - 1 && it.col == currentNode.col }
        val eastNode = nodes.firstOrNull { it.line == currentNode.line && it.col == currentNode.col + 1 }
        val southNode = nodes.firstOrNull { it.line == currentNode.line + 1 && it.col == currentNode.col }
        val westNode = nodes.firstOrNull { it.line == currentNode.line && it.col == currentNode.col - 1 }

        val possibleNodes: List<Noeud?> =
            when (currentNode.char) {
                'S' -> listOf(northNode, eastNode, southNode, westNode)
                'J' -> listOf(westNode, northNode)
                'F' -> listOf(southNode, eastNode)
                '7' -> listOf(southNode, westNode)
                '|' -> listOf(southNode, northNode)
                'L' -> listOf(eastNode, northNode)
                '-' -> listOf(eastNode, westNode)
                else -> listOf()
            }

        return possibleNodes.filterNotNull()
    }

}