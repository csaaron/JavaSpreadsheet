package spreadsheet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

public class Formula
{
	private ArrayList<String> tokens;

	private HashSet<String> variables;

	public Formula(String formula)
	{

	}

	public Formula(String formula, Normalizer normalize, IsValidFunctor isValid)
	{

	}

	private ArrayList<String> cleanAndValidate(Iterable<String> tokens, Normalizer normalize, IsValidFunctor isValid)
	{
		return null;
	}

	public Object evaluate(Lookup lookup)
	{
		return null;
	}

	private static void HandleDouble(double t, Stack<Double> values, Stack<String> operators)
	{

	}

	private static void ApplyOperatorStack(Stack<Double> values, Stack<String> operators)
	{

	}

	private static double ApplyOperator(double val1, double val2, String op)
	{
		return 0;
	}

	public Iterable<String> getVariables()
	{
		return null;
	}

	@Override
	public String toString()
	{
		return "";
	}

	@Override
	public boolean equals(Object obj)
	{
		return false;
	}

	@Override
	public int hashCode()
	{
		return 0;
	}

	private void verifySyntaxAndGetVariables(ArrayList<String> cleanedTokens)
	{

	}

	private void extraFollowRule(ArrayList<String> cleanedTokens, int index)
	{

	}

	private void parenthesesFollowRule(ArrayList<String> cleanedTokens, int index)
	{

	}

	private void endingTokenRule(ArrayList<String> cleanedTokens)
	{

	}

	private void startingTokenRule(ArrayList<String> cleanedTokens)
	{

	}

	private void oneTokenRule(ArrayList<String> cleanedTokens)
	{

	}

	private void balancedParenthesesRule(int parenthesesCount)
	{

	}

	private void rightParenthesesRule(int parenthesesCount)
	{

	}

	private static Iterable<String> getTokens(String formula)
	{
		return null;
	}

	private static boolean isValidToken(String token)
	{
		return true;
	}
	
	/**
	 * A normalizer class to use as the default normalizer.
	 */
	private class DoubleNormalize implements Normalizer
	{

		/**
		 * Takes in a string value, if it can be parsed to a double d, returns the
		 * Double.toString(d) else returns s unchanged.
		 */
		@Override
		public String normalize(String s)
		{
			try
			{
				return Double.valueOf(s).toString();
			}
			catch (NumberFormatException e)
			{
				return s;
			}
			catch (NullPointerException e)
			{
				return s;
			}
		}
	}

	/**
	 * Helpful static methods to use with standard library items for implementing the Formula class. 
	 */
	private static class ExtensionMethods
	{
		/**
		 * Returns true if string begins with a number
		 */
		public static boolean startsWithNumber(String s)
		{
			return (s.charAt(0) >= '0' && s.charAt(0) <= '9');
		}

		/**
		 * Returns true if string begins with a letter of the english language or an
		 * underscore.
		 */
		public static boolean startsWithLetterOrUnderscore(String s)
		{
			return ((s.charAt(0) >= 'a' && s.charAt(0) <= 'z') || (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z')
					|| (s.charAt(0) == '_'));
		}

		/**
		 * Returns true if s is an operator '*', '/', '+' or '-'
		 */
		public static boolean isOperator(String s)
		{
			return (s.equals('*') || s.equals('/') || s.equals("+") || s.equals("-"));
		}

		/**
		 * Takes a string s and returns true if a matching string is top of the stack,
		 * else returns false.
		 */
		public static boolean isAtTop(Stack<String> stack, String s)
		{
			return (stack.size() > 0 && stack.peek().equals(s));
		}

		/**
		 * Takes an inex and returns true if there is an item contained in this list
		 * after that index, else returns false.
		 */
		public static boolean hasNext(ArrayList<String> list, int index)
		{
			return (list.size() > (index + 1));
		}

	}

}
