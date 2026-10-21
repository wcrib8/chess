package chess.piecemoves;

import chess.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
        // set variables
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        int currentRow = myPosition.getRow();
        int currentColumn = myPosition.getColumn();
        ChessGame.TeamColor myColor = piece.getTeamColor();

        // set directional variable for white vs. black
        int advanceDirection;
        if (myColor == ChessGame.TeamColor.WHITE) {
            advanceDirection = 1;
        } else {
            advanceDirection = -1;
        }

        // check for diagonal spots if enemy
        ArrayList<ChessPosition> options = new ArrayList<>(List.of(
                new ChessPosition(currentRow + advanceDirection, currentColumn -1),
                new ChessPosition(currentRow + advanceDirection, currentColumn +1),
                new ChessPosition(currentRow + advanceDirection, currentColumn)
        ));
        // if piece hasn't moved add extra move option
        ChessPiece prevSquare = board.getPiece(new ChessPosition(currentRow + advanceDirection, currentColumn));
        if (piece.checkIfMoved(myPosition) && prevSquare == null) {
            options.add(new ChessPosition(currentRow + advanceDirection * 2, currentColumn));
        }

        pawnAddToList(options, possibleMoves, board, myPosition, myColor);

        return possibleMoves;
    }
}
