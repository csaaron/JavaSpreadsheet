package spreadsheet;

import ssUtils.Formula;
import ssUtils.Lookup;

public class Cell
{
	// defines cell types used to speed up type casting of returned contents and
	// values of the cells.
	enum CellType
	{
		STRING_TYPE, DOUBLE_TYPE, FORMULA_TYPE
	}

	private Object cellContents;

	private Object cellValue;

	// the type of this cell is access only after the cell is created.
	private CellType type;

	/**
	 * Constructs a new Cell, containing the given contents. The value of this Cell
	 * is equal to its contents.
	 */
	public Cell(String contents)
	{
		type = CellType.STRING_TYPE;

		cellContents = contents;
		cellValue = contents;
	}

	/**
	 * Constructs a new Cell, containing the given contents. The value of this Cell
	 * is equal to its contents.
	 */
	public Cell(double contents)
	{
		type = CellType.DOUBLE_TYPE;

		cellContents = contents;
		cellValue = contents;
	}

	/**
	 * Constructs a new Cell, containing the given contents. The value of this Cell
	 * is equal the result of evaluating contents, using the given lookup functor.
	 */
	public Cell(Formula contents, Lookup lookup)
	{
		type = CellType.FORMULA_TYPE;

		cellContents = contents;
		cellValue = contents.evaluate(lookup);
	}
	
	/**
	 * Returns the contents of this cell as an Object. 
	 */
	public Object getCellContents()
	{
		return cellContents;
	}
	
	/**
	 * Returns the value of this cell as an Object.
	 */
	public Object getCellValue()
	{
		return cellValue;
	}
	
	/**
	 * Reports the type of the cell's contents.
	 */
	public CellType getType()
	{
		return type;
	}
	
	/**
	 * Recalculates the cell's value
	 */
	public void recalculateCellValue(Lookup lookup)
	{
		if (type == Cell.CellType.FORMULA_TYPE)
		{
			Formula f = (Formula)cellContents;
			cellValue = f.evaluate(lookup);
		}
	}
}
