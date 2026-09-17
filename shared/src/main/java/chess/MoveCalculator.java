package chess;

import java.util.List;
import java.util.Collection;
import java.util.Objects;

public interface MoveCalculator {

    //general move piece func to call subclass?
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        return switch (board.getPiece(myPosition).getPieceType()) {
            case ChessPiece.PieceType.BISHOP -> BishopMove.pieceMoves(board, myPosition);
            case ChessPiece.PieceType.ROOK -> null;
            case ChessPiece.PieceType.KNIGHT -> null;
            case ChessPiece.PieceType.KING -> null;
            case ChessPiece.PieceType.QUEEN -> null;
            case ChessPiece.PieceType.PAWN -> null;
        };
    }

    //subclasses for each piece
    public class BishopMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
            return List.of(new ChessMove(new ChessPosition(5, 4), new ChessPosition(1, 8), null));
            //return List.of(MoveCalculatorAbs.movePiece(piece.getPieceType()));
        }
    }

    public class RookMove {

    }

    public class KnightMove {

    }

    public class KingMove {

    }

    public class QueenMove {

    }

    public class PawnMove {

    }
}
