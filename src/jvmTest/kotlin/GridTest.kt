import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GridTest {

    @Test
    fun basicAccess(){
        val puzzle = Puzzle()
        puzzle[0, 0] = 5
        assertEquals(5, puzzle[0, 0])
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

        val grid = puzzle.grid(1,0)!!
        assertEquals(1, grid[0,0])
        assertEquals(3, grid[2,0])
        assertEquals(9, grid[2,2])
    }
}