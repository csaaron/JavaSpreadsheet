/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetControl;

import java.util.regex.Pattern;

/**
 *
 * @author aaron
 */
public class CellValidator implements ssUtils.IsValidFunctor
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
