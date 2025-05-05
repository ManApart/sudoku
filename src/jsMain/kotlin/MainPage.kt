package org.manapart

import Puzzle
import STARTER_PUZZLE
import importPuzzle
import kotlinx.html.*
import kotlinx.html.div
import kotlinx.html.js.*
import kotlinx.html.table
import kotlinx.html.textArea
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import puzzleWidth

val puzzle = STARTER_PUZZLE

fun TagConsumer<HTMLElement>.mainPage() {
    div {
        id = "wrapper"
        div {
            id = "puzzle-wrapper"
            puzzle(puzzle)
        }
        div { id = "puzzle-messages" }
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
                    td(borderCol) {
                        input {
                            id = "cell-$x-$y"
                            onChangeFunction = { cellChanged(x, y) }
                            value = (cell.value?.toString() ?: "")
                        }
                    }
                }
            }
        }
    }
}

private fun cellChanged(x: Int, y: Int) {
    val raw = el<HTMLInputElement>("cell-$x-$y").value
    val newValue = raw.toIntOrNull()
    val messageBox = el("puzzle-messages")
    when {
        raw == "" -> {
            puzzle.manuallySet(x, y, null)
        }

        newValue == null -> {
            messageBox.textContent = "Unable to parse number"
        }

        newValue < 1 || newValue > 9 -> {
            messageBox.textContent = "$newValue out of range"
        }

        else -> {
            println("Cell $x,$y changed to $newValue")
            puzzle.manuallySet(x, y, newValue)
            History.add(puzzle[x, y])
        }
    }
}

//TODO - allow arrow keys move between boxes
private fun TagConsumer<HTMLElement>.controls(puzzle: Puzzle) {
    div {
        id = "puzzle-pieces"
        button {
            id = "previous"
            +"<"
            disabled = historyIndex < 0
            onClickFunction = {
                el<HTMLButtonElement>("next").disabled = false
                History.previous(puzzle)
                el<HTMLButtonElement>("previous").disabled = historyIndex < 0
                replaceElement("puzzle-wrapper") { puzzle(puzzle) }
            }
        }
        button {
            id = "next"
            +">"
            disabled = puzzle.isComplete()
            onClickFunction = {
                val isComplete = puzzle.isComplete()
                if (!isComplete) {
                    if (History.hasNext()) {
                        History.next(puzzle)
                        displayNext(puzzle)
                    } else {
                        val next = puzzle.takeStep()
                        if (next == null) {
                            el("puzzle-messages").textContent = "Unable to find Next Step"
                        } else {
                            History.add(next)
                            displayNext(puzzle)
                        }
                    }
                }
            }
        }
    }

    div {
        id = "import-export"
        textArea(classes = "puzzle-import-export") { id = "puzzle-import" }

        button {
            +"Import"
            onClickFunction = {
                val import = el<HTMLInputElement>("puzzle-import")
                val raw = import.value
                importPuzzle(raw, puzzle)
                replaceElement("puzzle-wrapper") { puzzle(puzzle) }
                import.value = ""
            }
        }

        button {
            +"Clear"
            onClickFunction = {
                puzzle.clear()
                replaceElement("puzzle-wrapper") { puzzle(puzzle) }
            }
        }

        button {
            +"Export"
            onClickFunction = {
                puzzle.export().joinToString("\n") { row ->
                    row.joinToString(",") { "" + (it ?: "") }
                }.let {
                    el<HTMLInputElement>("puzzle-export").value = it
                    println(it)
                }
            }
        }
        textArea(classes = "puzzle-import-export") { id = "puzzle-export" }
    }
}

private fun displayNext(puzzle: Puzzle) {
    replaceElement("puzzle-wrapper") { puzzle(puzzle) }
    el<HTMLButtonElement>("next").disabled = puzzle.isComplete()
    el<HTMLButtonElement>("previous").disabled = false
}
