package dataaccess;

/**
 * Indicates there was an error connecting to the database
 * thrown if a user attempts to update a non-existent game
 */
public class DataAccessException extends Exception{
    public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable ex) {
        super(message, ex);
    }
}
