package ssUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents formulas written in standard infix notation using standard
 * precedence rules. The allowed symbols are non-negative numbers written using
 * double-precision floating-point syntax; variables that consist of a letter or
 * underscore followed by zero or more letters, underscores, or digits;
 * parentheses; and the four operator symbols +, -, *, and /.
 *
 * Spaces are significant only insofar that they delimit tokens. For example,
 * "xy" is a single variable, "x y" consists of two variables "x" and y; "x23"
 * is a single variable; and "x 23" consists of a variable "x" and a number
 * "23".
 *
 * Associated with every formula are two delegates: a normalizer and a
 * validator. The normalizer is used to convert variables into a canonical form,
 * and the validator is used to add extra restrictions on the validity of a
 * variable (beyond the standard requirement that it consist of a letter or
 * underscore followed by zero or more letters, underscores, or digits.) Their
 * use is described in detail in the constructor and method comments.
 */
public class Formula
{
    // A list which contains a tokenized version of this formula. Tokens contained
    // in this list will be valid and in an order that is syntactically correct.

    private ArrayList<String> tokens;

    // Contains the set of all variables contained in this formula. The variables
    // are stored in normalized form.
    private HashSet<String> variables;

    /**
     * Creates a Formula from a string that consists of an infix expression. If
     * the expression is syntactically invalid, throws a FormulaFormatException
     * with an explanatory Message.
     *
     * The associated normalizer is the identity function, and the associated
     * validator maps every string to true.
     */
    public Formula(String formula)
    {
        this(formula, n -> n, v -> true);
    }

    /**
     * Creates a Formula from a string that consists of an infix expression. If
     * the expression is syntactically incorrect, throws a
     * FormulaFormatException with an explanatory Message.
     *
     * The associated normalizer and validator are the second and third
     * parameters, respectively.
     *
     * If the formula contains a variable v such that Normalizer.normalize(v) is
     * not a legal variable, throws a FormulaFormatException with an explanatory
     * message.
     *
     * If the formula contains a variable v such that
     * IsValidFunctor.isValid(Normalizer.normalize(v)) is false, throws a
     * FormulaFormatException with an explanatory message.
     *
     * Suppose that N is a method that converts all the letters in a string to
     * upper case, and that V is a method that returns true only if a string
     * consists of one letter followed by one digit. Then:
     *
     * new Formula("x2+y3", N, V) should succeed new Formula("x+y3", N, V)
     * should throw an exception, since V(N("x")) is false new Formula("2x+y3",
     * N, V) should throw an exception, since "2x+y3" is syntactically
     * incorrect.
     */
    public Formula(String formula, Normalizer normalize, IsValid isValid)
    {

        variables = new HashSet<String>();

        // clean, validate and normalize all tokens.
        ArrayList<String> validCleanedTokens = cleanAndValidate(formula, normalize, isValid);

        for (String s : validCleanedTokens)
        {
            if (ExtensionMethods.startsWithLetterOrUnderscore(s))
            {
                variables.add(s);
            }
        }

        // verify correct syntax
        verifySyntax(validCleanedTokens);

        // store valid function
        tokens = validCleanedTokens;

    }

    /**
     * Takes a formula, normalizes each token using the provided normalize
     * functor and checks that each token is valid. If a token is not valid
     * throws a a FormulaFormatException with an explanatory message.
     *
     * Will return an ArrayList<String> of all valid tokens which are cleaned
     * and validated.
     */
    private ArrayList<String> cleanAndValidate(String formula, Normalizer normalize, IsValid isValid)
    {
        Iterable<String> tokens = getTokens(formula);
        String strippedFormula = stripWhiteSpace(formula);

        // verify nothing funky has happened with weird tokens
        if (!joinTokens(tokens).equals(strippedFormula))
        {
            String message = "Formula contains invalid characters";
            throw new FormulaFormatException(message);
        }

        ArrayList<String> cleanedAndValidated = new ArrayList<String>();
        Normalizer doubleNormalizer = new DoubleNormalize();
        for (String token : tokens)
        {
            // normalize the token
            String validating = normalize.normalize(token);
            // token could have been a double, if so parse it and move it to a standard form
            validating = doubleNormalizer.normalize(validating);

            // verify this is a valid token
            if (!isValidToken(validating))
            {
                String message = "Token, \"" + token + ",\" was invalid when normalized to \"" + validating + "\"";
                throw new FormulaFormatException(message);
            }

            // validate variable names
            if (ExtensionMethods.startsWithLetterOrUnderscore(validating) && !isValid.isValid(validating))
            {
                String message = "Variable, \"" + token + ",\" was invalid when normalized to \"" + validating + "\"";
                throw new FormulaFormatException(message);
            }

            cleanedAndValidated.add(validating);
        }

        return cleanedAndValidated;
    }

