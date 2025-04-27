import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GridTest {

    @Test
    fun basicAccess(){
        val puzzle = Puzzle()
        puzzle.set(0,0, 5)
        assertEquals(5, puzzle.get(0,0))
    }

    @Test
    fun rowHas(){
        val puzzle = Puzzle()
        puzzle.set(0,0, 5)
        assertTrue(puzzle.rowHas(0,5))
        assertFalse(puzzle.rowHas(1,5))
    }
}