package chess.piecemoves;

import chess.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class KnightMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        // check all options
        int currentRow = myPosition.getRow();
        int currentColumn = myPosition.getColumn();
        ChessGame.TeamColor myColor = piece.getTeamColor();

        // add knight move options to list
        List<ChessPosition> options = Arrays.asList(
                new ChessPosition(currentRow +2, currentColumn -1),
                new ChessPosition(currentRow +2, currentColumn +1),
                new ChessPosition(currentRow -2, currentColumn -1),
                new ChessPosition(currentRow -2, currentColumn +1),
                new ChessPosition(currentRow +1, currentColumn -2),
                new ChessPosition(currentRow -1, currentColumn -2),
                new ChessPosition(currentRow +1, currentColumn +2),
                new ChessPosition(currentRow -1, currentColumn +2)
        );

        addToList(options, possibleMoves, board, myPosition, myColor);
        return possibleMoves;
    }
}
