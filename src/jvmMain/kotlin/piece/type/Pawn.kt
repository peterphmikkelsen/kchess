package piece.type

import piece.Piece
import piece.PieceColor
import utils.Position
import kotlin.math.abs

class Pawn(
    override val color: PieceColor,
    override var position: Position,
) : Piece {
    override val name: String
        get() = "pawn"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        val piece = board[to.first][to.second]

        if (movedBackward(to)) return false

        val deltaX = abs(to.first - position.first)
        val deltaY = abs(to.second - position.second)

        if (deltaY > 0) {
            return isAttacking(board, to)
        }

        if (jumpedOverOtherPieces(to, board)) return false

        if (piece != null && color != piece.color) return false

        if (hasNotMoved() && deltaX > 2) return false
        if (!hasNotMoved() && deltaX > 1) return false
        return true
    }

    private fun hasNotMoved(): Boolean {
        return (color == PieceColor.WHITE && position.first == 6) || (color == PieceColor.BLACK && position.first == 1)
    }

    private fun isAttacking(board: Array<Array<Piece?>>, to: Position): Boolean {
        val piece = board[to.first][to.second] ?: return false

        val deltaX = abs(to.first - position.first)
        val deltaY = abs(to.second - position.second)
        return piece.color != color && (deltaX == 1 && deltaY == 1)
    }

    private fun jumpedOverOtherPieces(to: Position, board: Array<Array<Piece?>>): Boolean {
        if (abs(to.first - position.first) < 2) return false
        if (board[if (to.first > position.first) to.first-1 else to.first+1][to.second] == null) return false
        return true
    }

    private fun movedBackward(to: Position): Boolean {
        return if (color == PieceColor.BLACK) to.first - position.first < 0 else position.first - to.first < 0
    }

    override fun toString() = "P"
}