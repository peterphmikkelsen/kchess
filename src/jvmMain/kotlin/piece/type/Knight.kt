package piece.type

import piece.Piece
import piece.PieceColor
import utils.Position

class Knight(
    override val color: PieceColor,
    override var position: Position,
) : Piece {
    override val name: String
        get() = "knight"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "Kn"
}