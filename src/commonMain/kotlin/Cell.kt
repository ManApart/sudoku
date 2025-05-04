data class Cell(val x: Int, val y: Int, var value: Int? = null) {
    private val possibleValues = puzzleNumbers.toMutableSet()

    fun isPossible(possible: Int) = value == possible || (value == null && possibleValues.contains(possible))

    fun updatePossible(puzzle: Puzzle) {
        if (value == null && possibleValues.size > 1) {
            possibleValues.toList().forEach { possible ->
                if (puzzle.rowHas(y, possible) || puzzle.colHas(x, possible) || puzzle.containingGrid(x, y).has(possible)) possibleValues.remove(possible)
            }
        }
    }

    fun mustNotBe(value: Int) {
        possibleValues.remove(value)
    }

    fun mustBe(value: Int) {
        possibleValues.clear()
        possibleValues.add(value)
    }

    fun hasOnlyOneOption() = possibleValues.size == 1
    fun applyUpdate() {
        if (possibleValues.size == 1) {
            value = possibleValues.first()
            println("Setting $x,$y to $value")
        }
    }

    fun resetPossible() {
        if (value == null) {
            possibleValues.clear()
            possibleValues.addAll(puzzleNumbers)
        }
    }
}
