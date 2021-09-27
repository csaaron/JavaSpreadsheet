package spreadsheetControl;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * SpreadsheetFileFilter provides filtering for JFileChooser matching
 *
 * Filters for files with the "sprd" file extension and allows for file system
 * navigation
 */
public class SpreadsheetFileFilter extends FileFilter
{

    @Override
    public boolean accept(File arg0)
    {
        /**
         * Allow directories to show in filter for file system navigation
         */
        if (arg0.isDirectory())
        {
            return true;
        }

        String extension = getExtension(arg0);
        if (extension != null)
        {
            return extension.equals("sprd");
        }

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

        if (i > 0 && i < s.length() - 1)
        {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