    /**
     * Evaluates this Formula, using the lookup functor to determine the values
     * of variables. When a variable symbol v needs to be determined, it will be
     * looked up via Lookup.lookup(Normalize.normalize(v)). (Here, normalize is
     * the normalizer that was passed to the constructor.)
     *
     * For example, if L("x") is 2, L("X") is 4, and N is a method that converts
     * all the letters in a string to upper case:
     *
     * new Formula("x+7", N, s => true).Evaluate(L) is 11 new
     * Formula("x+7").Evaluate(L) is 9
     *
     * Given a variable symbol as its parameter, lookup returns the variable's
     * value (if it has one) or throws an IllegalArgumentException (otherwise).
     *
     * If no undefined variables or divisions by zero are encountered when
     * evaluating this Formula, the value is returned. Otherwise, a FormulaError
     * is returned. The Reason property of the FormulaError should have a
     * meaningful explanation.
     *
     * This method should never throw an exception.
     */
    public Object evaluate(Lookup lookup)
    {
        Stack<Double> values = new Stack<Double>();
        Stack<String> operators = new Stack<String>();

        // this is the main body of the algorithm where the expression is evaluated
        for (String token : tokens)
        {
            Double operand = 0.0;
            if (ExtensionMethods.isDoubleString(token))
            {
                try
                {
                    operand = Double.valueOf(token);
                    handleDouble(operand, values, operators);
                }
                catch (Exception e)
                {
                    return new FormulaError(e.getMessage());
                }
            }
            // check if token is variable
            else if (ExtensionMethods.startsWithLetterOrUnderscore(token))
            {
                try
                {
                    operand = lookup.lookup(token);
                    handleDouble(operand, values, operators);
                }
                catch (Exception e)
                {
                    return new FormulaError(e.getMessage());
                }
            }
            else if (token.equals("+") || token.equals("-"))
            {
                // if plus or minus is at top of operator stack,
                // apply it to the top two operands on top of values stack
                if (ExtensionMethods.isAtTop(operators, "+") || ExtensionMethods.isAtTop(operators, "-"))
                {
                    applyOperatorStack(values, operators);
                }

                // push + or - to operators stack
                operators.push(token);
            }
            else if (token.equals("*") || token.equals("/"))
            {
                operators.push(token);
            }
            else if (token.equals("("))
            {
                operators.push(token);
            }
            else if (token.equals(")"))
            {
                // if plus or minus is at top of operator stack,
                // apply it to the top two operands on top of values stack
                if (ExtensionMethods.isAtTop(operators, "+") || ExtensionMethods.isAtTop(operators, "-"))
                {
                    applyOperatorStack(values, operators);
                }

                // now that + or - has been applied, next operator on stack
                // should be "("
                operators.pop();

                if (ExtensionMethods.isAtTop(operators, "*") || ExtensionMethods.isAtTop(operators, "/"))
                {
                    try
                    {
                        applyOperatorStack(values, operators);
                    }
                    catch (Exception e)
                    {
                        return new FormulaError(e.getMessage());
                    }
                }
            }

        }
        // the last token has been processed
        if (operators.size() == 0)
        {
            return values.pop();
        }
        else
        {
            // the only operator token should be '+' or '-'
            applyOperatorStack(values, operators);
            return values.pop();
        }

    }

    /**
     * If * or / is at the top of the operator stack, pops the value stack and
     * pops the operator stack. then applies the popped operator to the popped
     * number and t. Pushes the result onto the value stack. Otherwise, pushes t
     * onto the value stack.
     *
     * @param t - the double token
     * @param values - a stack containing values for the evaluate method
     * @param operators - a stack containing operators for the evaluate method
     */
    private static void handleDouble(double t, Stack<Double> values, Stack<String> operators)
    {
        // operand is a double, check if operator * or / is at top of stack and apply it
        if (!operators.empty() && (operators.peek().equals("*") || operators.peek().equals("/")))
        {
            double result = applyOperator(values.pop(), t, operators.pop());
            values.push(result);
        }
        // * or / is not at top of operator stack, push operand to top of stack
        else
        {
            values.push(t);
        }
    }

