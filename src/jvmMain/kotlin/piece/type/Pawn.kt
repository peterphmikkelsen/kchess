package piece.type

import Position
import piece.Piece
import piece.PieceColor
import piece.PieceState

class Pawn(
    override val color: PieceColor,
    override var position: Position,
    override var state: PieceState = PieceState()
) : Piece {
    override val name: String
        get() = "pawn"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "P"
}