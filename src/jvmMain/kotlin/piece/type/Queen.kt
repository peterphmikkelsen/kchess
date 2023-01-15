package piece.type

import Position
import piece.Piece
import piece.PieceColor
import piece.PieceState

class Queen(
    override val color: PieceColor,
    override var position: Position,
    override var state: PieceState = PieceState()
): Piece {
    override val name: String
        get() = "queen"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "Q"
}