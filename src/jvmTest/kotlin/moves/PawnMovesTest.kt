package moves

import game.Game
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import utils.Position
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PawnMovesTest {
    private val game = Game()
    private val board = game.board.value

    @BeforeEach
    fun setUpBoard() {
        board.reset()
        val piece = board[Position(6, 1)]
        board[Position(2, 1)] = piece
        board[Position(6, 1)] = null
        piece?.position = Position(2, 1)
    }

    @Test
    fun testPawnFirstMove() {
        assertTrue(firstMove())
    }

    @Test
    fun testPawnSecondMove() {
        assertTrue(secondMove())
    }

    @Test
    fun testPawnCannotMoveBackward() {
        assertFalse(moveBackWards())
    }

    @Test
    fun testPawnCanAttack() {
        assertTrue(attackMove())
    }

    @Test
    fun testPawnCannotMoveDiagonally() {
        assertFalse(board.move(Position(6, 3), Position(5, 4)))
        assertFalse(board.move(Position(6, 4), Position(5, 3)))
    }

    @Test
    fun testPawnCannotMoveVertically() {
        assertFalse(board.move(Position(6, 3), Position(6, 4)))
        assertFalse(board.move(Position(6, 4), Position(6, 3)))
    }

    @Test
    fun testPawnCannotJumpOverPiece() {
        firstMove()
        assertFalse(board.move(Position(1, 1), Position(3, 1)))
    }

    private fun firstMove(): Boolean {
        val pawnPosition = Position(6, 3)
        return board.move(pawnPosition, Position(4, 3))
    }

    private fun secondMove(): Boolean {
        firstMove()
        val pawnPosition = Position(4, 3)
        return board.move(pawnPosition, Position(3, 3))
    }

    private fun moveBackWards(): Boolean {
        firstMove()
        val pawnPosition = Position(4, 3)
        return board.move(pawnPosition, Position(5, 3))
    }

    private fun attackMove(): Boolean {
        return board.move(Position(2, 1), Position(1, 2))
    }
}