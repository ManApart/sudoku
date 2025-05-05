package org.manapart

import Cell
import Puzzle

var historyIndex = -1
var history = mutableListOf<Move>()

data class Move(val x: Int, val y: Int, val value: Int?)

object History {

    fun hasNext() = historyIndex < history.size - 1

    fun add(cell: Cell) {
        if (historyIndex > -1 && history.size > historyIndex) history = history.subList(0, historyIndex + 1)
        historyIndex++
        history.add(Move(cell.x, cell.y, cell.value))
    }

    fun next(puzzle: Puzzle) {
        historyIndex++
        println(history.getOrNull(historyIndex))
        if (historyIndex >= history.size) historyIndex = history.size
        history.getOrNull(historyIndex)?.let {
            puzzle.manuallySet(it.x, it.y, it.value)
        }
    }

    fun previous(puzzle: Puzzle) {
        if (historyIndex < 0) historyIndex = 0
        history.getOrNull(historyIndex)?.let {
            puzzle.manuallySet(it.x, it.y, null)
        }
        historyIndex--
    }
}
