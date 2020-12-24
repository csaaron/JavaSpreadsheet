package ssUtils;

/**
 * IsValidFunctor is an interface which defines a method, isValid which checks
 * the validity of a string.
 */
public interface IsValidFunctor
{
	/**
	 * Takes a String s and returns true if that string is valid, false otherwise.
	 */
	public boolean isValid(String s);
}