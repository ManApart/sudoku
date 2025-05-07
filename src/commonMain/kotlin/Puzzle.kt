val puzzleWidth = (0 until 9)
val puzzleNumbers = puzzleWidth.map { it + 1 }.toSet()
val gridWidth = (0 until 3)

class Puzzle {
    private val cells = puzzleWidth.associateWith { y -> puzzleWidth.map { x -> Cell(x, y) }.toTypedArray() }
    private val grids = gridWidth.map { y -> gridWidth.map { x -> buildGrid(x * 3, y * 3, cells) }.toTypedArray() }

    operator fun get(x: Int, y: Int) = cells[y]!!.let { it[x] }
    operator fun set(x: Int, y: Int, value: Int?) {
        cells[y]?.let { it[x].value = value }
    }

    fun cells() = cells.values.flatMap { it.toList() }
    private fun grids() = grids.flatMap { it.toList() }

    fun rowHas(row: Int, value: Int, ignoring: Cell? = null) = puzzleWidth.any {
        val cell = get(it, row)
        cell != ignoring && cell.value == value
    }

    fun colHas(col: Int, value: Int, ignoring: Cell? = null) = puzzleWidth.any {
        val cell = get(col, it)
        cell != ignoring && cell.value == value
    }

    fun row(y: Int) = cells[y]!!.toList()
    fun col(x: Int) = cells.values.map { r -> r[x] }
    fun grid(gridX: Int, gridY: Int) = grids[gridY].let { it[gridX] }
    fun containingGrid(x: Int, y: Int) = grid(x / 3, y / 3)

    fun manuallySet(x: Int, y: Int, value: Int?) {
        this[x, y].value = value
        clearPossible()
    }

    fun takeStep(apply: Boolean = true): Cell? {
        updatePossible()
        return (singleOption() ?: mustForRow() ?: mustForCol() ?: mustForGrid())
            ?.apply { if (apply) applyUpdate() }
    }

    fun updatePossible() {
        cells().forEach { it.updatePossible(this) }
        grids().forEach { it.updatePossible(this) }
    }

    private fun singleOption() = cells().firstOrNull { it.value == null && it.hasOnlyOneOption() }

    private fun mustForRow() = puzzleWidth.map { row(it) }.mustForCells()
    private fun mustForCol() = puzzleWidth.map { col(it) }.mustForCells()
    private fun mustForGrid() =
        gridWidth.flatMap { y -> gridWidth.map { x -> grid(x, y) } }.map { it.cells() }.mustForCells()

    private fun List<List<Cell>>.mustForCells(): Cell? {
        forEach { group ->
            val emptyCells = group.filter { it.value == null }
            if (emptyCells.isNotEmpty()) {
                val needed = puzzleNumbers - group.mapNotNull { it.value }.toSet()
                needed.forEach { need ->
                    val possibles = emptyCells.filter { it.isPossible(need) }
                    if (possibles.size == 1) {
                        return possibles.first().also { it.mustBe(need) }
                    }
                }
            }
        }
        return null
    }

    fun isValid(x: Int, y: Int, possible: Int): Boolean {
        val ignoring = this[x, y].let { if (it.value == possible) it else null }
        return possible in 1..9 && !rowHas(y, possible, ignoring) && !colHas(x, possible, ignoring) && !containingGrid(x, y).has(possible, ignoring)
    }

    fun clearPossible() {
        cells().forEach { it.resetPossible() }
    }

    fun clear() {
        cells().forEach { it.reset() }
    }

    fun isComplete() = cells().all { it.value != null }

    fun export() = cells.values.map { row -> row.map { it.value } }

    private fun copy() = Puzzle().also { copy ->
        cells().forEach { c -> copy[c.x, c.y] = c.value}
    }

    fun canBeCompleted(): Boolean {
        val puzzle = copy()
        var next = puzzle.takeStep()
        while (next != null) {
            next = puzzle.takeStep()
        }
        return puzzle.isComplete()
    }

}

private fun buildGrid(startX: Int, startY: Int, puzzleCells: Map<Int, Array<Cell>>): Grid {
    return Grid(startX, startY, gridWidth.associateWith { y ->
        gridWidth.map { x -> puzzleCells[startY + y]!![startX + x] }.toTypedArray()
    })
}

fun importPuzzle(raw: String): Puzzle {
    val puzzle = Puzzle()
    raw.split("\n").filter { it.isNotBlank() }.forEachIndexed { y, row ->
        row.trim().split(",").forEachIndexed { x, v ->
            v.toIntOrNull()?.let { puzzle[x, y] = it }
        }
    }
    return puzzle
}
