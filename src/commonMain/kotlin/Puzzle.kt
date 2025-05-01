val puzzleWidth = (0 until 9)
private val gridWidth = (0 until 3)

//TODO - check based on only place item can go in a given row

class Puzzle {
    private val cells = puzzleWidth.associateWith { y -> puzzleWidth.map { x -> Cell(x, y) }.toTypedArray() }
    private val grids = gridWidth.map { y -> gridWidth.map { x -> buildGrid(x * 3, y * 3, cells) }.toTypedArray() }

    operator fun get(x: Int, y: Int) = cells[y]!!.let { it[x] }
    operator fun set(x: Int, y: Int, value: Int) {
        cells[y]?.let { it[x].value = value }
    }

    private fun cells() = cells.values.flatMap { it.toList() }

    fun rowHas(row: Int, value: Int) = puzzleWidth.any { get(it, row).value == value }
    fun colHas(col: Int, value: Int) = puzzleWidth.any { get(col, it).value == value }

    fun row(y: Int) = cells[y]!!.toList()
    fun col(x: Int) = cells.values.map { r -> r[x] }
    fun grid(gridX: Int, gridY: Int) = grids[gridY].let { it[gridX] }

    fun updatePossible() {
        cells().forEach { it.updatePossible(this) }
    }

    fun takeStep() {
        updatePossible()
        //TODO - apply inference check based on two in a row
        val cell = cells().firstOrNull { it.value == null && it.hasOnlyOneOption() }
        if (cell != null){
            cell.applyUpdate()
        } else {
            println("Could not find step!")
        }
    }
}

private fun buildGrid(startX: Int, startY: Int, puzzleCells: Map<Int, Array<Cell>>): Grid {
    return Grid(gridWidth.associateWith { y ->
        gridWidth.map { x -> puzzleCells[startY + y]!![startX + x] }.toTypedArray()
    })
}

data class Grid(private val grid: Map<Int, Array<Cell>>) {
    operator fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }

    fun cells() = grid.values.flatMap { it.toList() }

    fun has(value: Int) = grid.any { row -> row.value.any { it.value == value } }

    fun mustHaveInRow(gridRow: Int, value: Int): Boolean {
        return !has(value) && grid[gridRow]!!.any { it.isPossible(value) }
                && grid.entries.filter { (row, _) -> row != gridRow }.none { (_, cells) -> cells.any { it.isPossible(value) } }
    }

    fun mustHaveInCol(gridCol: Int, value: Int): Boolean {
        return !has(value) && grid.values.any { row -> row[gridCol].isPossible(value) }
                && grid.values.none { cells -> cells.filter { it.x != gridCol }.any { it.isPossible(value) } }
    }
}

data class Cell(val x: Int, val y: Int, var value: Int? = null) {
    private val possibleValues = puzzleWidth.toMutableSet()

    fun isPossible(possible: Int) = value == possible || (value == null && possibleValues.contains(possible))

    fun updatePossible(puzzle: Puzzle) {
        if (value == null && possibleValues.size > 1) {
            possibleValues.toList().forEach { possible ->
                if (puzzle.rowHas(y, possible) || puzzle.colHas(x, possible)) possibleValues.remove(possible)
            }
        }
    }

    fun hasOnlyOneOption() = possibleValues.size == 1
    fun applyUpdate() {
        if (possibleValues.size == 1) {
            value = possibleValues.first()
            println("Setting $x,$y to $value")
        }
    }
}
