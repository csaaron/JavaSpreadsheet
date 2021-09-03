/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetControl;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author aaron
 */
public class SpreadsheetFileFilter extends FileFilter
{

    @Override
    public boolean accept(File arg0)
    {
        if (arg0.isDirectory())
        {
            return true;
        }
        
        String extension = getExtension(arg0);
        if (extension != null)
            return extension.equals("sprd");
        
        return false;
        
    }

    @Override
    public String getDescription()
    {
        return "\"*.sprd\" | Spreadsheet Files";
    }
    
    /**
     * If the given File f has an extension returns the extension in lower case
     * else returns null;
     */
    private static String getExtension(File f)
    {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');
        
        if (i > 0 && i < s.length() -1)
        {
            ext = s.substring(i+1).toLowerCase();
        }
        return ext;
    }

}
