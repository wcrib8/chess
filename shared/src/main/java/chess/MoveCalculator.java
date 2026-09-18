package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Collection;

public interface MoveCalculator {

    //general move piece func to call subclass
    public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        return switch (board.getPiece(myPosition).getPieceType()) {
            case ChessPiece.PieceType.BISHOP -> BishopMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.ROOK -> null;
            case ChessPiece.PieceType.KNIGHT -> KnightMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.KING -> KingMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.QUEEN -> null;
            case ChessPiece.PieceType.PAWN -> null;
        };
    }

    // helper function for checking list of move options
    static void addToList(List<ChessPosition> moveOptions, List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        for (ChessPosition pos : moveOptions) {
            if (pos.isValid()) {
                if (board.getPiece(pos) == null) {
                    possibleMoves.add(new ChessMove(myPosition, pos, null));
                }
                else if (board.getPiece(pos).getTeamColor() != myColor) {
                    possibleMoves.add(new ChessMove(myPosition, pos, null));
                }
            }
        }
    }

    // helper function for bishops, rooks, and queens
    static void addInLine(List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, int x, int y) {
        // while is valid and empty, add and continue; if enemy, add to list then stop; if ally, stop
        ChessPosition pos = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
        while (pos.isValid()) {
            if (board.getPiece(pos) == null) {
                possibleMoves.add(new ChessMove(myPosition, pos, null));
            }
            else if (board.getPiece(pos).getTeamColor() != myColor) {
                possibleMoves.add(new ChessMove(myPosition, pos, null));
                break;
            }
            else if (board.getPiece(pos).getTeamColor() == myColor) break;
            pos = new ChessPosition(pos.getRow()+x, pos.getColumn()+y);
        }
    }

    //subclasses for each piece
    public class BishopMove {

        //public static

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            // setup needed vars and list
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            // check all options
            int curr_row = myPosition.getRow();
            int curr_col = myPosition.getColumn();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add to possible moves list with helper func, for each direction
            addInLine(possibleMoves, board, myPosition, myColor, 1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, 1, -1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, -1);

            return possibleMoves;

//              return List.of(new ChessMove(new ChessPosition(5, 4), new ChessPosition(1, 8), null));
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
//
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

            addToList(options, possibleMoves, board, myPosition, myColor);
            return possibleMoves;
        }
    }

    public class KingMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            // check all options
            int curr_row = myPosition.getRow();
            int curr_col = myPosition.getColumn();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add move options
            List<ChessPosition> options = Arrays.asList(
                    new ChessPosition(curr_row+1, curr_col),
                    new ChessPosition(curr_row+1, curr_col+1),
                    new ChessPosition(curr_row, curr_col+1),
                    new ChessPosition(curr_row-1, curr_col+1),
                    new ChessPosition(curr_row-1, curr_col),
                    new ChessPosition(curr_row-1, curr_col-1),
                    new ChessPosition(curr_row, curr_col-1),
                    new ChessPosition(curr_row+1, curr_col-1)
            );

            // check if ally or end of board, otherwise add
            addToList(options, possibleMoves, board, myPosition, myColor);
            return possibleMoves;
        }
    }

    public class QueenMove {

    }

    public class PawnMove {

    }
}
