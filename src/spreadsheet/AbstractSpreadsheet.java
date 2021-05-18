package spreadsheet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import ssUtils.Formula;
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
		
		this.changed = false;
	}
	
	public abstract String getSavedVersion (String filename) throws SpreadsheetReadWriteException;
	
	public abstract void save(String filename) throws SpreadsheetReadWriteException;
	
	public abstract Object getCellValue(String name) throws InvalidNameException;
	
	public abstract Iterable<String> getNamesOfAllNonemptyCells();
	
	public abstract Object getCellContents(String name) throws InvalidNameException;
	
	public abstract Set<String> setContentsOfCell(String name, String content) throws InvalidNameException, CircularException;
	
	protected abstract Set<String> setCellContents(String name, double number) throws InvalidNameException, CircularException;
	
	protected abstract Set<String> setCellContents(String name, String text) throws InvalidNameException, CircularException;
	
	protected abstract Set<String> setCellContents(String name, Formula formula) throws InvalidNameException, CircularException;
	
	protected abstract Iterable<String> getDirectDependents(String name) throws InvalidNameException;
	
	protected Iterable<String> getCellsToRecalculate(Set<String> names) throws CircularException, InvalidNameException
	{
		LinkedList<String> changed = new LinkedList<String>();
		HashSet<String> visited = new HashSet<String>();
		for(String name : names)
		{
			if(!visited.contains(name))
			{
				visit(name, name, visited, changed);
			}
		}
		
		return changed;
		
	}
	
	protected Iterable<String> getCellsToRecalculate(String name) throws CircularException, InvalidNameException
	{

		HashSet<String> nameSet = new HashSet<String>(1);
		nameSet.add(name);
		return getCellsToRecalculate(nameSet);
	}
	
	private void visit(String start, String name, Set<String> visited, LinkedList<String> changed) throws CircularException, InvalidNameException
	{
		visited.add(name);
		for (String n : getDirectDependents(name))
		{
			if (n.equals(start))
			{
				throw new CircularException(name);
			}
			else if (!visited.contains(n))
			{
				visit(start, n, visited, changed);
			}
		}
		changed.addFirst(name);
	}
	
	

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
