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
            case ChessPiece.PieceType.ROOK -> RookMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.KNIGHT -> KnightMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.KING -> KingMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.QUEEN -> QueenMove.pieceMoves(board, myPosition, piece);
            case ChessPiece.PieceType.PAWN -> PawnMove.pieceMoves(board, myPosition, piece);
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

    // pawn version of above func
    static void pawnAddToList(List<ChessPosition> moveOptions, List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        for (ChessPosition pos : moveOptions) {
            if (pos.isValid()) {
                if (pos.getColumn() != myPosition.getColumn()) {
                    if (board.getPiece(pos) == null) continue;
                    if (board.getPiece(pos).getTeamColor() != myColor) {
                        possibleMoves.add(new ChessMove(myPosition, pos, null));
                    }
                    // check promotion here too
                }
                else {
                    // check in front, then skip jump if hasmoved
                    if (board.getPiece(pos) == null) {
                        possibleMoves.add(new ChessMove(myPosition, pos, null));
                    }
//                    else if (board.getPiece(pos).getTeamColor() != myColor) {
//                        possibleMoves.add(new ChessMove(myPosition, pos, null));
//                    }
                    // then promotion helper?
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
    class BishopMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            // setup needed vars and list
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add to possible moves list with helper func, for each direction
            addInLine(possibleMoves, board, myPosition, myColor, 1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, 1, -1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, -1);

            return possibleMoves;
        }
    }

    class RookMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            // setup needed vars and list
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add to possible moves list with helper func, for each direction
            addInLine(possibleMoves, board, myPosition, myColor, 1, 0);
            addInLine(possibleMoves, board, myPosition, myColor, -1, 0);
            addInLine(possibleMoves, board, myPosition, myColor, 0, 1);
            addInLine(possibleMoves, board, myPosition, myColor, 0, -1);

            return possibleMoves;
        }
    }

    class KnightMove {

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

    class KingMove {

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

    class QueenMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            // setup needed vars and list
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // add to possible moves list with helper func, for each direction
            addInLine(possibleMoves, board, myPosition, myColor, 1, 0);
            addInLine(possibleMoves, board, myPosition, myColor, -1, 0);
            addInLine(possibleMoves, board, myPosition, myColor, 0, 1);
            addInLine(possibleMoves, board, myPosition, myColor, 0, -1);
            addInLine(possibleMoves, board, myPosition, myColor, 1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, 1, -1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, 1);
            addInLine(possibleMoves, board, myPosition, myColor, -1, -1);

            return possibleMoves;
        }
    }

    class PawnMove {

        public static Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
            // set variables
            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
            int curr_row = myPosition.getRow();
            int curr_col = myPosition.getColumn();
            ChessGame.TeamColor myColor = piece.getTeamColor();

            // set directional variable for white vs. black
            int advanceDirection;
            if (myColor == ChessGame.TeamColor.WHITE) {
                advanceDirection = 1;
            } else advanceDirection = -1;

            // check for diagonal spots if enemy
            ArrayList<ChessPosition> options = new ArrayList<>(List.of(
                    new ChessPosition(curr_row+advanceDirection, curr_col-1),
                    new ChessPosition(curr_row+advanceDirection, curr_col+1),
                    new ChessPosition(curr_row+advanceDirection, curr_col)
            ));
            // if piece hasn't moved add extra move option
            ChessPiece prevSquare = board.getPiece(new ChessPosition(curr_row+advanceDirection, curr_col));
            if (piece.checkMoved(myPosition) && prevSquare == null) {
                options.add(new ChessPosition(curr_row+advanceDirection*2, curr_col));
                piece.markMoved();
            }


            pawnAddToList(options, possibleMoves, board, myPosition, myColor);

            // for this move, check if promotion(not for skip jump)
            // have promotion helper, need return 4 piece options

            return possibleMoves;
        }
    }
}
