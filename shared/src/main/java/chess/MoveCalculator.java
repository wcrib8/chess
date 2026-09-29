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
}
