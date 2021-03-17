package spreadsheet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import ssUtils.IsValidFunctor;
import ssUtils.Normalizer;
import ssUtils.DependancyGraph;
import ssUtils.Formula;
import ssUtils.Lookup;

public class Spreadsheet extends AbstractSpreadsheet
{
	// A graph that keeps track of references contained in each formula
	private DependancyGraph dependencies;

	// A HashMap keyed by cell names and contains all non empty cells.
	// If a cell becomes empty, will be removed from the dictionary.
	private HashMap<String, Cell> cells;

	/**
	 * Creates a new spreadsheet. In a new spreadsheet, the contents of every cell
	 * is the empty string. This constructor imposes no extra validity conditions,
	 * normalizes every cell name to itself, and has version "default".
	 */
	public Spreadsheet()
	{
		this(x -> true, x -> x, "default");
	}

	/**
	 * Creates a new spreadsheet. In a new spreadsheet, the contents of every cell
	 * is the empty string. The provided isValid delegate is used to impose
	 * additional restrictions to the validity of cell names. The normalize delegate
	 * allows cell names to be stored in a standardized format prior to use. Version
	 * provided to allow user to define versioning schema.
	 */
	public Spreadsheet(IsValidFunctor isValid, Normalizer normalize, String version)
	{
		super(isValid, normalize, version);

		dependencies = new DependancyGraph();
		cells = new HashMap<String, Cell>();
	}

	/**
	 * Reads the saved spreadsheet from the file stored at the provided filePath and
	 * uses it to construct a new spreadsheet. The new spreadsheet will use the
	 * provided validity delegate, normalization delegate and version.
	 * 
	 * If the version of the saved spreadsheet does not match the version parameter
	 * provided to the constructor, throws a SpreadsheetReadWriteException.
	 * 
	 * If any other problems such as, invalid names, circular dependencies, problems
	 * opening reading or closing the file occurs, throws a
	 * SpreadsheetReadWriteException.
	 */
	public Spreadsheet(String filePath, IsValidFunctor isValid, Normalizer normalize, String version)
	{
		this(isValid, normalize, version);
	}

	/**
	 * Returns the version information of the spreadsheet saved in the named file.
	 * If there are any problems opening, reading or closing the file, the method
	 * should throw a SpreadsheetReadWriteException with an explanatory message.
	 */
	@Override
	public String getSavedVersion(String filename)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(String filename)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * If name is null or invalid throws an InvalidNameException
	 * 
	 * Otherwise, returns the value (as opposed to the contents) of the named cell.
	 * The return value should be either a string, a double or a FormulaError.
	 */
	@Override
	public Object getCellValue(String name) throws InvalidNameException
	{
		String normalizedName = safelyNormalize(name);
		cellNameValidator(normalizedName);

		if (cells.containsKey(normalizedName))
			return cells.get(normalizedName).getCellValue();
		else
			return "";
	}

	@Override
	public Iterable<String> getNamesOfAllNonemptyCells()
	{
		HashSet<String> copyOfNames = new HashSet<String>();
		for (String cellName : cells.keySet())
		{
			copyOfNames.add(cellName);
		}
		return copyOfNames;
	}

	@Override
	public Object getCellContents(String name) throws InvalidNameException
	{

		// normalize and validate name
		String normalName = safelyNormalize(name);
		cellNameValidator(normalName);

		if (cells.containsKey(normalName))
			return cells.get(normalName).getCellContents();
		else
			return "";
	}

