package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

public interface MoveCalculator {

    //general move piece func to call subclass
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        return switch (board.getPiece(myPosition).getPieceType()) {
            case ChessPiece.PieceType.BISHOP -> BishopMove.pieceMoves(board, myPosition);
            case ChessPiece.PieceType.ROOK -> null;
            case ChessPiece.PieceType.KNIGHT -> KnightMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.KING -> null;
            case ChessPiece.PieceType.QUEEN -> null;
            case ChessPiece.PieceType.PAWN -> null;
        };
    }

    //subclasses for each piece
    public class BishopMove {

        //public static

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
//            //return List.of(new ChessMove(new ChessPosition(5, 4), new ChessPosition(1, 8), null));
//
//            // make list to hold possible positions to move to
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            // go through chess board and add all possible ChessMoves
//            int curr_row = myPosition.getRow();
//            int curr_col = myPosition.getColumn();
//
//            // iterate right
//            for (int row_val=0; row_val < 8; row_val++) {
//                //iterate left
//                for (int col_val=0; col_val < 8; col_val++) {
//                    // check if in path, has ally,
//                }
//            }
//            // or recurse?
//            // recurse has problem of list order? and need helper functions for each direction.
//            // two for loops has problem of ally or enemy blocking further position options
            return null;
        }
    }

    public class RookMove {

    }

    public class KnightMove {


        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            // check all options
            int curr_row = myPosition.getRow();
            int curr_col = myPosition.getColumn();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add knight move options to list
            List<ChessPosition> options = Arrays.asList(
                new ChessPosition(curr_row+2, curr_col-1),
                new ChessPosition(curr_row+2, curr_col+1),
                new ChessPosition(curr_row-2, curr_col-1),
                new ChessPosition(curr_row-2, curr_col+1),
                new ChessPosition(curr_row+1, curr_col-2),
                new ChessPosition(curr_row-1, curr_col-2),
                new ChessPosition(curr_row+1, curr_col+2),
                new ChessPosition(curr_row-1, curr_col+2)
            );

            // check if ally or end of board, cant add. otherwise add
            for (ChessPosition pos : options) {
                if (pos.isValid()) {
                    if (board.getPiece(pos) == null) {
                        possibleMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    else if (board.getPiece(pos).getTeamColor() != myColor) {
                        possibleMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }
            }
            return possibleMoves;
        }
    }

    public class KingMove {

    }

    public class QueenMove {

    }

    public class PawnMove {

    }
}
