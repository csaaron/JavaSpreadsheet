package ssUtils;

/**
 * The lookup interface provides a method which determines the values of
 * variables.
 */
public interface Lookup
{
	/**
	 * Lookup takes a string, variable and returns the value of that variable if one
	 * exists.
	 */
	public double lookup(String variable);
}