	/**
	 * If content is null, throws an IllegalArgumentException.
	 * 
	 * Otherwise, if name is null or invalid, throws an InvalidNameException.
	 * 
	 * Otherwise, if content parses as a double, the contents of the named cell
	 * becomes that double.
	 * 
	 * Otherwise, if content begins with the character '=', an attempt is made to
	 * parse the remainder of content into a Formula f using the Formula
	 * constructor. There are then three possibilities:
	 * 
	 * (1) If the remainder of content cannot be parsed into a Formula, a
	 * SpreadsheetUtilities.FormulaFormatException is thrown.
	 * 
	 * (2) Otherwise, if changing the contents of the named cell to be f would cause
	 * a circular dependency, a CircularException is thrown.
	 * 
	 * (3) Otherwise, the contents of the named cell becomes f.
	 * 
	 * Otherwise, the contents of the named cell becomes content.
	 * 
	 * If an exception is not thrown, the method returns a set consisting of name
	 * plus the names of all other cells whose value depends, directly or
	 * indirectly, on the named cell.
	 * 
	 * For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the set
	 * {A1, B1, C1} is returned.
	 * 
	 * @throws InvalidNameException
	 * @throws CircularException
	 */
	@Override
	public Set<String> setContentsOfCell(String name, String content) throws InvalidNameException, CircularException
	{
		if (content == null)
			throw new IllegalArgumentException();

		String normalName = safelyNormalize(name);
		cellNameValidator(name);

		if (Formula.ExtensionMethods.isDoubleString(content))
		{
			double doubleContent = Double.parseDouble(content);
			return setCellContents(normalName, doubleContent);
		}

		if (content.length() > 0 && content.charAt(1) == '=')
		{
			String formulaContent = content.length() > 1 ? content.substring(1) : "";
			Formula formula = new Formula(formulaContent, super.getNormalize(), super.getIsValid());
			return setCellContents(normalName, formula);
		}

		return setCellContents(normalName, content);

	}

	@Override
	protected Set<String> setCellContents(String name, String text) throws InvalidNameException, CircularException
	{
		cellNameValidator(name);
		setChanged(true);

		// Empty string is a special case which clears the cell.
		if (text.equals(""))
		{
			emptyCell(name);
			Iterable<String> recalcCells = getCellsToRecalculate(name);
			// recalculateCells(recalcCells); TODO: Might need this, wasn't in old project
			return hashSetifyIterable(recalcCells);
		}

		Cell cell = new Cell(text);
		addCellToHashMap(name, cell);

		Iterable<String> recalcCells = getCellsToRecalculate(name);
		recalculateCells(recalcCells);

		return hashSetifyIterable(recalcCells);
	}

	@Override
	protected Set<String> setCellContents(String name, Formula formula) throws InvalidNameException, CircularException
	{
		if (formula == null)
			throw new IllegalArgumentException("formula cannot be null");

		cellNameValidator(name);
		checkCircularDependency(name, formula);

		Cell cell = new Cell(formula, new LookupCellValue()); // TODO: make instance variable
		addCellToHashMap(name, cell);

		setChanged(true);

		Iterable<String> recalcCells = getCellsToRecalculate(name);
		recalculateCells(recalcCells);

		return hashSetifyIterable(recalcCells);

	}

	@Override
	protected Set<String> setCellContents(String name, double number) throws InvalidNameException, CircularException
	{
		cellNameValidator(name);

		Cell cell = new Cell(number);
		addCellToHashMap(name, cell);

		setChanged(true);

		Iterable<String> recalcCells = getCellsToRecalculate(name);
		recalculateCells(recalcCells);

		return hashSetifyIterable(recalcCells);

	}

	/**
	 *  
	 * @formatter:off
	 * If name is null, throws an IllegalArgumentException.
	 * 
	 * Otherwise, if name isn't a valid cell name, throws an InvalidNameException.
	 * 
	 * Otherwise, returns an enumeration, without duplicates, of the names of all
	 * cells whose values depend directly on the value of the named cell. In other
	 * words, returns an enumeration, without duplicates, of the names of all cells
	 * that contain formulas containing name.
	 * 
	 * For example, suppose that 
	 * 	A1 contains 3 
	 * 	B1 contains the formula A1 * A1 
	 * 	C1 contains the formula B1 + A1 
	 * 	D1 contains the formula B1 - C1 
	 * 
	 * The direct dependents of A1 are B1 and C1
	 * @formatter:on
	 */
	@Override
	protected Iterable<String> getDirectDependents(String name) throws InvalidNameException
	{
		if (name == null)
			throw new InvalidNameException(name);

		String normalName = safelyNormalize(name);
		cellNameValidator(normalName);

		return dependencies.getDependees(normalName);
	}

