package piece.type

import piece.Piece
import piece.PieceColor
import utils.Position

class Queen(
    override val color: PieceColor,
    override var position: Position,
): Piece {
    override val name: String
        get() = "queen"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "Q"
}