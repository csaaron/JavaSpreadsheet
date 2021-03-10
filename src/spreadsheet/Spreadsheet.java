package spreadsheet;

import java.util.HashMap;
import java.util.Set;
import ssUtils.IsValidFunctor;
import ssUtils.Normalizer;
import ssUtils.DependancyGraph;

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

		// TODO implement constructor
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
	public Object getCellValue(String name)
	{
		String normalizedName = safelyNormalize(name);
		CellNameValidator(normalizedName);

		if (cells.containsKey(normalizedName))
			return cells.get(normalizedName).getCellValue();
		else
			return "";
	}

	@Override
	public Iterable<String> getNamesOfAllNonemptyCells()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object GetCellContents(String name)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> setContentsOfCell(String name, String content)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> setCellContents(String name, double number)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Set<String> setCellContents(String name, String text)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Iterable<String> getDirectDependents(String name)
	{
		// TODO Auto-generated method stub
		return null;
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
	 * restrictions imposed by the validator are met. else, returns false. Used to
	 * pass to formulas to validate their variable names
	 */
	private boolean validator(String name)
	{
		if (name == null)
			return false;
		
		
	}

	private void CellNameValidator(String normalizedName)
	{
		// TODO Auto-generated method stub

	}

}