	/**
	 * Takes an Iterable of strings in the order they must be calculated and
	 * recalculates the value of each one
	 */
	private void recalculateCells(Iterable<String> recalcCells)
	{
		Lookup lookup = new LookupCellValue(); // TODO: Make instance variable for default lookup.
		for (String cell : recalcCells)
			cells.get(cell).recalculateCellValue(lookup);
	}

	/**
	 * If name is null, returns null. Else returns Normalize(name)
	 */
	private String safelyNormalize(String name)
	{
		if (name != null)
			return getNormalize().normalize(name);

		return null;
	}

	/**
	 * A function which returns true if name is a valid cell name and any additional
	 * restrictions imposed by the validator are met, else, returns false. Used to
	 * pass to formulas to validate their variable names
	 */
	private boolean validator(String name)
	{
		if (name == null)
			return false;

		String pattern = "^[a-zA-Z]+\\d+$"; // TODO: Turn pattern into global variable
		return Pattern.matches(pattern, name) && getIsValid().isValid(name);
	}

	/**
	 * If name is null or invalid, throws an InvalidNameException. Used to validate
	 * cell names passed to the spreadsheet.
	 */
	private void cellNameValidator(String normalizedName) throws InvalidNameException
	{
		if (!validator(normalizedName))
		{
			throw new InvalidNameException("Invalid cell name");
		}

	}

	/**
	 * Updates dependencies then checks for circular dependencies.
	 * 
	 * If there is a circular dependency, ensures the spreadsheet is not changed and
	 * old state of dependencies is restored, then throws CircularDependency. Else
	 * returns a set consisting of name plus the names of all other cells whose
	 * value depends directly or indirectly on the cell name.
	 */
	private Iterable<String> checkCircularDependency(String name, Formula formula)
			throws CircularException, InvalidNameException
	{
		// preserve current state
		Iterable<String> oldDependencies = dependencies.getDependents(name);
		// update the graph
		dependencies.replaceDependents(name, formula.getVariables());

		try
		{
			return getCellsToRecalculate(name);
		}
		catch (CircularException e)
		{
			dependencies.replaceDependents(name, oldDependencies);
			throw new CircularException(name);
		}
	}

	/**
	 * Removes named cell's dependents from the graph then removes it from cells per
	 * invariant
	 */
	private void emptyCell(String name)
	{
		dependencies.replaceDependents(name, new ArrayList<String>());
		cells.remove(name);
	}

	/**
	 * Takes a cell name and the corresponding cell and adds it to the dictionary.
	 * If the cell is replacing a cell which had a formula in it, removes the cells
	 * dependents.
	 */
	private void addCellToHashMap(String name, Cell cell)
	{
		emptyCell(name);
		cells.put(name, cell);
	}

	/**
	 * Places the elements of iterable into a HashSet and returns it.
	 */
	private HashSet<String> hashSetifyIterable(Iterable<String> iterable)
	{
		if (iterable instanceof HashSet)
			return (HashSet<String>) iterable;

		if (iterable instanceof Collection)
			return new HashSet<String>((Collection<String>) iterable);

		HashSet<String> set = new HashSet<String>();
		for (String s : iterable)
		{
			set.add(s);
		}
		return set;

	}

	/**
	 * Provides a lookup function for evaluating functions contained in this
	 * spreadsheet.
	 */
	private class LookupCellValue implements Lookup
	{

		/**
		 * If the value of variable can be mapped to a double, returns that double else
		 * throws an exception.
		 */
		@Override
		public double lookup(String variable)
		{
			try
			{
				Object value = getCellValue(variable);
				if (value instanceof Double)
					return (Double) value;

				throw new IllegalArgumentException("Could not look up the value of variable" + variable);

			}
			catch (InvalidNameException e)
			{
				throw new IllegalArgumentException("Could not look up the value of variable" + variable);
			}
		}

	}
}