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
}