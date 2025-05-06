import kotlin.random.Random

fun generatePuzzle(rand: Random): Puzzle {
    val puzzle = importPuzzle(COMPLETE_PUZZLE_TEXT)
    with(puzzle) {
        shuffleNumbers(rand)
        shuffleRows(rand)
        if (!isComplete()) println("Failed to generate puzzle!")

        val isValid = puzzle.cells().none { it.value != null && !puzzle.isValid(it.x, it.y, it.value!!) }
        if (!isValid) println("Puzzle is not valid!")
    }

    return puzzle
}

private fun Puzzle.shuffleNumbers(rand: Random) {
    puzzleWidth.forEach { swapNumbers(it + 1, rand.nextInt(8) + 1) }
}

private fun Puzzle.shuffleRows(rand: Random) {
    puzzleWidth.forEach {
        val randomNum = rand.nextInt(3)
        val blockNumber = it / 3
        swapRows(it, blockNumber * 3 + randomNum)
    }
}

private fun Puzzle.swapNumbers(n1: Int, n2: Int) {
    puzzleWidth.forEach { y ->
        puzzleWidth.forEach { x ->
            val cell = this[x, y]
            if (cell.value == n1) {
                cell.value = n2
            } else if (cell.value == n2) {
                cell.value = n1
            }
        }
    }
}

private fun Puzzle.swapRows(r1: Int, r2: Int) {
    val row1 = row(r1)
    val row2 = row(r2)
    val r1Values = row1.map { it.value }
    row2.forEachIndexed { i, cell -> row1[i].value = cell.value }
    r1Values.forEachIndexed { i, v -> row2[i].value = v }
}
