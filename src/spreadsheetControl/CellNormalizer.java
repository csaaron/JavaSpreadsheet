package spreadsheetControl;

import ssUtils.Normalizer;

/**
 * Normalizes cell names for Spreadsheet objects
 */
public class CellNormalizer implements Normalizer
{

    /**
     * Returns s converted to uppercase
     */
    @Override
    public String normalize(String s)
    {
        return s.toUpperCase();
    }

}
