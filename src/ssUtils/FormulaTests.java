package ssUtils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class FormulaTests
{

	@Test
	void basicExpressionEvaluate()
	{
		String expression = "(2 + 3) * 5 + 2";
		Formula formula = new Formula(expression);
		double value = 27;
		assertEquals(value, (Double)formula.evaluate(x -> 0));
	}
	
	@Test
	void basicExpressionEvaluateVariable()
	{
		String expression = "(2 + A6) * 5 + 2";
		double expected = 47;
		Formula formula = new Formula(expression);
		double actual = (Double)formula.evaluate(x -> 7.0);
		assertEquals(expected, actual);
	}
	
	// do some basic 2^n calculations
	@Test
	void twoRased4()
	{
		// create a string which is 2^n power using multiplication
		String expression = "2*2*2*2";
		
		double expected =16;
		Formula formula = new Formula(expression);
		double actual = (Double)formula.evaluate(x -> 0);
		
		assertEquals(expected, actual);
	}

	@Test
	void twoRaisedN()
	{
		//create a string which is 2^n power using multiplication
		StringBuilder expression = new StringBuilder();
		int n = 30;
		for(int i = 1; i < n; i++)
		{
			expression.append("2*");
		}
		expression.append("2");
		
		double expected = Math.pow(2, n);
		Formula formula = new Formula(expression.toString());
		double actual = (Double)formula.evaluate(x -> 0);
		
		assertEquals(expected, actual);	
	}
	
	@Test
	void twoRaisedNTonOfWhitespace()
	{
		//create a string which is 2^n power using multiplication
		StringBuilder expression = new StringBuilder();
		int n = 30;
		for(int i = 1; i < n; i++)
		{
			expression.append("2\t\t\n  * \t\t\n     \t\n");
		}
		expression.append("2");
		
		double expected = Math.pow(2, n);
		Formula formula = new Formula(expression.toString());
		double actual = (Double)formula.evaluate(x -> 0);
		
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
		String[] variables = {"A1", "B2", "AaBbCc1234", "alskdjfalsdkjf1", "TheAnswerToLifeTheUniverseAndEverythingIs42", "pI3141592654", "z129384701923874109"};
		// an array with several values to look up
		double[] values = {0, 10, 1234, 2 * 15, 42, 314159265, -18, 300, 254, 122584};
		
		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be valid
			Formula formula = new Formula(variables[i]);
			double value = values[i];
			// each variable name should evaluate to the correct value when looked up.
			assertEquals(values[i], (Double)formula.evaluate(x -> value));
		}
	}
	
	@Test
	void validVariableNamesLookupTimesTwo()
	{
		// an array with several valid variable names
		String[] variables = {"A1", "B2", "AaBbCc1234", "alskdjfalsdkjf1", "TheAnswerToLifeTheUniverseAndEverythingIs42", "pI3141592654", "z129384701923874109"};
		// an array with several values to look up
		double[] values = {0, 10, 1234, 2 * 15, 42, 314159265, -18, 300, 254, 122584};

		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be valid
			Formula formula = new Formula(variables[i]);
			double value = values[i];
			// each variable name should evaluate to the correct value when looked up.
			assertEquals(values[i] * 2, (Double)formula.evaluate(x -> value + value));
		}
	}
	
	@Test
	void invalidVariableNamesLookup()
	{
		// an array with several invalid variable names
		String[] variables = {"12A", "1b 2b", "1B", "%AaBbCc", "alsk$djfalsdkjf", "TheAnswerToLifeThe\nUniverseAndEverythingIs42", "p I3141592654", "129384701923874109asdfSDFss"};

		for (int i = 0; i < variables.length; i++)
		{
			// each variable name should be invalid
			String variable = variables[i];
			assertThrows(FormulaFormatException.class, () -> new Formula(variable));
			
		}
	}
	
	@Test
	void multiplicationAndPlusPrecidence()
	{
		// test standard order of operations
        // multiplication should be done first
        double expected = 15;
        String expression = "4*3+3";
        Formula formula = new Formula(expression);
        assertEquals(expected, (Double)formula.evaluate(x -> 0));

        // test standard order of operations
        // multiplication should be done first
        expected = 13;
        expression = "4+3*3";
        formula = new Formula(expression);
        assertEquals(expected, (Double)formula.evaluate(x -> 0));

        // the two previous expressions should not be equal when evaluated
        Formula formula2 = new Formula("4*3+ 3");
        assertEquals(formula2.evaluate(x -> 0), (Double)formula.evaluate(x -> 0));

        // force addition to be evaluated first using parenthesis
        expected = 21;
        expression = "(4+3)*3";
        formula = new Formula(expression);
        assertEquals(expected, (Double)formula.evaluate(x -> 0));

        // force addition to be evaluated first again using parenthesis
        expected = 24;
        expression = "4*(3+3)";
        formula = new Formula(expression);
        assertEquals(expected, (Double)formula.evaluate(x -> 0));
	}
	
	@Test
	void intDivisionSumTest()
	{
		// integer division at each step should result in 0, 
        // as opposed to truncation at the end which would lead to 1
        String expression = "1/3 + 1/3 + 1/3 + 1/3";
        Formula formula = new Formula(expression);
        double expected = 1.0 / 3.0 + 1.0 / 3.0 + 1.0 / 3.0 + 1.0 / 3.0;

        assertEquals(expected, (Double)formula.evaluate(x -> 0));

        // integer division should result in 2
        expression = "5/3 + 5/3 ";
        formula = new Formula(expression);
        expected = 5.0 / 3.0 + 5.0 / 3.0;

        assertEquals(expected, (Double)formula.evaluate(x -> 0));
	}
	
	@Test
	void divideBySelf()
	{
		// anything divided by itself should be 1
        String expression = "b3/b3 ";
        double expected = 1;
        Formula formula = new Formula(expression);

        for(int i = 1; i < 10; i++)
        {
            double n = Math.pow(2, i);
            assertEquals(expected, (Double)formula.evaluate(x -> n));
        }

	}
	
	@Test
	void zeroDivideSomething()
	{
		// zero divided anything should be 0
        String expression = "0/b3 ";
        double expected = 0;
        Formula formula = new Formula(expression);

        for(int i = 1; i < 10; i++)
        {
            double n = Math.pow(2, i);
            assertEquals(expected, (Double)formula.evaluate(x -> n));
        }
	}
	
	@Test
	void divideByOne()
	{
		// something divided by 1 should equal that something
        String expression = "_z58/1 ";
        Formula formula = new Formula(expression);

        for(int i = 1; i < 10; i++)
        {
            double n = Math.pow(2, i);
            assertEquals(n, (Double)formula.evaluate(x -> n));
        }
	}
	
	@Test
	void multiplyAndDivideLtoR()
	{
		// multiplication and divide should be applied left to right
        String expression = "14 / 7 * 4 / 2  ";
        double expected = 4;
        Formula formula = new Formula(expression);

        assertEquals(expected, (Double)formula.evaluate(x -> 0));
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

        for(int i = 1; i < 100; i = i + 2)
        {
            int lookupVal=i;
        	assertEquals(formula.evaluate(x -> lookupVal), formula2.evaluate(x -> lookupVal));
        }
	}
	
	@Test
	void divideByZero()
	{
		//Cannot divide by zero, should return FormulaError
        String expression = "14 / 0";
        Formula formula = new Formula(expression);

        Object o = formula.evaluate(x -> 0);
        if (!(o instanceof FormulaError))
        {
        	fail();
        }



        expression = "b1 / (b1 - b1)";
        formula = new Formula(expression);
        for(int i = 1; i < 25; i++)
        {
            int lookupValue = i;
        	o = formula.evaluate(x -> lookupValue);
            
            if(!(o instanceof FormulaError))
            {
            	fail();
            }
        }
	}
	
	
	
	
}
