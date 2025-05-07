package org.manapart

import Cell
import Puzzle
import generatePuzzle
import gridWidth
import importPuzzle
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onClickFunction
import minimalSolvable
import org.manapart.History.historyIndex
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import kotlin.js.Date
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random


fun TagConsumer<HTMLElement>.controls() {
    historyControls()
    numpadControls()
    saveControls()
}

private fun TagConsumer<HTMLElement>.numpadControls() {
    if (isMobile) {
        div {
            id = "numpad"
            table {
                gridWidth.forEach { y ->
                    tr {
                        gridWidth.forEach { x ->
                            td {
                                +"${y * 3 + x + 1}"
                                onClickFunction = {
                                    cellChanged(activeCell.x, activeCell.y, y * 3 + x + 1)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun TagConsumer<HTMLElement>.historyControls() {
    div {
        id = "puzzle-pieces"
        button(classes = "arrow-button") {
            id = "previous"
            +"<"
            disabled = historyIndex < 0
            onClickFunction = {
                el<HTMLButtonElement>("next").disabled = false
                val previous = History.previous(puzzle)
                el<HTMLButtonElement>("previous").disabled = historyIndex < 0
                replaceElement("puzzle-wrapper") { puzzle(previous) }
            }
        }
        button(classes = "arrow-button") {
            id = "next"
            +">"
            disabled = puzzle.isComplete()
            onClickFunction = {
                val isComplete = puzzle.isComplete()
                if (!isComplete) {
                    if (History.hasNext()) {
                        val next = History.next(puzzle)
                        displayNext(puzzle, next)
                    } else {
                        val next = puzzle.takeStep()
                        if (next == null) {
                            el("puzzle-messages").textContent = "Unable to find Next Step"
                        } else {
                            History.add(next)
                            displayNext(puzzle, next)
                        }
                    }
                }
            }
        }
    }
}


private fun TagConsumer<HTMLElement>.saveControls() {
    div {
        id = "import-export"
        textArea(classes = "puzzle-import-export") { id = "puzzle-import" }

        button {
            +"Import"
            onClickFunction = {
                val import = el<HTMLInputElement>("puzzle-import")
                val raw = import.value
                puzzle = importPuzzle(raw)
                replaceElement("puzzle-wrapper") { puzzle(animate = true) }
                import.value = ""
                clearHistory()
            }
        }

        button {
            +"Clear"
            onClickFunction = {
                puzzle.clear()
                replaceElement("puzzle-wrapper") { puzzle() }
                clearHistory()
            }
        }

        button {
            +"Generate"
            onClickFunction = {
                puzzle = generatePuzzle(Random(Date().getMilliseconds())).apply { minimalSolvable() }
                replaceElement("puzzle-wrapper") { puzzle(animate = true) }
                clearHistory()
            }
        }

        button {
            +"Export"
            onClickFunction = {
                puzzle.export().joinToString("\n") { row ->
                    row.joinToString(",") { "" + (it ?: "") }
                }.let {
                    el<HTMLInputElement>("puzzle-import").value = it
                    println(it)
                }
            }
        }
    }
}

private fun displayNext(puzzle: Puzzle, changedCell: Cell?) {
    replaceElement("puzzle-wrapper") { puzzle(changedCell) }
    el<HTMLButtonElement>("next").disabled = puzzle.isComplete()
    el<HTMLButtonElement>("previous").disabled = false
}

fun arrowNavigation(x: Int, y: Int, key: String) {
    when (key) {
        "ArrowUp" -> arrowNavigation(x, y - 1)
        "ArrowDown" -> arrowNavigation(x, y + 1)
        "ArrowLeft" -> arrowNavigation(x - 1, y)
        "ArrowRight" -> arrowNavigation(x + 1, y)
        else -> {}
    }
}

private fun arrowNavigation(x: Int, y: Int) {
    val newX = min(8, max(0, x))
    val newY = min(8, max(0, y))
    val newCell = el<HTMLInputElement>("cell-$newX-$newY")
    newCell.focus()
    newCell.select()
    el("cell-${activeCell.x}-${activeCell.y}").removeClass("active-cell")
    activeCell = puzzle[x, y]
    newCell.addClass("active-cell")
}

fun clearHistory(){
    History.clear()
    el<HTMLButtonElement>("previous").disabled = true
}