    /**
     * Pops the value stack twice and the operator stack once, then applies the
     * popped operator to the popped values, then pushes the result onto the
     * value stack.
     *
     * @param values - a stack containing values for the evaluate method
     * @param operators - a stack containing operators for the evaluate method
     */
    private static void applyOperatorStack(Stack<Double> values, Stack<String> operators)
    {
        // pop value twice
        double value2 = values.pop();
        double value1 = values.pop();
        // pop operator once
        String op = operators.pop();

        // apply operator and push the result to the values stack
        values.push(applyOperator(value1, value2, op));
    }

    /**
     * Applies the operator op to val1 and val2 respectively and returns the
     * result For example, if op is "*" will return val1 * val2.
     *
     * The only valid operators are "+", "-", "*", and "/".
     *
     * If val2 = 0 and op = "/" will throw IllegalArgumentException.
     */
    private static double applyOperator(double val1, double val2, String op)
    {
        switch (op.charAt(0))
        {
            case '+':
                return val1 + val2;
            case '-':
                return val1 - val2;
            case '*':
                return val1 * val2;
            case '/':
                if (val2 == 0)
                {
                    throw new IllegalArgumentException("Cannot divide by zero");
                }
                return val1 / val2;
        }

        return 0;
    }

    /**
     * Iterates the normalized versions of all of the variables that occur in
     * this formula. No normalization may appear more than once in the iterable,
     * even if it appears more than once in this Formula.
     *
     * For example, if N is a method that converts all the letters in a string
     * to upper case:
     *
     * new Formula("x+y*z", N, s => true).GetVariables() should enumerate "X",
     * "Y", and "Z" new Formula("x+X*z", N, s => true).GetVariables() should
     * enumerate "X" and "Z". new Formula("x+X*z").GetVariables() should
     * enumerate "x", "X", and "z".
     */
    public Iterable<String> getVariables()
    {
        HashSet<String> vars = new HashSet<String>(variables.size());
        for (String v : variables)
        {
            vars.add(v.toString());
        }

        return vars;
    }

