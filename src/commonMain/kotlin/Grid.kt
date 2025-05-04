data class Grid(val sourceX: Int, val sourceY: Int, private val grid: Map<Int, Array<Cell>>) {
    operator fun get(x: Int, y: Int) = grid[y]?.let { it[x].value }

    fun cells() = grid.values.flatMap { it.toList() }

    fun has(value: Int) = grid.any { row -> row.value.any { it.value == value } }

    fun needs() = puzzleNumbers - grid.values.flatMap { row -> row.mapNotNull { it.value } }.toSet()

    fun updatePossible(puzzle: Puzzle) {
        val needs = needs()
        val gridCells = cells()
        gridWidth.forEach { row ->
            needs.forEach { need ->
                if (mustHaveInRow(row, need)) {
                    //rest of row should not have this need as possible
                    puzzle.row(sourceY + row).filter { !gridCells.contains(it) }.forEach { it.mustNotBe(need) }
                }
            }
        }

        gridWidth.forEach { col ->
            needs.forEach { need ->
                if (mustHaveInCol(col, need)) {
                    //rest of col should not have this need as possible
                    puzzle.col(sourceX + col).filter { !gridCells.contains(it) }.forEach { it.mustNotBe(need) }
                }
            }
        }
    }

    fun mustHaveInRow(gridRow: Int, value: Int): Boolean {
        return !has(value) && grid[gridRow]!!.any { it.isPossible(value) }
                && grid.entries.filter { (row, _) -> row != gridRow }.none { (_, cells) -> cells.any { it.isPossible(value) } }
    }

    fun mustHaveInCol(gridCol: Int, value: Int): Boolean {
        return !has(value) && grid.values.any { row -> row[gridCol].isPossible(value) }
                && grid.values.none { cells -> cells.filter { it.x != gridCol }.any { it.isPossible(value) } }
    }
}