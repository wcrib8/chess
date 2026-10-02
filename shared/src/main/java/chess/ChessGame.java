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

    // have a global last move var for en passant....

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

            // Arrays.copyOf(list,len)
            // ChessBoard tempBoard = new ChessBoard(board);
            // tempBoard.movePiece(move, curr_piece);

            ChessGame tempGame = new ChessGame();
            tempGame.board = new ChessBoard(board);
            tempGame.board.movePiece(move, curr_piece);
            if (!tempGame.isInCheck(curr_piece.getTeamColor())) valid_moves_list.add(move); // use temp_board in check???
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
        ChessPiece piece = board.getPiece(start_position);

        // check for trying to move a position with no piece
        if (piece == null) throw new InvalidMoveException("Can't perform this move: " + move);

        // tries to execute given move: if in valid moves and team colors turn
        for (ChessMove val_move : validMoves(start_position)) {
            if (val_move.equals(move)) {
                if (piece.getTeamColor() == currTeamTurn) {
                    board.movePiece(move, piece);
                    if (currTeamTurn == TeamColor.WHITE) {
                        currTeamTurn = TeamColor.BLACK;
                    } else currTeamTurn = TeamColor.WHITE;
                    return;
                }
            }
        }
        // if move illegal, throw exception
        throw new InvalidMoveException("Can't perform this move: " + move);
    }

    // use when calling make move for try and catch:
    // try {
    //      makeMove(move);
    // } catch (InvalidMoveException ex) {
    //      System.out.println("Invalid Move - " + ex.getMessage());
    // }

    ChessPosition findKing(ChessBoard board, TeamColor teamColor) {
        for (int row=1; row <= 8; row++) {
            for (int column=1; column <= 8; column++) {
                ChessPosition check_spot = new ChessPosition(row, column);
                ChessPiece spotPiece = board.getPiece(check_spot);
                if (spotPiece == null) {
                    continue;
                }
                if (spotPiece.getPieceType() == ChessPiece.PieceType.KING && spotPiece.getTeamColor() == teamColor) {
                    return check_spot;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // go through each piece on board, check if enemy team pieces can reach current teams king position:

        // get kings position
        ChessPosition currKingPosition = findKing(board, teamColor);

        for (int row=1; row <= 8; row++) {
            for (int column=1; column <= 8; column++) {
                // check if piece there, then check if enemy piece
                ChessPosition check_spot = new ChessPosition(row, column);
                ChessPiece enemyPiece = board.getPiece(check_spot);

                if (enemyPiece != null) {
                    if (enemyPiece.getTeamColor() != teamColor) {
                        Collection<ChessMove> spot_moves = enemyPiece.pieceMoves(board, check_spot);

                        // loop over each move, if end position is equal to kings position, return true
                        for (ChessMove move : spot_moves) {
                            if (move.getEndPosition().equals(currKingPosition)) {
                                return true;

                                // do I need check for an ally piece blocking? should be automatic in piece moves
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        // if in check and no valid moves, checkmate
        return isInCheck(teamColor) && noValidMovesLeft(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // if not in check, and no valid moves, stalemate
        return !isInCheck(teamColor) && noValidMovesLeft(teamColor);
    }

    boolean noValidMovesLeft(TeamColor teamColor) {
        // check whole board, if no current team pieces have valid moves, return true

        for (int row=1; row <= 8; row++) {
            for (int column=1; column <= 8; column++) {
                ChessPosition check_spot = new ChessPosition(row, column);
                ChessPiece allyPiece = board.getPiece(check_spot);

                if (allyPiece != null) {
                    if (allyPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> spot_moves = validMoves(check_spot);

                        // if it's not empty return false
                        if (!spot_moves.isEmpty()) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
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
