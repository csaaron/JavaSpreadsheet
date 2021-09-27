package spreadsheet;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import ssUtils.Formula;
import ssUtils.Normalizer;
import ssUtils.IsValid;

public abstract class AbstractSpreadsheet
{

    /**
     * True of this spreadsheet has been modified since it was created or saved
     * (whichever happened most recently); false otherwise.
     */
    private boolean changed;

    /**
     * a functor containing a method to determine whether a string that consists
     * of one or more letters followed by one or more digits is a valid variable
     * name.
     */
    private IsValid isValid;

    /**
     * A functor containing a method for converting cell names to its standard
     * form
     */
    private Normalizer normalize;

    /**
     * Version information
     */
    private String version;

    /**
     * Creates a new Spreadsheet. In a new Spreadsheet, the contents of every
     * cell is the empty string. The provided isValid functor is used to impose
     * additional restrictions to the validity of cell names. The normalize
     * functor allows cell names to be stored in a standardized format prior to
     * use. Version provided to allow user to define versioning schema.
     */
    public AbstractSpreadsheet(IsValid isValid, Normalizer normalize, String version)
    {
        this.isValid = isValid;
        this.normalize = normalize;
        this.version = version;

        this.changed = false;
    }

    /**
     * Returns the version information of the spreadsheet saved in the named
     * file. If there are any problems opening, reading or closing the file, the
     * method should throw a SpreadsheetReadWriteException with an explanatory
     * message.
     *
     * @throws SpreadsheetReadWriteException
     */
    public abstract String getSavedVersion(String filename) throws SpreadsheetReadWriteException;

    /**
     * @formatter:off Writes the contents of this Spreadsheet to the named file
     * using an XML format.
     *
     * The XML elements should be structured as follows:
     *
     *
     * <spreadsheet version="version information goes here">
     *
     * <cell>
     * <name>
     * cell name goes here
     * </name>
     * <contents>
     * cell contents goes here
     * </contents>
     * </cell>
     *
     * </spreadsheet>
     *
     * There should be one cell element for each non-empty cell in the
     * spreadsheet. If the cell contains a string, it should be written as the
     * contents. If the cell contains a double d, d.ToString() should be written
     * as the contents. If the cell contains a Formula f, f.ToString() with "="
     * prepended should be written as the contents.
     *
     * If there are any problems opening, writing, or closing the file, the
     * method should throw a SpreadsheetReadWriteException with an explanatory
     * message.
     * @Formatter:on
     */
    public abstract void save(String filename) throws SpreadsheetReadWriteException;

    /**
     * If name is null or invalid throws an InvalidNameException
     *
     * Otherwise, returns the value (as opposed to the contents) of the named
     * cell. The return value should be either a string, a double or a
     * FormulaError.
     */
    public abstract Object getCellValue(String name) throws InvalidNameException;

    /**
     * Returns an iterable containing the names of all the non-empty cells in
     * this Spreadsheet
     */
    public abstract Iterable<String> getNamesOfAllNonemptyCells();

    /**
     * If name is null or invalid, throws an InvalidNameException.
     *
     * Otherwise, returns the contents (as opposed to the value) of the named
     * cell. The return value should be either a string, a double, or a Formula.
     */
    public abstract Object getCellContents(String name) throws InvalidNameException;

    /**
     * If content is null, throws an IllegalArgumentException.
     *
     * Otherwise, if name is null or invalid, throws an InvalidNameException.
     *
     * Otherwise, if content parses as a double, the contents of the named cell
     * becomes that double.
     *
     * Otherwise, if content begins with the character '=', an attempt is made
     * to parse the remainder of content into a Formula f using the Formula
     * constructor. There are then three possibilities:
     *
     * (1) If the remainder of content cannot be parsed into a Formula, a
     * SpreadsheetUtilities.FormulaFormatException is thrown.
     *
     * (2) Otherwise, if changing the contents of the named cell to be f would
     * cause a circular dependency, a CircularException is thrown.
     *
     * (3) Otherwise, the contents of the named cell becomes f.
     *
     * Otherwise, the contents of the named cell becomes content.
     *
     * If an exception is not thrown, the method returns a set consisting of
     * name plus the names of all other cells whose value depends, directly or
     * indirectly, on the named cell.
     *
     * For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
     * set {A1, B1, C1} is returned.
     *
     * @throws InvalidNameException
     * @throws CircularException
     */
    public abstract Set<String> setContentsOfCell(String name, String content) throws InvalidNameException, CircularException;

    /**
     * If name is null or invalid, throws an InvalidNameException.
     *
     * Otherwise, the contents of the named cell becomes number. The method
     * returns a set consisting of name plus the names of all other cells whose
     * value depends, directly or indirectly, on the named cell.
     *
     * For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
     * set {A1, B1, C1} is returned.
     */
    protected abstract Set<String> setCellContents(String name, double number) throws InvalidNameException, CircularException;

