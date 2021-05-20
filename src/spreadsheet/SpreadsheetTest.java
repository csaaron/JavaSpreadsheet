package spreadsheet;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
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
	void testComplexCircular()
	{

		Spreadsheet s = new Spreadsheet();

		try
		{
			s.setCellContents("A1", new Formula("A2+A3"));
			s.setCellContents("A3", new Formula("A4+A5"));
			s.setCellContents("A5", new Formula("A6+A7"));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail();
		}

		assertThrows(CircularException.class, () -> s.setCellContents("A7", new Formula("A1+A1")));
	}

	@Test
	void testUndoCircular()
	{
		Spreadsheet s = new Spreadsheet();

		try
		{
			s.setCellContents("A1", new Formula("A2+A3"));
			s.setCellContents("A2", 15);
			s.setCellContents("A3", 30);
			s.setCellContents("A2", new Formula("A3*A1"));
		}
		catch (InvalidNameException e)
		{
			fail();
		}
		catch (CircularException e)
		{
			try
			{
				assertEquals(15, (double) s.getCellContents("A2"));
				return;
			}
			catch (InvalidNameException e1)
			{
				fail();
			}
		}

		fail(); // if we never threw exception
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

	// NONEMPTY CELLS
	@Test
	void testEmptyNames()
	{
		Spreadsheet s = new Spreadsheet();
		assertFalse(s.getNamesOfAllNonemptyCells().iterator().hasNext());
	}

	@Test
	void testExplicitEmptySet()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("B1", "");
		}
		catch (InvalidNameException | CircularException e)
		{
			fail();
		}
		assertFalse(s.getNamesOfAllNonemptyCells().iterator().hasNext());
	}

	@Test
	void testSimpleNameString()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("B1", "hello");
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}

		HashSet<String> expectedSet = new HashSet<String>();
		expectedSet.add("B1");
		assertTrue(iterableEqualToSet(s.getNamesOfAllNonemptyCells(), expectedSet));
	}

	@Test
	void testSimpleNameDouble()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("B1", 52.25);
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}

		HashSet<String> expectedSet = new HashSet<String>();
		expectedSet.add("B1");
		assertTrue(iterableEqualToSet(s.getNamesOfAllNonemptyCells(), expectedSet));
	}

	@Test
	void testSimpleNameFormula()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("B1", new Formula("3.5"));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}

		HashSet<String> expectedSet = new HashSet<String>();
		expectedSet.add("B1");
		assertTrue(iterableEqualToSet(s.getNamesOfAllNonemptyCells(), expectedSet));
	}

	@Test
	void TestMixedNames()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", 17.2);
			s.setCellContents("C1", "hello");
			s.setCellContents("B1", new Formula("3.5"));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}

		HashSet<String> expectedSet = new HashSet<String>();
		expectedSet.add("A1");
		expectedSet.add("B1");
		expectedSet.add("C1");
		assertTrue(iterableEqualToSet(s.getNamesOfAllNonemptyCells(), expectedSet));
	}

	// RETURN VALUE OF SET CELL CONTENTS
	@Test
	void TestSetSingletonDouble()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("B1", "hello");
			s.setCellContents("C1", new Formula("5"));
			HashSet<String> expectedSet = new HashSet<String>();
			expectedSet.add("A1");
			assertTrue(iterableEqualToSet(s.setCellContents("A1", 17.2), expectedSet));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}

	@Test
	void TestSetSingletonString()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", 17.2);
			s.setCellContents("C1", new Formula("5"));
			HashSet<String> expectedSet = new HashSet<String>();
			expectedSet.add("B1");
			assertTrue(iterableEqualToSet(s.setCellContents("B1", "hello"), expectedSet));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}

	@Test
	void TestSetSingletonFormula()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", 17.1);
			s.setCellContents("B1", "hello");
			HashSet<String> expectedSet = new HashSet<String>();
			expectedSet.add("C1");
			assertTrue(iterableEqualToSet(s.setCellContents("C1", new Formula("5")), expectedSet));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}

	@Test
	void testSetChain()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", new Formula("A2+A3"));
			s.setCellContents("A2", 6);
			s.setCellContents("A3", new Formula("A2+A4"));
			s.setCellContents("A4", new Formula("A2+A5"));

			HashSet<String> expectedSet = new HashSet<String>();
			expectedSet.add("A5");
			expectedSet.add("A4");
			expectedSet.add("A3");
			expectedSet.add("A1");

			assertTrue(iterableEqualToSet(s.setCellContents("A5", 82.5), expectedSet));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}

	// CHANGING CELLS
	@Test
	void testChangeFtoD()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", new Formula("A2+A3"));
			s.setCellContents("A1", 2.5);
			assertEquals(2.5, (double) s.getCellContents("A1"));

		}
		catch (InvalidNameException | CircularException e)
		{
			fail("threw exception");
		}
	}

	@Test
	void testChangeFtoS()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", new Formula("A2+A3"));
			s.setCellContents("A1", "hello");
			assertEquals("hello", (String) s.getCellContents("A1"));

		}
		catch (InvalidNameException | CircularException e)
		{
			fail("threw exception");
		}
	}

	@Test
	void testChangeStoF()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			s.setCellContents("A1", "hello");
			s.setCellContents("A1", new Formula("23"));
			assertEquals(new Formula("23"), (Formula) s.getCellContents("A1"));
			assertNotEquals(new Formula("24"), (Formula) s.getCellContents("A1"));

		}
		catch (InvalidNameException | CircularException e)
		{
			fail("threw exception");
		}
	}

	// STRESS TESTS
	@Test
	void testStress1()
	{
		Spreadsheet s = new Spreadsheet();

		try
		{
			s.setCellContents("A1", new Formula("B1+B2"));
			s.setCellContents("B1", new Formula("C1-C2"));
			s.setCellContents("B2", new Formula("C3*C4"));
			s.setCellContents("C1", new Formula("D1*D2"));
			s.setCellContents("C2", new Formula("D3*D4"));
			s.setCellContents("C3", new Formula("D5*D6"));
			s.setCellContents("C4", new Formula("D7*D8"));
			s.setCellContents("D1", new Formula("E1"));
			s.setCellContents("D2", new Formula("E1"));
			s.setCellContents("D3", new Formula("E1"));
			s.setCellContents("D4", new Formula("E1"));
			s.setCellContents("D5", new Formula("E1"));
			s.setCellContents("D6", new Formula("E1"));
			s.setCellContents("D7", new Formula("E1"));
			s.setCellContents("D8", new Formula("E1"));
			Set<String> cells = s.setCellContents("E1", 0);

			HashSet<String> expectedSet = new HashSet<String>();
			expectedSet.add("A1");
			expectedSet.add("B1");
			expectedSet.add("B2");
			expectedSet.add("C1");
			expectedSet.add("C2");
			expectedSet.add("C3");
			expectedSet.add("C4");
			expectedSet.add("D1");
			expectedSet.add("D2");
			expectedSet.add("D3");
			expectedSet.add("D4");
			expectedSet.add("D5");
			expectedSet.add("D6");
			expectedSet.add("D7");
			expectedSet.add("D8");
			expectedSet.add("E1");

			assertTrue(iterableEqualToSet(cells, expectedSet));

		}
		catch (InvalidNameException | CircularException e)
		{
			fail("threw exception");
		}
	}

	@Test
	void testStress2()
	{
		Spreadsheet s = new Spreadsheet();
		HashSet<String> cells = new HashSet<String>();
		try
		{
			for (int i = 1; i < 200; i++)
			{
				cells.add("A" + i);
				assertTrue(iterableEqualToSet(s.setCellContents("A" + i, new Formula("A" + (i + 1))), cells));
			}

		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}

	@Test
	void testStress3()
	{
		Spreadsheet s = new Spreadsheet();

		try
		{
			for (int i = 1; i < 200; i++)
			{
				s.setCellContents("A" + i, new Formula("A" + (i + 1)));
			}
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw unexpected exception while loading spreadsheet");
		}
		
		assertThrows(CircularException.class, () -> s.setCellContents("A150", new Formula("A50")));
	}
	
	@Test
	void testStress4()
	{
		Spreadsheet s = new Spreadsheet();
		try
		{
			for (int i = 0; i < 500; i++)
			{
				s.setCellContents("A1" + i, new Formula("A1" + (i + 1)));
			}
			
			HashSet<String> firstCells = new HashSet<String>();
			HashSet<String> lastCells = new HashSet<String>();
			for (int i = 0; i < 250; i++)
			{
				firstCells.add("A1" + i);
				lastCells.add("A1" + (i + 250));
			}
			
			assertTrue(iterableEqualToSet(s.setCellContents("A1249", 25.0), firstCells));
			assertTrue(iterableEqualToSet(s.setCellContents("A1499", 0.0), lastCells));
		}
		catch (InvalidNameException | CircularException e)
		{
			fail("Threw exception");
		}
	}
	
	@Test
	void testStress5()
	{
		runRandomizedTest(47, 2526);
	}
	
	@Test
	void testStress6()
	{
		runRandomizedTest(48, 2527);
	}
	
	@Test
	void testStress7()
	{
		runRandomizedTest(49, 2517);
	}
	
	@Test
	void testStress8()
	{
		runRandomizedTest(50, 2516);
	}
	
	/**
	 * Sets random contents for a random cell 10000 times
	 */
	private void runRandomizedTest(int seed, int size)
	{
		Spreadsheet s = new Spreadsheet();
		Random rand = new Random(seed);
		
		try
		{
			for (int i = 0; i < 10000; i++)
			{
				switch (rand.nextInt(3))
				{
					case 0:
						s.setCellContents(randomName(rand), 3.14);
						break;
					case 1:
						s.setCellContents(randomName(rand), "hello");
						break;
					case 2:
						s.setCellContents(randomName(rand), randomFomula(rand));
						break;
				}
			}
		}
		catch (InvalidNameException | CircularException e)
		{
		}
		
		Set<String> set = new HashSet<String>();
		for (String cell : s.getNamesOfAllNonemptyCells())
		{
			set.add(cell);
		}
		
		assertEquals(size, set.size());
	}

	/**
	 * Generates a random cell name with a capital letter and a number between 1-99
	 */
	private String randomName(Random rand)
	{
		String letter = String.valueOf("ABCDEFGHIJKLMNOPQRSTUVWXYZ".charAt(rand.nextInt(26)));
		return letter + (rand.nextInt(99) +1);
	}
	
	/**
	 * Generates a random formula
	 */
	private String randomFomula(Random rand)
	{
		String f = randomName(rand);
		for(int i = 0; i < 10; i++)
		{
			switch(rand.nextInt(4))
			{
				case 0:
					f += "+";
					break;
				case 1:
					f += "-";
					break;
				case 2:
					f += "*";
					break;
				case 3:
					f += "/";
					break;
			}
			
			switch(rand.nextInt(2))
			{
				case 0:
					f += 7.2;
					break;
				case 1:
					f += randomName(rand);
					break;
			}
		}
		return f;
	}

	
	

}
