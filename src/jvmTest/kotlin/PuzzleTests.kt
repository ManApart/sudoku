import kotlin.random.Random
import kotlin.test.*

//Take another step shouldn't work if no valid step
//(If I enter something invalid)
class PuzzleTests {

    @Test
    fun basicAccess() {
        val puzzle = Puzzle()
        puzzle[0, 0] = 5
        assertEquals(5, puzzle[0, 0].value)
    }

    @Test
    fun getRow() {
        val puzzle = Puzzle()
        val rowValues = (1..9).reversed()

        rowValues.forEach { puzzle[9 - it, 0] = it }

        val values = puzzle.row(0).map { it.value }
        assertEquals(rowValues.toList(), values)
    }

    @Test
    fun getCol() {
        val puzzle = Puzzle()
        val rowValues = (1..9).reversed()

        rowValues.forEach { puzzle[1, 9 - it] = it }

        val values = puzzle.col(1).map { it.value }
        assertEquals(rowValues.toList(), values)
    }

    @Test
    fun rowHas() {
        val puzzle = Puzzle()
        puzzle[1, 2] = 5
        assertTrue(puzzle.rowHas(2, 5))
        assertFalse(puzzle.rowHas(1, 5))
    }

    @Test
    fun colHas() {
        val puzzle = Puzzle()
        puzzle[3, 4] = 5
        assertTrue(puzzle.colHas(3, 5))
        assertFalse(puzzle.colHas(4, 5))
    }


    @Test
    fun rowMustHave() {
        val puzzle = Puzzle()
        puzzle[0, 0] = 9
        puzzle[1, 0] = 2

        puzzle[3, 0] = 4
        puzzle[5, 0] = 3
        puzzle[5, 1] = 1

        puzzle[6, 0] = 5
        puzzle[7, 0] = 6

        puzzle[2, 3] = 1

        puzzle.takeStep()
        assertEquals(1, puzzle[8, 0].value)
    }

    @Test
    fun colMustHave() {
        val puzzle = Puzzle()
        puzzle[0, 0] = 1
        puzzle[0, 1] = 2
        puzzle[0, 2] = 3
        puzzle[0, 3] = 4

        puzzle[0, 6] = 7
        puzzle[0, 7] = 8
        puzzle[0, 8] = 9


        puzzle[4, 5] = 5

        puzzle.takeStep()
        assertEquals(6, puzzle[0, 5].value)
    }

    @Test
    fun import() {
        val puzzle = importPuzzle(
            """
1,,,,,,,,4
,,,,,,,,
,,,,,,,,
,,,,2,,,,
,,,,,,,,
,,,,,,,,
,,,,,,,,
,,,,,,,,
,,,,,,,3,
"""
        )
        assertEquals(1, puzzle[0, 0].value)
        assertEquals(2, puzzle[4, 3].value)
        assertEquals(3, puzzle[7, 8].value)
        assertEquals(4, puzzle[8, 0].value)
    }

    @Test
    fun importIgnoresEmptyTrailingLines() {
        val puzzle = importPuzzle(
            """
1,,,,,,,,4
,,,,,,,,
,,,,,,,,
,,,,2,,,,
"""
        )
        assertEquals(1, puzzle[0, 0].value)
        assertEquals(2, puzzle[4, 3].value)
        assertEquals(4, puzzle[8, 0].value)
    }

    @Test
    fun noDuplicateNumbers() {
        val puzzle = importPuzzle(
            """
        1,2,3,,,,7,8,9
        ,,,,,,,3,
        7,8,9,,,,,1,2
        ,,,,,,,,
        ,,,,,,3,,
"""
        )
        assertEquals(3, puzzle.takeStep()?.value)
        assertEquals(4, puzzle.takeStep()?.value)
    }

    @Test
    fun isValid() {
        val puzzle = importPuzzle(
            """
        1,2,3,,,,,,
        4,5,6,,,,,,
        ,,,,,,,,
        ,,,,,,,,
        ,,,,6,,,,
"""
        )
        assertTrue(puzzle.isValid(3, 0, 6))
        assertTrue(puzzle.isValid(0, 0, 1))
        assertFalse(puzzle.isValid(0, 2, 6))
        assertFalse(puzzle.isValid(4, 0, 6))
        assertFalse(puzzle.isValid(3, 1, 6))
        assertFalse(puzzle.isValid(8, 8, 0))
        assertFalse(puzzle.isValid(8, 8, 10))
    }

    @Test
    fun canBeCompleted() {
        val goodPuzzle = STARTER_PUZZLE
        val badPuzzle = importPuzzle(
            """
        1,2,3,,,,,,
        4,5,6,,,,,,
        ,,,,,,,,
        ,,,,,,,,
        ,,,,6,,,,
"""
        )

        assertTrue(goodPuzzle.canBeCompleted())
        assertFalse(badPuzzle.canBeCompleted())
    }


    @Test
    fun generatePuzzle() {
        val puzzle = generatePuzzle(Random(0))
        puzzle.minimalSolvable()
        assertTrue(puzzle.cells().filter { it.value != null }.size < 50)
        assertTrue(puzzle.canBeCompleted())
    }
}
