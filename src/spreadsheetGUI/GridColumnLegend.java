/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * Creates column legends for a ScrollableSpreadsheetPanel
 */
public class GridColumnLegend extends javax.swing.JPanel
{
    private static final int COLUMN_WIDTH = 80;
    private static final int ROW_NUMBER_WIDTH = 20;

    private JLabel[] labels;
    private int size;
    private int labelWidth;
    private int labelHeight;
    
    private int selection;

    /**
     * Creates column legends for a ScrollableSpreadsheetPanel using the
     * default legends and default label width and height
     * 
     * Default legend contains 26 legends labeled A - Z
     */
    public GridColumnLegend()
    {
        super();
        
        JLabel example = new JLabel("example");
        
        size = (int) 'Z'-'A'+ 1;
        labels = new JLabel[size];
        labelWidth = COLUMN_WIDTH;
        labelHeight = example.getPreferredSize().height;
        selection = 0;
        
        
        String[] legends = new String[size];
        
        for (int i = 0; i + 'A' < 'Z' +1; i++)
        {
            legends[i] = String.valueOf((char)(i + 'A'));
        }
        
        initComponents(legends);
        
        setSelection(selection);
    }

    /**
     * Creates column legends for a ScrollableSpreadsheetPanel using the
     * default width and height and using legends for column headers
     */
    public GridColumnLegend(String[] legends)
    {
        super();
        
        JLabel example = new JLabel("example");
        
        size = legends.length;
        labels = new JLabel[size];
        labelWidth = COLUMN_WIDTH;
        labelHeight = example.getPreferredSize().height;
        selection = 0;
        
        initComponents(legends);
        
        setSelection(selection);
        
    }

    /**
     * Creates column legends for a ScrollableSpreadsheetPanel using the
     * default width and height and using legends for column headers
     */
    public GridColumnLegend(ArrayList<String> legends)
    {
        this((String[])legends.toArray());
    }

    /**
     * Creates column legends for a ScrollableSpreadsheetPanel using the
     * provided width and height and using legends for column headers
     */
    public GridColumnLegend(String[] legends, int labelWidth, int labelHeight)
    {
        super();
        
        size = legends.length;
        labels = new JLabel[size];
        this.labelWidth = labelWidth;
        this.labelHeight = labelHeight;
        selection = 0;
        
        initComponents(legends);
        
        setSelection(selection);
    }

    /**
     * Creates column legends for a ScrollableSpreadsheetPanel using the
     * provided width and height and using legends for column headers
     */
    public GridColumnLegend(ArrayList<String> legends, int labelWidth, int labelHeight)
    {
        this((String[])legends.toArray(), labelWidth, labelHeight);
    }

    /**
     * Set this panel's layout as an absolute layout and initialize and place all components  
     */
    private void initComponents(String[] legends)
    {
        //create an absolute layout.
        setLayout(null);
        setAutoscrolls(true);

        createLabels(legends);

        setPreferredSize(new Dimension(size * labelWidth, labelHeight));
    }

    /**
     * Create and place the labels for this panel  
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

            locationX += labelWidth;
        }
    }
    
     public boolean inRange(int column)
    {
        if (column >= 0 && column < size)
        {
            return true;
        }
        
        return false;
    }
    
    public boolean setSelection(int column)
    {
        if (!inRange(column))
        {
            return false;
        }
        
        //unset old selection by setting default border
        labels[selection].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        toggleBold(selection);
        
        //set new selection
        //thick border
        labels[column].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));
        toggleBold(column);

        //update selected
        selection = column;
        
        return true;
    }
    
    private void toggleBold(int column)
    {
        Font font = labels[column].getFont();
        labels[column].setFont(font.deriveFont(font.getStyle() ^ Font.BOLD));
    }

}
