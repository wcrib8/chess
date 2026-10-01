package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor currTeamTurn;
    // enforcer??

    public ChessGame() {
        this.board = new ChessBoard();
        this.currTeamTurn = TeamColor.WHITE;
        // endgameenforcer?
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece curr_piece = board.getPiece(startPosition);
        if (curr_piece == null) return null;

        Collection<ChessMove> moves = curr_piece.pieceMoves(board, startPosition);
        Collection<ChessMove> valid_moves_list = new ArrayList<>();

        // check each move if it will leave king in check
        for (ChessMove move : moves) {
            // make board copy, make move on copy, check if is in check, add to collection if passes
            ChessBoard temp_board = board;
            temp_board.movePiece(move, curr_piece);
            if (!isInCheck(currTeamTurn)) valid_moves_list.add(move);
        }
        return valid_moves_list;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start_position = move.getStartPosition();

        //if (move == val_move for ChessMove val_move : validMoves(startPosition)) {

        //}

        // tries to execute given move. if move illegal, throw exception

        // check if in valid moves, or if not team colors turn

        // throw exception
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {this.board = board;}

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {return board;}


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currTeamTurn == chessGame.currTeamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currTeamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" + "board=" + board + ", currTeamTurn=" + currTeamTurn + '}';
    }
}
