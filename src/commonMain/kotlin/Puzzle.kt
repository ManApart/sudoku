private val puzzleWidth = (0 until 9)

class Puzzle {
    private val grid = puzzleWidth.associateWith { puzzleWidth.map { Cell() }.toTypedArray() }
    operator fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }
    operator fun set(x: Int, y: Int, value: Int) {
        grid[y]?.let { it[x] = Cell(value) }
    }

    fun rowHas(row: Int, value: Int) = puzzleWidth.any { get(it, row) == value }
    fun colHas(col: Int, value: Int) = puzzleWidth.any { get(col, it) == value }
}

data class Cell(var value: Int? = null)