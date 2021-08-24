/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

/**
 * A JPanel consisting of a grid of JLabels
 */
public class LabelGrid extends javax.swing.JPanel
{

    private static final int COLUMN_WIDTH = 80;

    private int labelWidth;
    private int labelHeight;
    private int rowsSize;
    private int columnSize;

    private GridLabel[][] labels;

    private int selectedRow;
    private int selectedColumn;

    /**
     * Creates a grid of JLabels with 99 rows and 26 columns with the default
     * width and height
     */
    LabelGrid()
    {
        this(99, 26, COLUMN_WIDTH, new JLabel("example").getPreferredSize().height);
    }

    /**
     * Creates a grid of JLabels consisting of the specified number of rows and
     * columns with the default width and height
     */
    LabelGrid(int rows, int columns)
    {
        this(rows, columns, COLUMN_WIDTH, new JLabel("example").getPreferredSize().height);
    }

    /**
     * Creates a grid of JLabels consisting of the specified number of rows and
     * columns consisting of labels with dimensions width and height
     */
    LabelGrid(int rows, int columns, int width, int height)
    {
        super();
        
        labelWidth = width;
        labelHeight = height;
        rowsSize = rows;
        columnSize = columns;

        selectedRow = 0;
        selectedColumn = 0;

        labels = new GridLabel[rowsSize][columnSize];

        initComponents();

        setSelection(0, 0);
    }

    /**
     * Set this panel's layout as an absolute layout and initialize and place
     * all components
     */
    private void initComponents()
    {
        //create an absolute layout.
        setLayout(null);

        setBackground(new java.awt.Color(255, 255, 255));
        setAutoscrolls(true);

        createLabelGrid();

        setPreferredSize(new Dimension(labelWidth * columnSize, labelHeight * rowsSize));
    }

    /**
     * Creates a grid of JLabels with the parameters of this LabelGrid
     */
    private void createLabelGrid()
    {
        int locationX = 0;
        int locationY = 0;

        for (int row = 0; row < rowsSize; row++)
        {
            for (int column = 0; column < columnSize; column++)
            {
                GridLabel label = new GridLabel(row, column);
                label.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                // label.setHorizontalAlignment(SwingConstants.CENTER);

                labels[row][column] = label;
                add(label);
                label.setBounds(locationX, locationY, labelWidth, labelHeight);

                locationX += labelWidth;
            }

            locationX = 0;
            locationY += labelHeight;
        }
    }

    /**
     * Returns true if the zero-based column and row is in range, else returns
     * false
     */
    public boolean inRange(int row, int col)
    {
        if ((row >= 0 && row < rowsSize) && (col >= 0 && col < columnSize))
        {
            
            return true;
        }
        
        return false;
    }

    /**
     * returns the zero-based row index of the currently selected label
     */
    public int getSelectionRow()
    {
        return selectedRow;
    }

    /**
     * returns the zero-based column index of the currently selected label
     */
    public int getSelectionColumn()
    {
        return selectedColumn;
    }

    /**
     * If zero based row and column provided are within range, sets the label at
     * that location as selected and returns true, else returns false
     */
    public boolean setSelection(int row, int col)
    {
        if (!inRange(row, col))
        {
            return false;
        }

        //unset old selection by setting default border
        labels[selectedRow][selectedColumn].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        //set new selection
        //thick border
        labels[row][col].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        //update selected
        selectedRow = row;
        selectedColumn = col;

        return true;
    }

    /**
     * If the JLabel provided exists on the grid of this LabelGrid, marks it as
     * the selected and returns true, else returns false
     */
    public boolean setSelection(JLabel label)
    {
        if (!(label instanceof GridLabel))
        {
            return false;
        }

        GridLabel l = (GridLabel) label;

        return setSelection(l.getRow(), l.getColumn());

    }

    /**
     * If zero based row and column provided are within range, sets the text of
     * that label to value and returns true, else returns false
     */
    protected boolean setValue(int row, int col, String value)
    {
        if (!inRange(row, col))
        {
            return false;
        }

        labels[row][col].setText(value);

        return true;
    }

    private class GridLabel extends JLabel implements MouseListener
    {

        private int row;
        private int column;

        public GridLabel(int row, int column)
        {
            super();
            this.row = row;
            this.column = column;
            addMouseListener(this);
            addMouseListener(new RedispatchMouseListener());

        }

        public int getRow()
        {
            return row;
        }

        public int getColumn()
        {

            return column;
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
            setSelection(row, column);
        }

        @Override
        public void mousePressed(MouseEvent e)
        {
            return;
        }

        @Override
        public void mouseReleased(MouseEvent e)
        {
            return;
        }

        @Override
        public void mouseEntered(MouseEvent e)
        {
            return;
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
            return;
        }

    }
    
    
}
