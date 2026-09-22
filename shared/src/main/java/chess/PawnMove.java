package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PawnMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
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
        }

        pawnAddToList(options, possibleMoves, board, myPosition, myColor);

        return possibleMoves;
    }
}
