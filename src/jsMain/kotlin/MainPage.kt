package org.manapart

import Cell
import Puzzle
import STARTER_PUZZLE
import kotlinx.browser.window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.dom.addClass
import kotlinx.dom.removeClass
import kotlinx.html.*
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyUpFunction
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.events.KeyboardEvent
import puzzleWidth

val isMobile = listOf("iPhone", "iPad", "iPod", "Android").any { window.navigator.userAgent.contains(it) }

var puzzle = STARTER_PUZZLE
var activeCell = puzzle[0, 0]

fun TagConsumer<HTMLElement>.mainPage() {
    div {
        id = "wrapper"
        div {
            id = "puzzle-wrapper"
            puzzle(animate = true)
        }
        div { id = "puzzle-messages" }
        div {
            id = "controls"
            controls()
        }
    }
}

fun TagConsumer<HTMLElement>.puzzle(highlightedCell: Cell? = null, animate: Boolean = false) {
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
                            value = if (animate) "" else (cell.value?.toString() ?: "")
                            autoComplete = "off"
                            readonly = isMobile
                            onKeyUpFunction = { e ->
                                arrowNavigation(x, y, (e as KeyboardEvent).key)
                            }
                            onClickFunction = {
                                val inputCell = el<HTMLInputElement>("cell-$x-$y")
                                inputCell.select()
                                el("cell-${activeCell.x}-${activeCell.y}").removeClass("active-cell")
                                activeCell = puzzle[x, y]
                                inputCell.addClass("active-cell")
                            }
                            onChangeFunction = { cellChanged(x, y) }
                        }
                    }
                }
            }
        }
    }
    animate(highlightedCell, animate)
}

private fun animate(highlightedCell: Cell?, animate: Boolean) {
    CoroutineScope(GlobalScope.coroutineContext).launch {
        delay(10)
        highlightedCell?.let { highlightBox("cell-${it.x}-${it.y}") }
        if (animate) {
            puzzle.cells().filter { it.value != null }.forEachIndexed { i, cell ->
                launch {
                    delay(20L * i)
                    highlightBox("cell-${cell.x}-${cell.y}")
                    delay(100L)
                    el<HTMLInputElement>("cell-${cell.x}-${cell.y}").value = "" + cell.value
                }
            }
        }
        markInvalid(puzzle)
    }
}

private fun cellChanged(x: Int, y: Int) {
    val raw = el<HTMLInputElement>("cell-$x-$y").value
    if (raw == "") {
        puzzle.manuallySet(x, y, null)
        el("cell-$x-$y").removeClass("invalid-cell")
        highlightBox("cell-$x-$y")
    } else {
        cellChanged(x, y, raw.toIntOrNull())
    }
}

fun cellChanged(x: Int, y: Int, newValue: Int?) {
    val messageBox = el("puzzle-messages")
    val cellDiv = el<HTMLInputElement>("cell-$x-$y")
    when {
        newValue == null -> {
            messageBox.textContent = "Unable to parse number"
            cellDiv.value = ""
        }

        newValue < 1 || newValue > 9 -> {
            messageBox.textContent = "$newValue out of range"
            cellDiv.value = ""
        }

        else -> {
            cellDiv.value = "" + newValue
            puzzle.manuallySet(x, y, newValue)
            History.add(puzzle[x, y])
            el<HTMLButtonElement>("previous").disabled = false
            if (puzzle.isValid(x, y, newValue)) highlightBox("cell-$x-$y")
            markInvalid(puzzle)
        }
    }
}

private fun highlightBox(element: String) {
    with(el(element)) {
        removeClass("play-highlight")
        offsetWidth
        addClass("play-highlight")
    }
}

fun markInvalid(puzzle: Puzzle) {
    puzzle.cells().forEach {
        val cellDiv = el("cell-${it.x}-${it.y}")
        if (it.value != null && !puzzle.isValid(it.x, it.y, it.value!!)) {
            cellDiv.addClass("invalid-cell")
        } else {
            cellDiv.removeClass("invalid-cell")
        }
    }
}
