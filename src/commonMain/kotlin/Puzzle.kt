class Puzzle {
    private val grid = (0 until 9).associateWith { (0 until 9).map { Cell() }.toTypedArray() }
    fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }
    fun set(x: Int, y: Int, value: Int) {
        grid[y]?.let { it[x] = Cell(value) }
    }
}

data class Cell(var value: Int? = null)