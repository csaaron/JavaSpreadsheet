package spreadsheet;

import java.util.HashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SpreadsheetDocumentHandler extends DefaultHandler
{

    /**
     * A HashMap containing the contents of each cell, keyed by the cell name.
     */
    private HashMap<String, String> cellsAndContents;
    /**
     * stores the version of the saved spreadhseet
     */
    private String version;

    private StringBuilder contentsBuilder;
    private CellContents currentContents;

    public SpreadsheetDocumentHandler()
    {
        super();
        cellsAndContents = new HashMap<String, String>();
        contentsBuilder = new StringBuilder();
        currentContents = new CellContents();
        version = "";

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
    {
        contentsBuilder.setLength(0); // clears the contentsBuilder at each new element

        // store the spreadsheet version information
        if (qName.equalsIgnoreCase("spreadsheet"))
        {
            version = atts.getValue("version");
        }

        // create storage for new cell name and contents
        if (qName.equalsIgnoreCase("cell"))
        {
            currentContents = new CellContents();
        }

    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException
    {
        if (qName.equalsIgnoreCase("name"))
        {
            currentContents.setName(contentsBuilder.toString());
        }

        if (qName.equalsIgnoreCase("contents"))
        {
            currentContents.setContents(contentsBuilder.toString());
        }

        // we are at the end of the cell tag, so should have name and contents to store
        // in HashMap
        if (qName.equalsIgnoreCase("cell"))
        {
            cellsAndContents.put(currentContents.getName(), currentContents.getContents());
        }

    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException
    {
        // reads in the chars in between opening and closing tags. Can come in multiple
        // chunks so can't just create new string.
        contentsBuilder.append(ch, start, length);
    }

    /**
     * Returns a HasMap keyed by the cell names and whos values are the contents
     * of the cells contained in the saved spreadsheet. Map will be empty if
     * parsing has not be completed.
     */
    public HashMap<String, String> getCellNamesAndContents()
    {
        return cellsAndContents;
    }

    /**
     * Returns the version of the saved spreadsheet. Will return an empty string
     * if parsing has not be completed.
     */
    public String getSpreadsheetVersion()
    {
        return version;
    }

    /**
     * Convenience class for storing the name and contents of a cell while
     * parsing a Spreadsheet xml file.
     */
    private class CellContents
    {

        private String name;
        private String contents;

        /**
         * Constructs a CellContents object
         */
        public CellContents()
        {
            name = "";
            contents = "";
        }

        /**
         * Sets the name parameter of this object
         */
        public void setName(String name)
        {
            this.name = name;
        }

        /**
         * Sets the contents parameter of this object
         */
        public void setContents(String contents)
        {
            this.contents = contents;
        }

        /**
         * Returns the name parameter of this object
         */
        public String getName()
        {
            return name;
        }

        /**
         * Returns the contents parameter of this object
         */
        public String getContents()
        {
            return contents;
        }
    }

}
