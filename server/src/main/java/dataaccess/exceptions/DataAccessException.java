package dataaccess.exceptions;

import java.sql.SQLException;

public class DataAccessException extends Exception {
    public DataAccessException(String message, SQLException ex) {
        super(message);
    }
}
