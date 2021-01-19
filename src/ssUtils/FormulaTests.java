package ssUtils;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

class FormulaTests
{

	@Test
	void basicExpressionEvaluate()
	{
		String expression = "(2 + 3) * 5 + 2";
		Formula formula = new Formula(expression);
		double value = 27;
		assertEquals(value, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void basicExpressionEvaluateVariable()
	{
		String expression = "(2 + A6) * 5 + 2";
		double expected = 47;
		Formula formula = new Formula(expression);
		double actual = (Double) formula.evaluate(x -> 7.0);
		assertEquals(expected, actual);
	}

	// do some basic 2^n calculations
	@Test
	void twoRased4()
	{
		// create a String which is 2^n power using multiplication
		String expression = "2*2*2*2";

		double expected = 16;
		Formula formula = new Formula(expression);
		double actual = (Double) formula.evaluate(x -> 0);

		assertEquals(expected, actual);
	}

	@Test
	void twoRaisedN()
	{
		// create a String which is 2^n power using multiplication
		StringBuilder expression = new StringBuilder();
		int n = 30;
		for (int i = 1; i < n; i++)
		{
			expression.append("2*");
		}
		expression.append("2");

		double expected = Math.pow(2, n);
		Formula formula = new Formula(expression.toString());
		double actual = (Double) formula.evaluate(x -> 0);

		assertEquals(expected, actual);
	}

	@Test
	void twoRaisedNTonOfWhitespace()
	{
		// create a String which is 2^n power using multiplication
		StringBuilder expression = new StringBuilder();
		int n = 30;
		for (int i = 1; i < n; i++)
		{
			expression.append("2\t\t\n  * \t\t\n     \t\n");
		}
		expression.append("2");

		double expected = Math.pow(2, n);
		Formula formula = new Formula(expression.toString());
		double actual = (Double) formula.evaluate(x -> 0);

		assertEquals(expected, actual);
	}

	@Test
	void InvalidTokenCarrot()
	{
		String expression = "2+3^2";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void InvalidTokenDollar()
	{
		String expression = "2+3+2$";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void InvalidTokenComplicatedExpression()
	{
		String expression = "2+3) * 2 \n\n\t   /4 %#@@!";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void InvalidTokenInVariableName()
	{
		String expression = "(2+BB3?7) * 2 \n\n\t   /4";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void validVariableNames()
	{
		// an array with several valid variable names
		String[] variables =
		{ "A1", "B2", "AaBbCc1234", "alskdjfalsdkjf1", "TheAnswerToLifeTheUniverseAndEverythingIs42", "pI3141592654",
				"z129384701923874109" };
		// an array with several values to look up
		double[] values =
		{ 0, 10, 1234, 2 * 15, 42, 314159265, -18, 300, 254, 122584 };

		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be valid
			Formula formula = new Formula(variables[i]);
			double value = values[i];
			// each variable name should evaluate to the correct value when looked up.
			assertEquals(values[i], (Double) formula.evaluate(x -> value));
		}
	}

	@Test
	void validVariableNamesLookupTimesTwo()
	{
		// an array with several valid variable names
		String[] variables =
		{ "A1", "B2", "AaBbCc1234", "alskdjfalsdkjf1", "TheAnswerToLifeTheUniverseAndEverythingIs42", "pI3141592654",
				"z129384701923874109" };
		// an array with several values to look up
		double[] values =
		{ 0, 10, 1234, 2 * 15, 42, 314159265, -18, 300, 254, 122584 };

		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be valid
			Formula formula = new Formula(variables[i]);
			double value = values[i];
			// each variable name should evaluate to the correct value when looked up.
			assertEquals(values[i] * 2, (Double) formula.evaluate(x -> value + value));
		}
	}

	@Test
	void invalidVariableNamesLookup()
	{
		// an array with several invalid variable names
		String[] variables =
		{ "12A", "1b 2b", "1B", "%AaBbCc", "alsk$djfalsdkjf", "TheAnswerToLifeThe\nUniverseAndEverythingIs42",
				"p I3141592654", "129384701923874109asdfSDFss" };

		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be invalid
			String variable = variables[i];
			assertThrows(FormulaFormatException.class, () -> new Formula(variable));

		}
	}

	@Test
	void invalidVariableNamesLookupTroubleshoot()
	{
		// an array with several invalid variable names
//		String[] variables =
//		{ "12A", "1b 2b", "1B", "%AaBbCc", "alsk$djfalsdkjf", "TheAnswerToLifeThe\nUniverseAndEverythingIs42",
//				"p I3141592654", "129384701923874109asdfSDFss" };

		String invalidVariable = "%AaBbCc";

//		for (int i = 0; i < variables.length; i++)
//		{
		// each variable name should be invalid
		String variable = invalidVariable;
		assertThrows(FormulaFormatException.class, () -> new Formula(variable));

//		}
	}

	@Test
	void multiplicationAndPlusPrecidence()
	{
		// test standard order of operations
		// multiplication should be done first
		double expected = 15;
		String expression = "4*3+3";
		Formula formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// test standard order of operations
		// multiplication should be done first
		expected = 13;
		expression = "4+3*3";
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// the two previous expressions should not be equal when evaluated
		Formula formula2 = new Formula("4*3+ 3");
		assertNotEquals(formula2.evaluate(x -> 0), (Double) formula.evaluate(x -> 0));
		
		// force addition to be evaluated first using parenthesis
		expected = 21;
		expression = "(4+3)*3";
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// force addition to be evaluated first again using parenthesis
		expected = 24;
		expression = "4*(3+3)";
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void intDivisionSumTest()
	{
		// integer division at each step should result in 0,
		// as opposed to truncation at the end which would lead to 1
		String expression = "1/3 + 1/3 + 1/3 + 1/3";
		Formula formula = new Formula(expression);
		double expected = 1.0 / 3.0 + 1.0 / 3.0 + 1.0 / 3.0 + 1.0 / 3.0;

		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// integer division should result in 2
		expression = "5/3 + 5/3 ";
		formula = new Formula(expression);
		expected = 5.0 / 3.0 + 5.0 / 3.0;

		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void divideBySelf()
	{
		// anything divided by itself should be 1
		String expression = "b3/b3 ";
		double expected = 1;
		Formula formula = new Formula(expression);

		for (int i = 1; i < 10; i++)
		{
			double n = Math.pow(2, i);
			assertEquals(expected, (Double) formula.evaluate(x -> n));
		}

	}

	@Test
	void zeroDivideSomething()
	{
		// zero divided anything should be 0
		String expression = "0/b3 ";
		double expected = 0;
		Formula formula = new Formula(expression);

		for (int i = 1; i < 10; i++)
		{
			double n = Math.pow(2, i);
			assertEquals(expected, (Double) formula.evaluate(x -> n));
		}
	}

	@Test
	void divideByOne()
	{
		// something divided by 1 should equal that something
		String expression = "_z58/1 ";
		Formula formula = new Formula(expression);

		for (int i = 1; i < 10; i++)
		{
			double n = Math.pow(2, i);
			assertEquals(n, (Double) formula.evaluate(x -> n));
		}
	}

	@Test
	void multiplyAndDivideLtoR()
	{
		// multiplication and divide should be applied left to right
		String expression = "14 / 7 * 4 / 2  ";
		double expected = 4;
		Formula formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void fractionDividedByNumber()
	{
		// a fraction divided by a number is the same as a fraction multiplied
		// by the reciprocal of that number
		String expression = "14 / 7 / b1";
		Formula formula = new Formula(expression);

		String equivalent = "14 / 7 * 1 / b1";
		Formula formula2 = new Formula(equivalent);

		for (int i = 1; i < 100; i = i + 2)
		{
			int lookupVal = i;
			assertEquals(formula.evaluate(x -> lookupVal), formula2.evaluate(x -> lookupVal));
		}
	}

	@Test
	void divideByZero()
	{
		// Cannot divide by zero, should return FormulaError
		String expression = "14 / 0";
		Formula formula = new Formula(expression);

		Object o = formula.evaluate(x -> 0);
		if (!(o instanceof FormulaError))
		{
			fail();
		}

		expression = "b1 / (b1 - b1)";
		formula = new Formula(expression);
		for (int i = 1; i < 25; i++)
		{
			int lookupValue = i;
			o = formula.evaluate(x -> lookupValue);

			if (!(o instanceof FormulaError))
			{
				fail();
			}
		}
	}

	@Test
	void divideByNegative()
	{
		// a number divided by something negative should equal a negative number
		String expression = "14 / (0 - 2)";
		double expected = -7;
		Formula formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void multiplyByZero()
	{
		// a number multiplied by zero equals zero
		String expression = "18 * 0";
		double expected = 0;
		Formula formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// test it with another operator
		expression = "_b_bs_d45 * 99 + 3";
		expected = 3;
		formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		// zero should follow the multiplication
		expression = "bbsd45 * 2 * 4 * 8 * 16 * 32 * 64 * 128";
		expected = 0;
		formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void multiplyByOne()
	{
		// something multiplied by 1 equals that something
		String expression = "a4 * 1";
		Formula formula = new Formula(expression);

		for (int i = 1; i < 10; i++)
		{
			double n = Math.pow(2, i);
			assertEquals(n, (Double) formula.evaluate(x -> n));
		}
	}

	@Test
	void addToZero()
	{
		// something added to 0 equals that something
		String expression = "a4 + 0 ";
		Formula formula = new Formula(expression);

		for (int i = 1; i < 10; i++)
		{
			double n = Math.pow(2, i);
			assertEquals(n, (Double) formula.evaluate(x -> n));
		}
	}

	void divisionPrecidence()
	{
		String expression = "4 / 3 + 3";
		double expected = 4.0 / 3.0 + 3.0;
		Formula formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		expression = "4 + 3 / 3";
		expected = 4.0 + 3.0 / 3.0;
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		expression = "(4 + 3) / 3";
		expected = (4.0 + 3.0) / 3.0;
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		expression = "4 / (3 + 3)";
		expected = 4.0 / (3.0 + 3.0);
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));

		expression = "4 + 3 *2 / 3";
		expected = 4.0 + 3.0 * 2.0 / 3.0;
		formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 0));
	}

	@Test
	void tooManyOperatorsAtEnd()
	{
		// extra operator at end
		String expression = "14 / 7 * 4 / 2  +";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void tooManyOperatorsAtStart()
	{
		String expression = " * 14 / 7 * 4 / 2 ";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void tooManyOperatorsInMiddle()
	{
		String expression = "14 / 7 * 4 * / 2 ";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void noOpperators()
	{
		// no operators
		String expression = "14  7";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void tryToPassNegativeInt()
	{
		String expression = "14 / -7 * 4  / 2 ";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void mismatchedParenthesis()
	{
		{
			String expression = "(14 / 7)) * 4 / 2 + 5";

			assertThrows(FormulaFormatException.class, () -> new Formula(expression));
		}
		{
			String expression = "((14 / 7) * 4 / 2 ";

			assertThrows(FormulaFormatException.class, () -> new Formula(expression));
		}
		{
			String expression = "(14 / 7) * 4 / 2 ) ";

			assertThrows(FormulaFormatException.class, () -> new Formula(expression));
		}
		{
			String expression = "((14 / 7 * 4 / 2 ";

			assertThrows(FormulaFormatException.class, () -> new Formula(expression));
		}
		{
			String expression = "14 / 7 * 4 / 2 ))";

			assertThrows(FormulaFormatException.class, () -> new Formula(expression));
		}
	}

	@Test
	void tryImplicitMultiplication()
	{
		String expression = "2(14+5)";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void tryPassingNullString()
	{
		String expression = null;
		assertThrows(NullPointerException.class, () -> new Formula(expression));
	}

	@Test
	void tryPassingEmptyString()
	{
		String expression = "";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	public void nestedParenthesis()
	{
		String expression = "(5 + ((((b4)))))";
		double expected = 10;
		Formula formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> 5));
	}

	@Test
	void emptyParenthesis()
	{
		String expression = "()";
		assertThrows(FormulaFormatException.class, () -> new Formula(expression));
	}

	@Test
	void everyOperandAndVariable()
	{
		String expression = "(2 * b5) - 4 + 7 / 4";
		double expected = 7.75;
		Formula formula = new Formula(expression);

		assertEquals(expected, (Double) formula.evaluate(x -> 5));
	}

	@Test
	void everyOperandAndVariableCrazyWWhiteSpace()
	{
		String expression = "(2.0 + b5 * 3) - (4 + 7 / 4 -3+8-1* \t\t\t\n  zd56) / (b5)";
		double expected = 5.125;
		Formula formula = new Formula(expression);
		assertEquals(expected, (Double) formula.evaluate(x -> "b5".equals(x) ? 2 : 5));
	}

	@Test
	void normalizerValidatorConstructor()
	{
		String expression = "_b5 + 2";
		double expected = 7.0;
		// create object with multi-argument constructor
		Formula formula = new Formula(expression, x -> x.toUpperCase(), s -> s == s.toUpperCase());
		assertEquals(expected, (Double) formula.evaluate(x -> 5));
	}

	@Test
	void badNormalizerValidatorConstructor()
	{
		String expression = "_b5 + 2";
		// double expected = 7.0;
		// create object with multi-argument constructor
		// there can never be valid variables in this, we should expect an error
		assertThrows(FormulaFormatException.class,
				() -> new Formula(expression, x -> x.toUpperCase(), s -> s.equals(s.toLowerCase())));
	}

	/**
	 * This functor only acts to throw a RuntimeException. Used to check that
	 * evaluate functions handle all exceptions properly IE changes the exception
	 * into a FormatError
	 */
	private class LookupException implements Lookup
	{

		@Override
		public double lookup(String variable)
		{
			throw new RuntimeException();
		}

	}

	@Test
	void lookupThrowsException()
	{
		String expression = "_b5 + 2";
		// double expected = 7.0;
		// create object with multi-argument constructor

		Formula formula = new Formula(expression, x -> x.toUpperCase(), s -> true);
		Lookup lookup = new LookupException();
		Object o;
		try
		{
			o = formula.evaluate(lookup);
			assertTrue(o instanceof FormulaError);
		}
		catch (RuntimeException e)
		{
			fail("Exception was not handled by Formula.evaluate()");
		}
	}

	@Test
	void getVariableDoubles()
	{
		String expression = "_b5 + c17 + _b5";
		double expected = 15.0;
		// create object with multi-argument constructor
		Formula formula = new Formula(expression, x -> x.toUpperCase(), s -> s == s.toUpperCase());

		HashSet<String> expectedVariables = new HashSet<String>();
		expectedVariables.add("_B5");
		expectedVariables.add("C17");

		Iterable<String> variables = formula.getVariables();
		int count = 0;
		for (String s : variables)
		{
			assertTrue(expectedVariables.contains(s));
			count++;
		}

		assertEquals(expectedVariables.size(), count);

		assertEquals(expected, formula.evaluate(x -> 5));
	}

	@Test
	void getVariablesNoVariables()
	{
		String expression = "5+5+5";
		double expected = 15.0;
		// create object with multi-argument constructor
		Formula formula = new Formula(expression, x -> x.toUpperCase(), s -> s == s.toUpperCase());
		Iterable<String> variables = formula.getVariables();

		int count = 0;
		for (String s : variables)
		{
			count++;
		}

		assertEquals(0, count);
		assertEquals(expected, formula.evaluate(x -> 5));
	}

	@Test
	void toStringCheck()
	{
		String expression = "5 + 5 + 5";

		Formula formula = new Formula(expression);
		String actual = formula.toString();
		String expected = "5.0+5.0+5.0";

		assertEquals(expected, actual);

		expression = "b3 + \n\t\t25";
		formula = new Formula(expression, x -> x.toUpperCase(), s -> s == s.toUpperCase());
		actual = formula.toString();
		expected = "B3+25.0";

		assertEquals(expected, actual);
	}

	@Test
	void equalsCheck()
	{
		String expression1 = "5 + 5 + \t\t5";
		String expression2 = "5 + 5 + 5";
		String expression3 = "5 + 5 + 5.0";
		// create object with multi-argument constructor
		Formula formula1 = new Formula(expression1);
		Formula formula2 = new Formula(expression2);
		Formula formula3 = new Formula(expression3);

		assertTrue(formula1.equals(formula2));
		assertTrue(formula2.equals(formula1));
		assertTrue(formula2.equals(formula3));

		assertFalse(formula2.equals("5+5+5"));
		assertFalse(formula1.equals(null));

		String expression4 = "5 + 5";
		Formula formula4 = new Formula(expression4);
		assertFalse(formula1.equals(formula4));

	}

	@Test
	void hashcodeCheck()
	{
		String expression1 = "5 + 5 + \t\t5";
		String expression2 = "5 + 5 + 5";
		String expression3 = "5 + 5 + 5.0";
		// create object with multi-argument constructor
		Formula formula1 = new Formula(expression1);
		Formula formula2 = new Formula(expression2);
		Formula formula3 = new Formula(expression3);

		// System.out.println(formula1.toString());

		assertTrue(formula1.hashCode() == formula2.hashCode());
		assertTrue(formula2.hashCode() == formula1.hashCode());
		assertTrue(formula2.hashCode() == formula3.hashCode());
		assertTrue(formula2.hashCode() == "5.0+5.0+5.0".hashCode());

		String expression4 = "5 + 5";
		Formula formula4 = new Formula(expression4);
		assertFalse(formula1.hashCode() == formula4.hashCode());
		assertFalse(formula2.hashCode() == "5 + 5 + 5".hashCode());
	}

	@Test
	void isDoubleStringTest()
	{
		String double1 = "5";
		String double2 = "5.0";
		String double3 = "3.141592654";
		String whitespaceString = "  \t \n";
		String emptyString = "";
		String scientificDouble1 = "9.7E4";
		String scientificDouble2 = "9.7e4";

		String notDouble1 = "Hello World";
		String nullString = null;

		assertTrue(Formula.ExtensionMethods.isDoubleString(double1));
		assertTrue(Formula.ExtensionMethods.isDoubleString(double2));
		assertTrue(Formula.ExtensionMethods.isDoubleString(double3));
		assertTrue(Formula.ExtensionMethods.isDoubleString(scientificDouble1));
		assertTrue(Formula.ExtensionMethods.isDoubleString(scientificDouble2));

		assertFalse(Formula.ExtensionMethods.isDoubleString(notDouble1));
		assertFalse(Formula.ExtensionMethods.isDoubleString(nullString));
		assertFalse(Formula.ExtensionMethods.isDoubleString(whitespaceString));
		assertFalse(Formula.ExtensionMethods.isDoubleString(emptyString));
	}
}
