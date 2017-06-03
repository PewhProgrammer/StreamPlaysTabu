package common.database;

/**
 * Created by Thinh-Laptop on 20.04.2017.
 */
public class DatabaseException extends Exception {

    public DatabaseException() { super(); }
    public DatabaseException(String message) { super(message); }
    public DatabaseException(String message, Throwable cause) { super(message, cause); }
    public DatabaseException(Throwable cause) { super(cause); }
}
