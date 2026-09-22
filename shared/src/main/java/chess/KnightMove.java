package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class KnightMove extends MoveCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition, ChessPiece piece) {
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
