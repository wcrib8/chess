package chess;

import java.util.List;
import java.util.Collection;

public abstract class MoveCalculator {

    // abstract method
    public abstract Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece);

    //general move piece func to call subclass
    public static Collection<ChessMove> getMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        MoveCalculator calc = switch (piece.getPieceType()) {
            case BISHOP -> new BishopMove();
            case ROOK -> new RookMove();
            case KNIGHT -> new KnightMove();
            case KING -> new KingMove();
            case QUEEN -> new QueenMove();
            case PAWN -> new PawnMove();
        };
        return calc.pieceMoves(board, myPosition, piece);
    }

    // helper function for checking list of move options
    void addToList(List<ChessPosition> moveOptions, List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        for (ChessPosition pos : moveOptions) {
            if (pos.isValid()) {
                if (board.getPiece(pos) == null) {
                    possibleMoves.add(new ChessMove(myPosition, pos, null));
                } else if (board.getPiece(pos).getTeamColor() != myColor) {
                    possibleMoves.add(new ChessMove(myPosition, pos, null));
                }
            }
        }
    }

    // pawn version of above func
    void pawnAddToList(List<ChessPosition> moveOptions, List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        for (ChessPosition pos : moveOptions) {
            if (pos.isValid()) {
                if (pos.getColumn() != myPosition.getColumn()) {
                    if (board.getPiece(pos) == null) continue;
                    if (board.getPiece(pos).getTeamColor() != myColor) {
                        if (canPromote(pos)) {
                            possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.KNIGHT));
                        } else possibleMoves.add(new ChessMove(myPosition, pos, null));
                    }
                }
                else if (board.getPiece(pos) == null) {
                    if (canPromote(pos)) {
                        possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(myPosition, pos, ChessPiece.PieceType.KNIGHT));
                    } else possibleMoves.add(new ChessMove(myPosition, pos, null));
                }
            }
        }
    }

    // promotion helper
    boolean canPromote(ChessPosition pos) {
        // check if white at 8 or black at 1
        return pos.getRow() == 8 || pos.getRow() == 1;
    }

    // helper function for bishops, rooks, and queens
    void addInLine(List<ChessMove> possibleMoves, ChessBoard board, ChessPosition myPosition, ChessGame.TeamColor myColor, int x, int y) {
        // while is valid and empty, add and continue; if enemy, add to list then stop; if an ally, stop
        ChessPosition pos = new ChessPosition(myPosition.getRow()+x, myPosition.getColumn()+y);
        while (pos.isValid()) {
            if (board.getPiece(pos) == null) {
                possibleMoves.add(new ChessMove(myPosition, pos, null));
            } else if (board.getPiece(pos).getTeamColor() != myColor) {
                possibleMoves.add(new ChessMove(myPosition, pos, null));
                break;
            } else if (board.getPiece(pos).getTeamColor() == myColor) break;
            pos = new ChessPosition(pos.getRow()+x, pos.getColumn()+y);
        }
    }


    //subclasses for each piece
//    class BishopMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            // setup needed vars and list
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // add to possible moves list with helper func, for each direction
//            addInLine(possibleMoves, board, myPosition, myColor, 1, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, 1, -1);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, -1);
//
//            return possibleMoves;
//        }
//    }

//    class RookMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            // setup needed vars and list
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // add to possible moves list with helper func, for each direction
//            addInLine(possibleMoves, board, myPosition, myColor, 1, 0);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, 0);
//            addInLine(possibleMoves, board, myPosition, myColor, 0, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, 0, -1);
//
//            return possibleMoves;
//        }
//    }

//    class KnightMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            // check all options
//            int curr_row = myPosition.getRow();
//            int curr_col = myPosition.getColumn();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // add knight move options to list
//            List<ChessPosition> options = Arrays.asList(
//                new ChessPosition(curr_row+2, curr_col-1),
//                new ChessPosition(curr_row+2, curr_col+1),
//                new ChessPosition(curr_row-2, curr_col-1),
//                new ChessPosition(curr_row-2, curr_col+1),
//                new ChessPosition(curr_row+1, curr_col-2),
//                new ChessPosition(curr_row-1, curr_col-2),
//                new ChessPosition(curr_row+1, curr_col+2),
//                new ChessPosition(curr_row-1, curr_col+2)
//            );
//
//            addToList(options, possibleMoves, board, myPosition, myColor);
//            return possibleMoves;
//        }
//    }

//    class KingMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            // check all options
//            int curr_row = myPosition.getRow();
//            int curr_col = myPosition.getColumn();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // add move options
//            List<ChessPosition> options = Arrays.asList(
//                    new ChessPosition(curr_row+1, curr_col),
//                    new ChessPosition(curr_row+1, curr_col+1),
//                    new ChessPosition(curr_row, curr_col+1),
//                    new ChessPosition(curr_row-1, curr_col+1),
//                    new ChessPosition(curr_row-1, curr_col),
//                    new ChessPosition(curr_row-1, curr_col-1),
//                    new ChessPosition(curr_row, curr_col-1),
//                    new ChessPosition(curr_row+1, curr_col-1)
//            );
//
//            // check if ally or end of board, otherwise add
//            addToList(options, possibleMoves, board, myPosition, myColor);
//            return possibleMoves;
//        }
//    }

//    class QueenMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            // setup needed vars and list
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // add to possible moves list with helper func, for each direction
//            addInLine(possibleMoves, board, myPosition, myColor, 1, 0);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, 0);
//            addInLine(possibleMoves, board, myPosition, myColor, 0, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, 0, -1);
//            addInLine(possibleMoves, board, myPosition, myColor, 1, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, 1, -1);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, 1);
//            addInLine(possibleMoves, board, myPosition, myColor, -1, -1);
//
//            return possibleMoves;
//        }
//    }

//    class PawnMove {
//
//        public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
//            // set variables
//            ArrayList<ChessMove> possibleMoves = new ArrayList<>();
//            int curr_row = myPosition.getRow();
//            int curr_col = myPosition.getColumn();
//            ChessGame.TeamColor myColor = piece.getTeamColor();
//
//            // set directional variable for white vs. black
//            int advanceDirection;
//            if (myColor == ChessGame.TeamColor.WHITE) {
//                advanceDirection = 1;
//            } else advanceDirection = -1;
//
//            // check for diagonal spots if enemy
//            ArrayList<ChessPosition> options = new ArrayList<>(List.of(
//                    new ChessPosition(curr_row+advanceDirection, curr_col-1),
//                    new ChessPosition(curr_row+advanceDirection, curr_col+1),
//                    new ChessPosition(curr_row+advanceDirection, curr_col)
//            ));
//            // if piece hasn't moved add extra move option
//            ChessPiece prevSquare = board.getPiece(new ChessPosition(curr_row+advanceDirection, curr_col));
//            if (piece.checkMoved(myPosition) && prevSquare == null) {
//                options.add(new ChessPosition(curr_row+advanceDirection*2, curr_col));
//            }
//
//            pawnAddToList(options, possibleMoves, board, myPosition, myColor);
//
//            return possibleMoves;
//        }
//    }
}
