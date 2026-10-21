package chess;

import chess.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class KingMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        // check all options
        int currentRow = myPosition.getRow();
        int currentColumn = myPosition.getColumn();
        ChessGame.TeamColor myColor = piece.getTeamColor();

        // add move options
        List<ChessPosition> options = Arrays.asList(
                new ChessPosition(currentRow +1, currentColumn),
                new ChessPosition(currentRow +1, currentColumn +1),
                new ChessPosition(currentRow, currentColumn +1),
                new ChessPosition(currentRow -1, currentColumn +1),
                new ChessPosition(currentRow -1, currentColumn),
                new ChessPosition(currentRow -1, currentColumn -1),
                new ChessPosition(currentRow, currentColumn -1),
                new ChessPosition(currentRow +1, currentColumn -1)
        );

        // check if ally or end of board, otherwise add
        addToList(options, possibleMoves, board, myPosition, myColor);
        return possibleMoves;
    }
}
