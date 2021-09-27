package ssUtils;

/**
 * Used as a possible return value of the Formula.evaluate method. Contains an
 * explanation for why the value of an evaluated formula could not be calculated
 */
public class FormulaError
{

    /**
     * The reason why this FormulaError was created.
     */
    private String reason;

    /**
     * Constructs a FormulaError containing the reason it is an error.
     */
    public FormulaError(String reason)
    {
        this.reason = reason;
    }

    /**
     * Returns the reason for this error.
     */
    public String getReason()
    {
        return reason;
    }

}
