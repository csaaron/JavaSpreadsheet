package spreadsheet;

public class CircularException extends Exception
{
	/**
	 * Constructs a new CircularExpetion with the given message and Throwable
	 * cause of this exception.
	 */
	public CircularException(String message, Throwable cause)
	{
		super(message, cause);

	}

	/**
	 * Constructs a CircularException using the given message. Does not provide
	 * Throwable cause. One can be provided after creation using 
	 * initCause(Throwable cause);
	 */
	public CircularException(String message)
	{
		super(message);
	}

}
