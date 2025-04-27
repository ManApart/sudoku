private val puzzleWidth = (0 until 9)

class Puzzle {
    private val cells = puzzleWidth.associateWith { puzzleWidth.map { Cell() }.toTypedArray() }
    //TODO - build grids properly
    private val grids = puzzleWidth.associateWith { puzzleWidth.map { Grid(mapOf()) }.toTypedArray() }

    operator fun get(x: Int, y: Int) = cells[y]?.let { it[x].value }
    operator fun set(x: Int, y: Int, value: Int) {
        cells[y]?.let { it[x] = Cell(value) }
    }

    fun rowHas(row: Int, value: Int) = puzzleWidth.any { get(it, row) == value }
    fun colHas(col: Int, value: Int) = puzzleWidth.any { get(col, it) == value }

    fun grid(gridX: Int, gridY: Int) = grids[gridY]?.let { it[gridX] }
}

data class Grid(private val grid: Map<Int,Array<Cell>>){
    operator fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }
    //    fun has(gridX: Int, gridY: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
//    fun mustHaveInRow(gridX: Int, gridY: Int, gridRow: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
//    fun mustHaveInCol(gridX: Int, gridY: Int, gridCol: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
}

data class Cell(var value: Int? = null) {
    private var possibleValues = puzzleWidth.toMutableSet()
}