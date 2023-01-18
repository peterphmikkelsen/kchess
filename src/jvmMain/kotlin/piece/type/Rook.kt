package piece.type

import piece.Piece
import piece.PieceColor
import piece.PieceState
import utils.Position

class Rook(
    override val color: PieceColor,
    override var position: Position,
    override var state: PieceState = PieceState()
): Piece {
    override val name: String
        get() = "rook"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "R"
}