package spreadsheet;

import java.util.Set;

import ssUtils.IsValidFunctor;
import ssUtils.Normalizer;

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

	public AbstractSpreadsheet(IsValidFunctor isValid, Normalizer normalize, String version) 
	{
		this.isValid = isValid;
		this.normalize = normalize;
		this.version = version;
	}
	
	public abstract String getSavedVersion (String filename);
	
	public abstract void save(String filename);
	
	public abstract Object getCellValue(String name);
	
	public abstract Iterable<String> getNamesOfAllNonemptyCells();
	
	public abstract Object GetCellContents(String name);
	
	public abstract Set<String> SetContentsOfCell(String name, String content);
	
	public abstract Set<String> setCellContents(String name, double number);
	
	protected abstract Set<String> SetCellContents(String name, String text);
	
	
	
	/******************************************************************************
	 * Getters and setters for instance variables
	 *****************************************************************************/
	
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

	

	

}
