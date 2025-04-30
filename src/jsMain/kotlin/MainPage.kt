package org.manapart

import Puzzle
import STARTER_PUZZLE
import kotlinx.html.*
import kotlinx.html.js.button
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement
import puzzleWidth

fun TagConsumer<HTMLElement>.mainPage() {
    val puzzle = STARTER_PUZZLE
    div {
        id = "wrapper"
        div {
            id = "puzzle-wrapper"
            puzzle(puzzle)
        }
        div {
            id = "controls"
            controls(puzzle)
        }
    }
}

private fun TagConsumer<HTMLElement>.puzzle(puzzle: Puzzle) {
    table {
        id = "puzzle"
        puzzleWidth.forEach { y ->
            val borderRow = if (y == 2 || y == 5) "border-row" else ""
            tr(borderRow) {
                puzzleWidth.forEach { x ->
                    val borderCol = if (x == 2 || x == 5) "border-col" else ""
                    val cell = puzzle[x, y]
                    td(borderCol) { +(cell.value?.toString() ?: "") }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.controls(puzzle: Puzzle) {
    button {
        +"Take Step"
        onClickFunction = {
            puzzle.takeStep()
            replaceElement("puzzle-wrapper") { puzzle(puzzle) }
        }
    }
}
