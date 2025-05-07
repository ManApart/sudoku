package org.manapart

import Cell
import Puzzle


data class Move(val x: Int, val y: Int, val value: Int?)

object History {
    var historyIndex = -1
    private var history = mutableListOf<Move>()

    fun hasNext() = historyIndex < history.size - 1

    fun add(cell: Cell) {
        if (historyIndex > -1 && history.size > historyIndex) history = history.subList(0, historyIndex + 1)
        historyIndex++
        history.add(Move(cell.x, cell.y, cell.value))
    }

    fun next(puzzle: Puzzle): Cell? {
        historyIndex++
        println(history.getOrNull(historyIndex))
        if (historyIndex >= history.size) historyIndex = history.size
        return history.getOrNull(historyIndex)?.let {
            puzzle.manuallySet(it.x, it.y, it.value)
            puzzle[it.x, it.y]
        }
    }

    fun previous(puzzle: Puzzle): Cell? {
        if (historyIndex < 0) historyIndex = 0
        return history.getOrNull(historyIndex)?.let {
            puzzle.manuallySet(it.x, it.y, null)
            puzzle[it.x, it.y]
        }.also { historyIndex-- }
    }

    fun clear(){
        history.clear()
        historyIndex = -1
    }
}
