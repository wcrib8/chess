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
    private TeamColor currentTeamTurn;
    // enforcer??
    public ChessMove lastMove;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTeamTurn = TeamColor.WHITE;
        // end game enforcer?
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
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
        ChessPiece currentPiece = board.getPiece(startPosition);
        if (currentPiece == null) {
            return null;
        }

        Collection<ChessMove> moves = currentPiece.pieceMoves(board, startPosition);
        Collection<ChessMove> validMovesList = new ArrayList<>();

        // check each move if it will leave king in check
        for (ChessMove move : moves) {
            // make board copy, make move on copy, check if is in check, add to collection if passes

            ChessGame tempGame = new ChessGame();
            tempGame.board = new ChessBoard(board);
            tempGame.board.movePiece(move, currentPiece);
            if (!tempGame.isInCheck(currentPiece.getTeamColor())) {
                validMovesList.add(move);
            }
        }
        return validMovesList;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPiece piece = board.getPiece(startPosition);

        // check for trying to move a position with no piece
        if (piece == null) {
            throw new InvalidMoveException("Can't perform this move: " + move);
        }

        // tries to execute given move: if in valid moves and team colors turn
        for (ChessMove validMove : validMoves(startPosition)) {
            if (validMove.equals(move)) {
                if (piece.getTeamColor() == currentTeamTurn) {
                    board.movePiece(move, piece);
                    lastMove = move;
                    if (currentTeamTurn == TeamColor.WHITE) {
                        currentTeamTurn = TeamColor.BLACK;
                    } else {
                        currentTeamTurn = TeamColor.WHITE;
                    }
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
                ChessPosition checkSpot = new ChessPosition(row, column);
                ChessPiece spotPiece = board.getPiece(checkSpot);
                if (spotPiece == null) {
                    continue;
                }
                if (spotPiece.getPieceType() == ChessPiece.PieceType.KING && spotPiece.getTeamColor() == teamColor) {
                    return checkSpot;
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
        ChessPosition currentKingPosition = findKing(board, teamColor);

        for (int row=1; row <= 8; row++) {
            for (int column=1; column <= 8; column++) {
                // check if piece there, then check if enemy piece
                ChessPosition checkSpot = new ChessPosition(row, column);
                ChessPiece enemyPiece = board.getPiece(checkSpot);

                if (enemyPiece != null) {
                    if (enemyPiece.getTeamColor() != teamColor) {
                        Collection<ChessMove> spotMoves = enemyPiece.pieceMoves(board, checkSpot);

                        // loop over each move, if end position is equal to kings position, return true
                        for (ChessMove move : spotMoves) {
                            if (move.getEndPosition().equals(currentKingPosition)) {
                                return true;
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
                ChessPosition checkSpot = new ChessPosition(row, column);
                ChessPiece allyPiece = board.getPiece(checkSpot);

                if (allyPiece != null) {
                    if (allyPiece.getTeamColor() == teamColor) {
                        Collection<ChessMove> spotMoves = validMoves(checkSpot);

                        // if it's not empty return false
                        if (!spotMoves.isEmpty()) {
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
        return Objects.equals(board, chessGame.board) && currentTeamTurn == chessGame.currentTeamTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTeamTurn);
    }

    @Override
    public String toString() {
        return "ChessGame{" + "board=" + board + ", currTeamTurn=" + currentTeamTurn + '}';
    }
}
