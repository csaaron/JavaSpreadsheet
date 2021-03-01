package spreadsheet;

public class InvalidNameException extends Exception
{
	/**
	 * Constructs a new InvalidNameException with the given message and Throwable
	 * cause of this exception.
	 */
	public InvalidNameException(String message, Throwable cause)
	{
		super(message, cause);

	}

	/**
	 * Constructs a InvalidNameException using the given message. Does not provide
	 * Throwable cause. One can be provided after creation using 
	 * initCause(Throwable cause);
	 */
	public InvalidNameException(String message)
	{
		super(message);
	}

}
