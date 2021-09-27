package spreadsheetGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Creates row legends for a ScrollableSpreadsheetPanel
 */
public class GridRowLegend extends javax.swing.JPanel
{
    private static final String DEFAULT_SIZE = "000";
    
    private JLabel[] labels;
    private int size;
    private int labelWidth;
    private int labelHeight;

    private int selection;

    /**
     * Creates row legends for a SpreadsheetPanel using the default legends and
     * default label width and height
     *
     * Default legend contains 99 legends labeled 1 - 99
     */
    public GridRowLegend()
    {
        super();

        JLabel example = new JLabel(DEFAULT_SIZE);

        size = 99;
        labels = new JLabel[size];
        labelWidth = example.getPreferredSize().width;
        labelHeight = example.getPreferredSize().height;
        selection = 0;

        String[] legends = new String[size];

        for (int i = 0; i < size; i++)
        {
            legends[i] = String.valueOf(i + 1);
        }

        initComponents(legends);

    }
    
    /**
     * Creates row legends for a SpreadsheetPanel using the default legends and
     * provided label width and height
     *
     * Default legend contains 99 legends labeled 1 - 99
     */
    public GridRowLegend(int width, int height)
    {
        super();

        JLabel example = new JLabel(DEFAULT_SIZE);

        size = 99;
        labels = new JLabel[size];
        labelWidth = width;
        labelHeight = height;
        selection = 0;

        String[] legends = new String[size];

        for (int i = 0; i < size; i++)
        {
            legends[i] = String.valueOf(i + 1);
        }

        initComponents(legends);

    }

    /**
     * Creates row legends for a SpreadsheetPanel using the default width and
     * height and using legends for row headers
     */
    public GridRowLegend(String[] legends)
    {
        super();

        JLabel example = new JLabel(DEFAULT_SIZE);

        size = legends.length;
        labels = new JLabel[size];
        labelWidth = example.getPreferredSize().width;
        labelHeight = example.getPreferredSize().height;
        selection = 0;

        initComponents(legends);
    }

    /**
     * Creates row legends for a SpreadsheetPanel using the default width and
     * height and using legends for row headers
     */
    public GridRowLegend(ArrayList<String> legends)
    {
        this((String[]) legends.toArray());
    }

    /**
     * Creates row legends for a SpreadsheetPanel using the provided width and
     * height and using legends for row headers
     */
    public GridRowLegend(String[] legends, int labelWidth, int labelHeight)
    {
        super();

        size = legends.length;
        labels = new JLabel[size];
        this.labelWidth = labelWidth;
        this.labelHeight = labelHeight;
        selection = 0;

        initComponents(legends);
    }

    /**
     * Creates row legends for a ScrollableSpreadsheetPanel using the provided
     * width and height and using legends for row headers
     */
    public GridRowLegend(ArrayList<String> legends, int labelWidth, int labelHeight)
    {
        this((String[]) legends.toArray(), labelWidth, labelHeight);
    }

    /**
     * Set this panel's layout as an absolute layout and initialize and place
     * all components
     */
    private void initComponents(String[] legends)
    {
        //create an absolute layout.
        setLayout(null);
        setAutoscrolls(true);

        createLabels(legends);

        setPreferredSize(new Dimension(labelWidth, size * labelHeight));
    }

    /**
     * create the labels for this panel
     */
    private void createLabels(String[] legends)
    {
        int locationX = 0;
        int locationY = 0;

        // create column name row
        for (int i = 0; i < legends.length; i++)
        {
            JLabel label = new JLabel();
            label.setText(legends[i]);
            label.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
            label.setHorizontalAlignment(SwingConstants.CENTER);

            labels[i] = label;
            add(label);
            label.setBounds(locationX, locationY, labelWidth, labelHeight);

            locationY += labelHeight;
        }
    }

    /**
     * Returns true if the zero indexed row exists in this legend, else
     * returns false
     */
    public boolean inRange(int row)
    {
        if (row >= 0 && row < size)
        {
            return true;
        }

        return false;
    }

    /**
     * Sets the zero indexed row legend as selected 
     */
    public boolean setSelection(int row)
    {
        if (!inRange(row))
        {
            return false;
        }

        //unset old selection by setting default border
        labels[selection].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        toggleBold(selection, false);

        //set new selection
        //thick border
        labels[row].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        toggleBold(row, true);

        //update selected
        selection = row;

        return true;
    }

    /**
     * Sets the text in the zero indexed row as bold if the boolean activate 
     * is true else sets the font to the standard weight
     */
    private void toggleBold(int column, boolean activate)
    {
        Font font = labels[column].getFont();
        
        if (activate)
        {
            labels[column].setFont(font.deriveFont(font.getStyle() | Font.BOLD));
        }
        else
        {
            labels[column].setFont(font.deriveFont(font.getStyle() & ~Font.BOLD));
        }
    }
}
