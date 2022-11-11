package piece.type

import Position
import piece.Piece
import piece.PieceColor

class Rook(override val color: PieceColor, override var position: Position): Piece {
    override val name: String
        get() = "rook"

    override fun isValidMove(board: Array<Array<Piece?>>, to: Position): Boolean {
        return true
    }

    override fun toString() = "R"
}