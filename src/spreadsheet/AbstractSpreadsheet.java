package spreadsheet;

public abstract class AbstractSpreadsheet
{

	/**
	 * True of this spreadsheet has been modified since it was created or saved
	 * (whichever happened most recently); false otherwise.
	 */
	private boolean changed;
	/**
	 * a functor containing a method to determine whether a string that consists of
	 * one or more letters followed by one or more digits is a valid variable name.
	 */
	private IsValidFunctor isValid;
	/**
	 * A functor containing a method for converting cell names to its standard form
	 */
	private Normalizer normalize;
	/**
	 * Version information
	 */
	private String version;

	/**
	 * Returns true if this spreadsheet has been modified since it was created or
	 * saved (whichever happened most recently); false otherwise;
	 */
	public boolean getChanged()
	{
		return changed;
	}

	/**
	 * A method for marking this spreadsheet as changed when it has been modified
	 * since it was created or saved (whichever happened most recently); Or should
	 * be marked as false otherwise.
	 */
	protected void setChanged(boolean change)
	{
		changed = change;
	}

	/**
	 * Sets this spreadsheet's isValid functor which provides a method to validate
	 * variable names.
	 */
	protected void setIsValid(IsValidFunctor validator)
	{
		isValid = validator;
	}

	/**
	 * Gets this spreadsheet's a functor containing the function for validating
	 * variable names
	 */
	public IsValidFunctor getIsValid()
	{
		return isValid;
	}

	/**
	 * Sets this spreadsheet's Normalizer to normal. Normal contains a method used
	 * to convert a cell name to its standard form. For example, normalize might
	 * convert names to upper case.
	 */
	protected void setNormalize(Normalizer normal)
	{
		normalize = normal;
	}

	/**
	 * Getsd this spreadsheet's Normalizer. Normal contains a method used
	 * to convert a cell name to its standard form. For example, normalize might
	 * convert names to upper case.
	 */
	public Normalizer getNormalize()
	{
		return normalize;
	}

	/**
	 * Sets this spreadsheet's version information
	 */
	protected void setVersion(String vers)
	{
		version = vers;
	}

	/**
	 * Gets this spreadsheet's version information
	 */
	public String getVersion()
	{
		return version;
	}

	/********************************************************
	 * Sub-classes and sub-interfaces defined blow this block
	 ********************************************************/

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

	/**
	 * Normalizer is an interface which defines a method, Normalizer which takes a
	 * string and returns its normal form.
	 */
	public interface Normalizer
	{
		/**
		 * Takes a String s and returns the normalized version of that string.
		 */
		public String normalize(String s);
	}

}
