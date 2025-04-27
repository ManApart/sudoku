import kotlin.test.Test
import kotlin.test.assertEquals

class GridTest {

    @Test
    fun basicAccess(){
        val puzzle = Puzzle()
        puzzle.set(0,0, 5)
        assertEquals(5, puzzle.get(0,0))
    }
}