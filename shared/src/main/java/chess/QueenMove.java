package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
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
