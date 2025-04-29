private val puzzleWidth = (0 until 9)
private val gridWidth = (0 until 3)

class Puzzle {
    private val cells = puzzleWidth.associateWith { puzzleWidth.map { Cell() }.toTypedArray() }
    private val grids = gridWidth.map { y -> gridWidth.map { x -> buildGrid(x * 3, y * 3, cells) }.toTypedArray() }

    operator fun get(x: Int, y: Int) = cells[y]?.let { it[x].value }
    operator fun set(x: Int, y: Int, value: Int) {
        cells[y]?.let { it[x].value = value }
    }

    fun rowHas(row: Int, value: Int) = puzzleWidth.any { get(it, row) == value }
    fun colHas(col: Int, value: Int) = puzzleWidth.any { get(col, it) == value }

    fun grid(gridX: Int, gridY: Int) = grids[gridY].let { it[gridX] }

}

private fun buildGrid(startX: Int, startY: Int, puzzleCells: Map<Int, Array<Cell>>): Grid {
    return Grid(gridWidth.associateWith { y ->
        gridWidth.map { x -> puzzleCells[startY + y]!![startX + x] }.toTypedArray()
    })
}

data class Grid(private val grid: Map<Int, Array<Cell>>) {
    operator fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }
    fun has(value: Int) = grid.any { row -> row.value.any { it.value == value } }
//    fun mustHaveInRow(gridX: Int, gridY: Int, gridRow: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
//    fun mustHaveInCol(gridX: Int, gridY: Int, gridCol: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
}

data class Cell(var value: Int? = null) {
    private var possibleValues = puzzleWidth.toMutableSet()
}
