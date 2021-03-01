package spreadsheet;

import java.util.Set;
import ssUtils.IsValidFunctor;
import ssUtils.Normalizer;

public class Spreadsheet extends AbstractSpreadsheet
{

	public Spreadsheet(IsValidFunctor isValid, Normalizer normalize, String version)
	{
		super(isValid, normalize, version);
		// TODO Auto-generated constructor stub
	}

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

	@Override
	public Object getCellValue(String name)
	{
		// TODO Auto-generated method stub
		return null;
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

}
