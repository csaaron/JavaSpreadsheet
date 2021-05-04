package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ssUtils.Formula;

class SpreadsheetTest
{

	@BeforeEach
	void setUp() throws Exception
	{
	}

	@AfterEach
	void tearDown() throws Exception
	{
	}

	// EMPTY SPREADSHEETS
	@Test
	void testEmptyGetNull()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.getCellContents(null));
	}

	@Test
	void testEmptyGetContents()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.getCellContents("1AA"));
	}

	@Test
	void testGetEmptyContents()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			assertTrue("".equals(s.getCellContents("A2")));
		}
		catch (InvalidNameException e)
		{
			fail();
		}
	}

	// SETTING CELL TO A DOUBLE

	@Test
	void testSetNullDouble()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents(null, 1.5));
	}

	@Test
	void testSetInvalidNameDouble()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents("1A1A", 1.5));
	}

	@Test
	void testSimpleSetDouble()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("Z7", 1.5);
			assertEquals(1.5, s.getCellContents("Z7"));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail();
		}
	}

	// SETTING CELL TO A STRING
	@Test
	void testSetNullStringVal()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(NullPointerException.class, () -> s.setCellContents("A8", (String) null));
	}

	@Test
	void testSetNullStringName()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents(null, "hello"));
	}

	@Test
	void testSetSimpleString()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents("1AZ", "hello"));
	}

	@Test
	void testSetGetSimpleString()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("Z7", "hello");
			assertTrue("hello".equals(s.getCellContents("Z7")));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail();
		}
	}

	// SETTING CELL TO A FORMULA
	@Test
	void testSetNullFormVal()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(NullPointerException.class, () -> s.setCellContents("A8", (Formula) null));
	}

	@Test
	void testSetNullFormName()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents(null, new Formula("2")));
	}

	@Test
	void testSetSimpleForm()
	{
		Spreadsheet s = new Spreadsheet();
		assertThrows(InvalidNameException.class, () -> s.setCellContents("1AZ", new Formula("2")));
	}

	@Test
	void testSetGetForm()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("Z7", new Formula("3"));
			Formula f = (Formula) s.getCellContents("Z7");
			assertTrue(f.equals(new Formula("3")));
			assertFalse(f.equals(new Formula("2")));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail();
		}
	}

	@Test
	void testSimpleCircular()
	{
		Spreadsheet s = new Spreadsheet();

		try
		{
			s.setCellContents("A1", new Formula("A2"));

		}
		catch (InvalidNameException | CircularException e)
		{

			fail();

		}

		assertThrows(CircularException.class, () -> s.setCellContents("A2", new Formula("A1")));

	}

	@Test
	void testGetDirectDependents()
	{
		// taken from method description

		/**
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

		// set up
		Spreadsheet s = new Spreadsheet();

		try
		{
			s.setCellContents("A1", 3);
			s.setCellContents("B1", new Formula("A1 * A1"));
			s.setCellContents("C1", new Formula("B1 + A1"));
			s.setCellContents("D1", new Formula("B1 - C1"));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Exception encountered while adding elements to Spreadsheet s");
		}

		// test
		try
		{
			Iterable<String> dependentsOfA1 = s.getDirectDependents("A1");
			HashSet<String> expectedDependentsOfA1 = new HashSet<String>();
			expectedDependentsOfA1.add("B1");
			expectedDependentsOfA1.add("C1");
			assertTrue(iterableEqualToSet(dependentsOfA1, expectedDependentsOfA1));
		}
		catch (InvalidNameException e)
		{

			fail("Exception encountered while attempting to get the direct dependents of cell A1");
		}

	}

	/**
	 * Returns true if iterable contains the same number of elements as expectedSet
	 * and each element in iterable is contained by expectedSet else returns false.
	 */
	private boolean iterableEqualToSet(Iterable<String> iterable, HashSet<String> expectedSet)
	{
		int iterableSize = 0;
		for (String s : iterable)
		{
			iterableSize++;
			
			if (!expectedSet.contains(s))
				return false;
		}

		if (iterableSize != expectedSet.size())
			return false;

		return true;
	}
}