    /**
     * Returns a string containing no spaces which, if passed to the Formula
     * constructor, will produce a Formula f such that this.Equals(f). All of
     * the variables in the string should be normalized.
     *
     * For example, if N is a method that converts all the letters in a string
     * to upper case:
     *
     * new Formula("x + y", N, s => true).ToString() should return "X+Y" new
     * Formula("x + Y").ToString() should return "x+Y"
     */
    @Override
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        for (String token : tokens)
        {
            s.append(token);
        }
        return s.toString();
    }

    /**
     * If obj is null or obj is not a Formula, returns false. Otherwise, reports
     * whether or not this Formula and obj are equal.
     *
     * Two Formulae are considered equal if they consist of the same tokens in
     * the same order. To determine token equality, all tokens are compared as
     * strings except for numeric tokens and variable tokens. Numeric tokens are
     * considered equal if they are equal after being "normalized" by Java's
     * standard conversion from string to double, then back to string. This
     * eliminates any inconsistencies due to limited floating point precision.
     * Variable tokens are considered equal if their normalized forms are equal,
     * as defined by the provided normalizer.
     *
     * For example, if N is a method that converts all the letters in a string
     * to upper case:
     *
     * new Formula("x1+y2", N, s => true).Equals(new Formula("X1 + Y2")) is true
     * new Formula("x1+y2").Equals(new Formula("X1+Y2")) is false new
     * Formula("x1+y2").Equals(new Formula("y2+x1")) is false new Formula("2.0 +
     * x7").Equals(new Formula("2.000 + x7")) is true
     */
    @Override
    public boolean equals(Object obj)
    {
        return (obj != null) && (obj instanceof Formula) && (this.toString().equals(obj.toString()));
    }

    /**
     * Returns a hash code for this Formula. If f1.Equals(f2), then it must be
     * the case that f1.GetHashCode() == f2.GetHashCode(). Ideally, the
     * probability that two randomly-generated unequal Formulae have the same
     * hash code should be extremely small.
     */
    @Override
    public int hashCode()
    {
        return this.toString().hashCode();
    }

    /**
     * Takes a list of valid tokens making up this formula and verifies the
     * ordering of the tokens does not contain a syntax error.
     *
     * If there is a syntax error in the cleaned tokens, throws a
     * FormulaFormatException.
     */
    private void verifySyntax(ArrayList<String> cleanedTokens)
    {
        // C complexity rules
        oneTokenRule(cleanedTokens);
        startingTokenRule(cleanedTokens);
        endingTokenRule(cleanedTokens);

        // N complexity rules
        // counter for parentheses rules
        int parenthesesCount = 0;
        for (int i = 0; i < cleanedTokens.size(); i++)
        {
            if (cleanedTokens.get(i).equals("("))
            {
                parenthesesCount++;
                parenthesesFollowRule(cleanedTokens, i);
            }
            else if (ExtensionMethods.isOperator(cleanedTokens.get(i)))
            {
                parenthesesFollowRule(cleanedTokens, i);
            }
            else if (cleanedTokens.get(i).equals(")"))
            {
                rightParenthesesRule(--parenthesesCount);
                extraFollowRule(cleanedTokens, i);
            }
            else if (ExtensionMethods.startsWithLetterOrUnderscore(cleanedTokens.get(i))
                    || ExtensionMethods.startsWithNumber(cleanedTokens.get(i)))
            {
                extraFollowRule(cleanedTokens, i);
            }
        }

        balancedParenthesesRule(parenthesesCount);
    }

    /**
     * Extra Follow Rule: Any token that immediately follows a number, a
     * variable, or a closing parenthesis must be either an operator or a
     * closing parenthesis.
     *
     * If the Extra Follow Rule is violated, throws a FormulaFormatException
     */
    private void extraFollowRule(ArrayList<String> cleanedTokens, int index)
    {
        // check next token
        if (ExtensionMethods.hasNext(cleanedTokens, index))
        {
            String nextToken = cleanedTokens.get(index + 1);
            if (!(ExtensionMethods.isOperator(nextToken) || nextToken.equals(")")))
            {
                String message = "Extra Follow Rule Violation: Any token that "
                        + "immediately follows a number, a variable, or a "
                        + "closing parenthesis must be either an operator or a "
                        + "closing parenthesis.";
                throw new FormulaFormatException(message);
            }
        }
        // these tokens can end an expression
    }

    /**
     * Parentheses Follow Rule: Any token that immediately follows an opening
     * parenthesis or an operator must be either a number, a variable, or an
     * opening parenthesis.
     *
     * If the Parentheses Follow Rule is violated, throws a
     * FormulaFormatException
     */
    private void parenthesesFollowRule(ArrayList<String> cleanedTokens, int index)
    {
        String message = "Parentheses Follow Rule Violation: Any token that "
                + "immediately follows an opening parenthesis or an operator "
                + "must be either a number, a variable, or an opening "
                + "parenthesis.";

        // check next token
        if (ExtensionMethods.hasNext(cleanedTokens, index))
        {
            String nextToken = cleanedTokens.get(index + 1);
            if (!(ExtensionMethods.startsWithNumber(nextToken)
                    || ExtensionMethods.startsWithLetterOrUnderscore(nextToken) 
                    || nextToken.equals("(")))
            {

                throw new FormulaFormatException(message);
            }
        }

        // these tokens cannot end an expression, there should have been a next token
        else
        {
            throw new FormulaFormatException(message);
        }
    }

    /**
     * Ending Token Rule: The last token of an expression must be a number, a
     * variable, or a closing parenthesis.
     *
     * If the Ending Token Rule is violated, throws a FormulaFormatException
     */
    private void endingTokenRule(ArrayList<String> cleanedTokens)
    {
        if (cleanedTokens.size() > 0)
        {
            String endingToken = cleanedTokens.get(cleanedTokens.size() - 1);
            if (!(ExtensionMethods.startsWithNumber(endingToken)
                    || ExtensionMethods.startsWithLetterOrUnderscore(endingToken)
                    || endingToken.equals(")")))
            {
                String message = "Ending Token Rule Violation: The last token of"
                        + "an expression must be a number, a variable, or a "
                        + "closing parenthesis.";
                throw new FormulaFormatException(message);
            }
        }
    }

    /**
     * Starting Token Rule: The first token of an expression must be a number, a
     * variable, or an opening parenthesis.
     *
     * If Starting Token Rule is violated, throws FormulaFormatException
     */
    private void startingTokenRule(ArrayList<String> cleanedTokens)
    {
        if (cleanedTokens.size() > 0)
        {
            String startingToken = cleanedTokens.get(0);
            if (!(ExtensionMethods.startsWithNumber(startingToken)
                    || ExtensionMethods.startsWithLetterOrUnderscore(startingToken) 
                    || startingToken.equals("(")))
            {
                String message = "Starting Token Rule Violation: The first token"
                        + " of an expression must be a number, a variable, or an"
                        + " opening parenthesis";
                throw new FormulaFormatException(message);
            }
        }
    }

    /**
     * One Token Rule: there must be at least one token.
     *
     * If One Token Rule is violated, throws FormulaFormatException
     */
    private void oneTokenRule(ArrayList<String> cleanedTokens)
    {
        if (cleanedTokens.size() < 1)
        {
            String message = "One Token Rule Violation: Formula must contain at"
                    + " least one token.";
            throw new FormulaFormatException(message);
        }
    }

    /**
     * Balanced Parentheses Rule: The total number of opening parentheses must
     * equal the total number of closing parentheses.
     *
     * Takes an int which represents the number of opening parentheses minus the
     * number of closing parentheses. If Balanced Parentheses rule is violated
     * throws FormulaFormatException.
     *
     * @param parenthesesCount - The number of opening parentheses minus the
     * number of closing parentheses
     */
    private void balancedParenthesesRule(int parenthesesCount)
    {
        if (parenthesesCount != 0)
        {
            String message = "Balanced Parentheses Rule Violation: The total "
                    + "number of opening parentheses must equal the total number"
                    + " of closing parentheses";
            throw new FormulaFormatException(message);
        }
    }

    /**
     * Right Parenthesis Rule: When reading tokens from left to right, at no
     * point should the number of closing parentheses seen so far be greater
     * than the number of opening parentheses seen so far.
     *
     * Takes an int which represents the number of opening parentheses minus the
     * number of closing parentheses seen thus far when read from left to right.
     * If right parenthesis rule is violated throws FormulaFormatException.
     *
     * @param parenthesesCount -The number of opening parentheses minus the
     * number of closing parentheses seen thus far when read from left to right
     */
    private void rightParenthesesRule(int parenthesesCount)
    {
        if (parenthesesCount < 0)
        {
            String message = "Right Parentheses Rule Violation: Number of "
                    + "closing parentheses greater than opening parentheses when"
                    + " read from left to right";
            throw new FormulaFormatException(message);
        }
    }

    /**
     * Given an expression, iterates through the tokens that compose it. Tokens
     * are left paren; right paren; one of the four operator symbols; a string
     * consisting of a letter or underscore followed by zero or more letters,
     * digits, or underscores; a double literal; and anything that doesn't match
     * one of those patterns. There are no empty tokens, and no token contains
     * white space.
     */
    private static Iterable<String> getTokens(String formula)
    {
        // Patterns for individual tokens. Cannot contain white space
        String lpPattern = "\\(";
        String rpPattern = "\\)";
        String opPattern = "[\\+\\-*\\/]";
        String varPattern = "[a-zA-Z_](?:[a-zA-Z_]|\\d)*";
        String doublePattern = "(?:\\d+\\.\\d*|\\d*\\.\\d+|\\d+)(?:[eE][\\+-]?\\d+)?";
        String spacePattern = "\\s+";

        // Overall pattern
        String pattern = String.format("(%s)|(%s)|(%s)|(%s)|(%s)|(%s)",
                lpPattern, rpPattern, opPattern, varPattern, doublePattern,
                spacePattern);

        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(formula);

        ArrayList<String> tokens = new ArrayList<String>();
        while (!matcher.hitEnd() && matcher.find())
        {
            // Skip tokens consisting only of white space
            if (!matcher.group().matches("^\\s*$"))
            {
                tokens.add(matcher.group());
            }
        }

        return tokens;
    }

    /**
     * Takes a String s, and returns a copy of that string, with all white space
     * stripped.
     */
    private static String stripWhiteSpace(String s)
    {
        String spacePattern = "\\s+";
        String[] tokens = s.split(spacePattern);

        StringBuilder strippedWhiteSpace = new StringBuilder();
        for (String token : tokens)
        {
            strippedWhiteSpace.append(token);
        }
        return strippedWhiteSpace.toString();
    }

    /**
     * Joins the strings contained in tokens into a single string.
     */
    private static String joinTokens(Iterable<String> tokens)
    {
        StringBuilder tokenString = new StringBuilder();
        for (String token : tokens)
        {
            tokenString.append(token);
        }
        return tokenString.toString();
    }

    /**
     * Takes in a string, token, and returns true if it's a valid token
     *
     * Tokens are valid if they are "(", ")", "+", "-", "*", or "/". Tokens are
     * also valid if it is a string which consists of a letter or underscore
     * followed by zero or more letters, underscores, or digits or is a valid
     * floating point number.
     */
    private static boolean isValidToken(String token)
    {
        // a pattern that matches all valid tokens without white space
        String pattern = "(^\\($)|(^\\)$)|(^-$)|(^\\+$)|(^\\*$)|(^\\/$)"
                + "|(^[a-zA-Z_][a-zA-Z\\d_]*$)";
        return Pattern.matches(pattern, token) || ExtensionMethods.isDoubleString(token);
    }

    /**
     * A normalizer class to use as the default normalizer.
     */
    private class DoubleNormalize implements Normalizer
    {

        /**
         * Takes in a string value, if it can be parsed to a double d, returns
         * the Double.toString(d) else returns s unchanged.
         */
        @Override
        public String normalize(String s)
        {
            if (ExtensionMethods.isDoubleString(s))
            {
                return Double.valueOf(s).toString();
            }
            else
            {
                return s;
            }
        }
    }

    /**
     * Helpful static methods to use with standard library items for
     * implementing the Formula class.
     */
    public static class ExtensionMethods
    {

        /**
         * Returns true if string begins with a number
         */
        public static boolean startsWithNumber(String s)
        {
            return (s.charAt(0) >= '0' && s.charAt(0) <= '9');
        }

        /**
         * Returns true if string begins with a letter of the english language
         * or an underscore.
         */
        public static boolean startsWithLetterOrUnderscore(String s)
        {
            return ((s.charAt(0) >= 'a' && s.charAt(0) <= 'z') 
                    || (s.charAt(0) >= 'A' && s.charAt(0) <= 'Z')
                    || (s.charAt(0) == '_'));
        }

        /**
         * Returns true if s is an operator '*', '/', '+' or '-'
         */
        public static boolean isOperator(String s)
        {
            return (s.equals("*") || s.equals("/") || s.equals("+") || s.equals("-"));
        }

        /**
         * Takes a string s and returns true if a matching string is top of the
         * stack, else returns false.
         */
        public static boolean isAtTop(Stack<String> stack, String s)
        {
            return (stack.size() > 0 && stack.peek().equals(s));
        }

        /**
         * Takes an index and returns true if there is an item contained in this
         * list after that index, else returns false.
         */
        public static boolean hasNext(ArrayList<String> list, int index)
        {
            return (list.size() > (index + 1));
        }

        /**
         * Takes in a string s and returns true if s can successfully be parsed
         * to a Double, else returns false.
         */
        public static boolean isDoubleString(String s)
        {

            if (s == null)
            {
                return false;
            }

            // prescribed method from Double.valueOf(String) method documentation for
            // outputting a double from string without throwing exception.
            // Copied directly from documentation.
            final String Digits = "(\\p{Digit}+)";
            final String HexDigits = "(\\p{XDigit}+)";
            // an exponent is 'e' or 'E' followed by an optionally
            // signed decimal integer.
            final String Exp = "[eE][+-]?" + Digits;
            final String fpRegex = ("[\\x00-\\x20]*"
                    + // Optional leading "whitespace"
                    "[+-]?("
                    + // Optional sign character
                    "NaN|"
                    + // "NaN" string
                    "Infinity|"
                    + // "Infinity" string
                    // A decimal floating-point string representing a finite positive
                    // number without a leading sign has at most five basic pieces:
                    // Digits . Digits ExponentPart FloatTypeSuffix
                    //
                    // Since this method allows integer-only strings as input
                    // in addition to strings of floating-point literals, the
                    // two sub-patterns below are simplifications of the grammar
                    // productions from section 3.10.2 of
                    // The Java Language Specification.
                    // Digits ._opt Digits_opt ExponentPart_opt FloatTypeSuffix_opt
                    "(((" + Digits + "(\\.)?(" + Digits + "?)(" + Exp + ")?)|"
                    + // . Digits ExponentPart_opt FloatTypeSuffix_opt
                    "(\\.(" + Digits + ")(" + Exp + ")?)|"
                    + // Hexadecimal strings
                    "(("
                    + // 0[xX] HexDigits ._opt BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "(\\.)?)|"
                    + // 0[xX] HexDigits_opt . HexDigits BinaryExponent FloatTypeSuffix_opt
                    "(0[xX]" + HexDigits + "?(\\.)" + HexDigits + ")"
                    + ")[pP][+-]?" + Digits + "))" + "[fFdD]?))" + "[\\x00-\\x20]*");// Optional trailing "whitespace"

            return Pattern.matches(fpRegex, s);

        }

    }

}
