import kotlin.random.Random

fun generatePuzzle(rand: Random): Puzzle {
    val puzzle = importPuzzle(COMPLETE_PUZZLE_TEXT)
    with(puzzle) {
        shuffleNumbers(rand)
        shuffleGroup(rand, ::swapRows)
        shuffleGroup(rand, ::swapColumns)
        shuffleGrid(rand, ::swapRows)
        shuffleGrid(rand, ::swapColumns)

        val isValid = puzzle.cells().none { it.value != null && !puzzle.isValid(it.x, it.y, it.value!!) }
        if (!isComplete()) println("Failed to generate puzzle!")
        if (!isValid) println("Puzzle is not valid!")
    }

    return puzzle
}

fun Puzzle.minimalSolvable() {
    val cells = cells().filter { it.value != null }.shuffled().toMutableList()
    while (cells.isNotEmpty()) {
        val cell = cells.removeLast()
        val before = cell.value
        cell.reset()
        clearPossible()
        if (!canBeCompleted()) {
            manuallySet(cell.x,cell.y, before)
            return
        }
    }
}

private fun Puzzle.shuffleNumbers(rand: Random) {
    puzzleWidth.forEach { swapNumbers(it + 1, rand.nextInt(8) + 1) }
}

private fun shuffleGroup(rand: Random, swapFun: (Int, Int) -> Unit) {
    puzzleWidth.forEach {
        val randomNum = rand.nextInt(3)
        val blockNumber = it / 3
        swapFun(it, blockNumber * 3 + randomNum)
    }
}

private fun shuffleGrid(rand: Random, swapFun: (Int, Int) -> Unit) {
    gridWidth.forEach { a ->
        val b = rand.nextInt(3)
        gridWidth.forEach {
            swapFun(a * 3 + it, b * 3 + it)
        }
    }
}

private fun Puzzle.swapNumbers(a: Int, b: Int) {
    puzzleWidth.forEach { y ->
        puzzleWidth.forEach { x ->
            val cell = this[x, y]
            if (cell.value == a) {
                cell.value = b
            } else if (cell.value == b) {
                cell.value = a
            }
        }
    }
}

private fun Puzzle.swapRows(a: Int, b: Int) {
    val row1 = row(a)
    val row2 = row(b)
    val r1Values = row1.map { it.value }
    row2.forEachIndexed { i, cell -> row1[i].value = cell.value }
    r1Values.forEachIndexed { i, v -> row2[i].value = v }
}

private fun Puzzle.swapColumns(a: Int, b: Int) {
    val col1 = col(a)
    val col2 = col(b)
    val c1Values = col1.map { it.value }
    col2.forEachIndexed { i, cell -> col1[i].value = cell.value }
    c1Values.forEachIndexed { i, v -> col2[i].value = v }
}
