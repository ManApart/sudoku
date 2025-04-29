import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GridTest {

    @Test
    fun basicAccess(){
        val puzzle = Puzzle()
        puzzle[0, 0] = 5
        assertEquals(5, puzzle[0, 0].value)
    }

    @Test
    fun rowHas(){
        val puzzle = Puzzle()
        puzzle[1, 2] = 5
        assertTrue(puzzle.rowHas(2,5))
        assertFalse(puzzle.rowHas(1,5))
    }

    @Test
    fun colHas(){
        val puzzle = Puzzle()
        puzzle[3, 4] = 5
        assertTrue(puzzle.colHas(3,5))
        assertFalse(puzzle.colHas(4,5))
    }

    @Test
    fun getGrid(){
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

        val grid = puzzle.grid(1,0)
        assertEquals(1, grid[0,0])
        assertEquals(3, grid[2,0])
        assertEquals(9, grid[2,2])
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
    fun gridHas(){
        val puzzle = Puzzle()
        puzzle[0, 1] = 5
        val grid = puzzle.grid(0,0)
        assertTrue(grid.has(5))
        assertFalse(grid.has(6))
        assertFalse(puzzle.grid(1,0).has(5))
    }

    @Test
    fun gridMustHaveInRow(){
        val puzzle = Puzzle()
        puzzle[0, 0] = 1
        puzzle[1, 0] = 2
        puzzle[2, 0] = 3

        puzzle[0, 2] = 4
        puzzle[1, 2] = 5
        puzzle[2, 2] = 6

        puzzle[4, 1] = 8
        puzzle.updatePossible()

        val grid = puzzle.grid(0,0)
        assertTrue(grid.mustHaveInRow(1, 7))
        assertFalse(grid.mustHaveInRow(0, 7))
        assertFalse(grid.mustHaveInRow(1, 6))
        assertFalse(grid.mustHaveInRow(1, 8))
    }

    @Test
    fun gridMustHaveInCol(){
        val puzzle = Puzzle()
        puzzle[0, 0] = 1
        puzzle[2, 0] = 3

        puzzle[0, 1] = 4
        puzzle[2, 1] = 6

        puzzle[0, 2] = 7
        puzzle[2, 2] = 9

        puzzle[1, 4] = 2

        puzzle.updatePossible()

        val grid = puzzle.grid(0,0)
        assertTrue(grid.mustHaveInCol(1, 5))
        assertFalse(grid.mustHaveInCol(0, 5))
        assertFalse(grid.mustHaveInCol(1, 3))
        assertFalse(grid.mustHaveInCol(1, 2))
    }
}
