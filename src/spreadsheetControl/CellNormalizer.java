/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetControl;

import ssUtils.Normalizer;

/**
 *
 * @author aaron
 */
public class CellNormalizer implements Normalizer
{

    @Override
    public String normalize(String s)
    {
        return s.toUpperCase();
    }

    
    
}