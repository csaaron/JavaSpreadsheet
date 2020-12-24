package ssUtils;

/**
 * 
 */
public class FormulaFormatException extends Exception
{
	/**
	 * Constructs a new FormulaFormatException with the given message and Throwable
	 * cause of this exception.
	 */
	public FormulaFormatException(String message, Throwable cause)
	{
		super(message, cause);

	}

	/**
	 * Constructs a FormulaFormatException using the given message. Does not provide
	 * Throwable cause. One can be provided after creation using 
	 * initCause(Throwable cause);
	 */
	public FormulaFormatException(String message)
	{
		super(message);
	}
}
