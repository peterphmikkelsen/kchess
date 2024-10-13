package piece.type

import piece.Piece
import piece.PieceColor
import utils.Position
import kotlin.math.abs

class King(
    override val color: PieceColor,
    override var position: Position,
    var hasMoved: Boolean = false
): Piece {
    override val name: String
        get() = "king"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    fun canCastle(board: Array<Array<Piece?>>): Boolean {
        return canCastleQueenSide(board) || canCastleKingSide(board)
    }

    fun canCastleQueenSide(board: Array<Array<Piece?>>): Boolean {
        if (hasMoved) return false

        val queenSideRook = if (color == PieceColor.WHITE) board[7][0] else board[0][0]
        if (queenSideRook != null) {
            return clearPathBetween(queenSideRook.position, board)
        }
        return false
    }

    fun canCastleKingSide(board: Array<Array<Piece?>>): Boolean {
        if (hasMoved) return false

        val kingSideRook = if (color == PieceColor.WHITE) board[7][7] else board[0][7]
        if (kingSideRook != null) {
            return clearPathBetween(kingSideRook.position, board)
        }
        return false
    }

    private fun clearPathBetween(to: Position, board: Array<Array<Piece?>>): Boolean {
        if (abs(to.first - position.first) < 2) return false
        if (board[if (to.first > position.first) to.first-1 else to.first+1][to.second] == null) return false
        return true
    }

    override fun toString() = "K"
}