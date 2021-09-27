package spreadsheetControl;

import java.util.regex.Pattern;

/**
 * Validates cell names for Spreadsheet objects
 *
 * CellValidator will match all strings that begin with one letter of the
 * alphabet followed by any number between 1-99
 */
public class CellValidator implements ssUtils.IsValid
{

    private Pattern cellNamePattern;

    public CellValidator()
    {
        cellNamePattern = Pattern.compile("^[a-zA-Z][1-9]\\d?$");
    }

    @Override
    public boolean isValid(String s)
    {
        return cellNamePattern.matcher(s).find();
    }

}