    /**
     * If name is null or invalid, throws an InvalidNameException.
     *
     * Otherwise, the contents of the named cell becomes text. The method
     * returns a set consisting of name plus the names of all other cells whose
     * value depends, directly or indirectly, on the named cell.
     *
     * For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
     * set {A1, B1, C1} is returned.
     */
    protected abstract Set<String> setCellContents(String name, String text) throws InvalidNameException, CircularException;

    /**
     * If name is null or invalid, throws an InvalidNameException.
     *
     * Otherwise, if changing the contents of the named cell to be the formula
     * would cause a circular dependency, throws a CircularException. (No change
     * is made to the spreadsheet.)
     *
     * Otherwise, the contents of the named cell becomes formula. The method
     * returns a Set consisting of name plus the names of all other cells whose
     * value depends, directly or indirectly, on the named cell.
     *
     * For example, if name is A1, B1 contains A1*2, and C1 contains B1+A1, the
     * set {A1, B1, C1} is returned.
     */
    protected abstract Set<String> setCellContents(String name, Formula formula) throws InvalidNameException, CircularException;

    /**
     * @formatter:off If name is null, throws an IllegalArgumentException.
     *
     * Otherwise, if name isn't a valid cell name, throws an
     * InvalidNameException.
     *
     * Otherwise, returns an iterable, without duplicates, of the names of all
     * cells whose values depend directly on the value of the named cell. In
     * other words, returns an iterable, without duplicates, of the names of all
     * cells that contain formulas containing name.
     *
     * For example, suppose that A1 contains 3 B1 contains the formula A1 * A1
     * C1 contains the formula B1 + A1 D1 contains the formula B1 - C1
     *
     * The direct dependents of A1 are B1 and C1
     * @formatter:on
     */
    protected abstract Iterable<String> getDirectDependents(String name) throws InvalidNameException;

    /**
     * Returns an Iterable<String> iterating over all cells which need to be
     * recalculated in the order they should be recalculated when the contents
     * of the cells in names has changed
     * 
     * If a cycle is detected in the dependencies throws CurcularException
     * If an invalid name is contained in names throws InvalidNameException
     */
    protected Iterable<String> getCellsToRecalculate(Set<String> names) throws CircularException, InvalidNameException
    {
        LinkedList<String> changed = new LinkedList<String>();
        HashSet<String> visited = new HashSet<String>();
        for (String name : names)
        {
            if (!visited.contains(name))
            {
                visit(name, name, visited, changed);
            }
        }

        return changed;

    }

    /**
     * Returns an Iterable<String> iterating over all cells which need to be
     * recalculated in the order they should be recalculated when the contents
     * of the the String name has changed
     * 
     * Method provided for convenience when passing a single cell name
     * 
     
     */
    protected Iterable<String> getCellsToRecalculate(String name) throws CircularException, InvalidNameException
    {

        HashSet<String> nameSet = new HashSet<String>(1);
        nameSet.add(name);
        return getCellsToRecalculate(nameSet);
    }

    /**
     *  Helper method for getCellsToRecalculate 
     * 
     * Marks name as visited and recursively follows dependencies of name to new
     * cells then adds name to list of cells to be recalculated
     * 
     * If a cycle is detected in the dependencies of name throws CurcularException
     * If an invalid name is contained in names throws InvalidNameException
     */
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

    /**
     * *************************************************************************
     * Getters and setters for instance variables
     * *************************************************************************
     */
    
    /**
     * Returns true if this spreadsheet has been modified since it was created
     * or saved (whichever happened most recently); false otherwise;
     */
    public boolean getChanged()
    {
        return changed;
    }

    /**
     * A method for marking this spreadsheet as changed when it has been
     * modified since it was created or saved (whichever happened most
     * recently); Or should be marked as false otherwise.
     */
    protected void setChanged(boolean change)
    {
        changed = change;
    }

    /**
     * Sets this spreadsheet's isValid functor which provides a method to
     * validate variable names.
     */
    protected void setIsValid(IsValid validator)
    {
        isValid = validator;
    }

    /**
     * Gets this spreadsheet's isValid functor containing the function for
     * validating variable names
     */
    public IsValid getIsValid()
    {
        return isValid;
    }

    /**
     * Sets this spreadsheet's Normalizer to normal. Normal contains a method
     * used to convert a cell name to its standard form. For example, normalize
     * might convert names to upper case.
     */
    protected void setNormalize(Normalizer normal)
    {
        normalize = normal;
    }

    /**
     * Gets this spreadsheet's Normalizer. Normal contains a method used to
     * convert a cell name to its standard form. For example, normalize might
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

}
