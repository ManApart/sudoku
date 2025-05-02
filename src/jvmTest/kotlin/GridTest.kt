import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/*
TODO
apply inference check based on two in a row
 */


class GridTest {

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
    fun getGrid() {
        val puzzle = Puzzle()
        puzzle[3, 0] = 1
        puzzle[4, 0] = 2
        puzzle[5, 0] = 3
        puzzle[3, 1] = 4
        puzzle[4, 1] = 5
        puzzle[5, 1] = 6
        puzzle[3, 2] = 7
        puzzle[4, 2] = 8
        puzzle[5, 2] = 9

        val grid = puzzle.grid(1, 0)
        assertEquals(1, grid[0, 0])
        assertEquals(3, grid[2, 0])
        assertEquals(9, grid[2, 2])
    }

    @Test
    fun isPossibleRowCol() {
        val puzzle = Puzzle()
        puzzle[1, 1] = 5
        puzzle.updatePossible()

        assertFalse(puzzle[1, 0].isPossible(5))
        assertFalse(puzzle[1, 2].isPossible(5))
        assertFalse(puzzle[0, 1].isPossible(5))
        assertFalse(puzzle[2, 1].isPossible(5))
        assertFalse(puzzle[1, 1].isPossible(6))
        assertTrue(puzzle[1, 0].isPossible(6))
    }

    @Test
    fun getContainingGrid() {
        val puzzle = Puzzle()

        val grid1 = puzzle.containingGrid(0, 0)
        assertTrue(grid1.cells().any { it.x == 0 && it.y == 0 })

        val grid2 = puzzle.containingGrid(4, 1)
        assertTrue(grid2.cells().any { it.x == 3 && it.y == 0 })

        val grid3 = puzzle.containingGrid(8, 8)
        assertTrue(grid3.cells().any { it.x == 8 && it.y == 8 })
    }

    @Test
    fun gridHas() {
        val puzzle = Puzzle()
        puzzle[0, 1] = 5
        val grid = puzzle.grid(0, 0)
        assertTrue(grid.has(5))
        assertFalse(grid.has(6))
        assertFalse(puzzle.grid(1, 0).has(5))
    }

    @Test
    fun isPossibleGrid() {
        val puzzle = Puzzle()
        puzzle[4, 1] = 5
        puzzle.updatePossible()

        assertFalse(puzzle[3, 0].isPossible(5))
        assertTrue(puzzle[4, 0].isPossible(6))
    }

    @Test
    fun gridMustHaveInRow() {
        val puzzle = Puzzle()
        puzzle[0, 0] = 1
        puzzle[1, 0] = 2
        puzzle[2, 0] = 3

        puzzle[0, 2] = 4
        puzzle[1, 2] = 5
        puzzle[2, 2] = 6

        puzzle[4, 1] = 8
        puzzle.updatePossible()

        val grid = puzzle.grid(0, 0)
        assertTrue(grid.mustHaveInRow(1, 7))
        assertFalse(grid.mustHaveInRow(0, 7))
        assertFalse(grid.mustHaveInRow(1, 6))
        assertFalse(grid.mustHaveInRow(1, 8))
    }

    @Test
    fun gridMustHaveInCol() {
        val puzzle = Puzzle()
        puzzle[0, 0] = 1
        puzzle[2, 0] = 3

        puzzle[0, 1] = 4
        puzzle[2, 1] = 6

        puzzle[0, 2] = 7
        puzzle[2, 2] = 9

        puzzle[1, 4] = 2

        puzzle.updatePossible()

        val grid = puzzle.grid(0, 0)
        assertTrue(grid.mustHaveInCol(1, 5))
        assertFalse(grid.mustHaveInCol(0, 5))
        assertFalse(grid.mustHaveInCol(1, 3))
        assertFalse(grid.mustHaveInCol(1, 2))
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
        assertEquals(5, puzzle[0, 4].value)
    }

//    @Test
//    fun gridMustHave() {
//        val puzzle = Puzzle()
//        puzzle[0, 0] = 9
//        puzzle[1, 0] = 2
//
//        puzzle[3, 0] = 4
//        puzzle[5, 0] = 3
//        puzzle[5, 1] = 1
//
//        puzzle[6, 0] = 5
//        puzzle[7, 0] = 6
//
//        puzzle[2, 3] = 1
//
//        puzzle.takeStep()
//
//        assertEquals(1, puzzle[8, 0].value)
//    }

}
