package spreadsheet;

public class SpreadsheetReadWriteException extends Exception
{

    /**
     * Constructs a new SpreadsheetReadWriteException with the given message and
     * Throwable cause of this exception.
     */
    public SpreadsheetReadWriteException(String message, Throwable cause)
    {
        super(message, cause);

    }

    /**
     * Constructs a SpreadsheetReadWriteException using the given message. Does
     * not provide Throwable cause. One can be provided after creation using
     * initCause(Throwable cause);
     */
    public SpreadsheetReadWriteException(String message)
    {
        super(message);
    }
}
