package spreadsheetGUI;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JLabel;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.AnnotationHelper;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.helpers.MethodFilter;

/**
 * A JPanel consisting of a grid of JLabels
 */
public class LabelGrid extends javax.swing.JPanel implements ItemSelectable
{

    // Used to create a JLabel to base sizing off of. 
    // Need to base sizing off of system font defaults
    private static final String DEFAULT_SIZE = "0000000000";

    private int labelWidth;
    private int labelHeight;
    private int rowsSize;
    private int columnSize;
    private float fontSize;
            
    private GridLabel[][] labels;

    private int selectedRow;
    private int selectedColumn;

    /**
     * Creates a grid of JLabels with 99 rows and 26 columns with the default
     * width and height
     */
    LabelGrid()
    {
        this(99, 26, 16);
    }

    /**
     * Creates a grid of JLabels consisting of the specified number of rows and
     * columns with the default width and height
     */
    LabelGrid(int rows, int columns)
    {
        this(rows,columns, 16);
    }
    
    /**
     * Creates a grid of JLabels consisting of the specified number of rows and
     * columns with the default width and height
     */
    LabelGrid(int rows, int columns, float fontSize)
    {
        super();
        
        JLabel label = new JLabel(DEFAULT_SIZE);
        label.setFont(label.getFont().deriveFont(fontSize));
        
        labelWidth = label.getPreferredSize().width;
        labelHeight = label.getPreferredSize().height;
        rowsSize = rows;
        columnSize = columns;
        this.fontSize = fontSize;
        
        selectedRow = 0;
        selectedColumn = 0;

        labels = new GridLabel[rowsSize][columnSize];

        initComponents();

        setSelection(0, 0);
    }

    /**
     * Creates a grid of JLabels consisting of the specified number of rows and
     * columns consisting of labels with dimensions width and height
     */
    LabelGrid(int rows, int columns, int width, int height, float fontSize)
    {
        super();

        labelWidth = width;
        labelHeight = height;
        rowsSize = rows;
        columnSize = columns;
        this.fontSize = fontSize;
        
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
                label.setForeground(Color.BLACK);
                label.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
                label.setFont(label.getFont().deriveFont(fontSize));

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

        //fire event stating old selection was deselected
        fireItemEvent(labels[selectedRow][selectedColumn], false);

        //unset old selection by setting default border
        labels[selectedRow][selectedColumn].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        //set new selection
        //thick border
        labels[row][col].setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        //update selected
        selectedRow = row;
        selectedColumn = col;

        //fire event stating new selection was selected
        fireItemEvent(labels[selectedRow][selectedColumn], true);

        return true;
    }

    /**
     * If the JLabel provided exists on the grid of this LabelGrid, marks it as
     * selected and returns true, else returns false
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

    /**
     * Returns the currently selected Label in this LabelGrid
     */
    @Override
    public Object[] getSelectedObjects()
    {
        return new Object[]
        {
            labels[selectedRow][selectedColumn]
        };
    }

    /**
     * Adds a listener to receive item events when the state of an item is
     * changed by the user. Item events are not sent when an item's state is set
     * programmatically. If l is null, no exception is thrown and no action is
     * performed.
     *
     * @param arg0 the listener to receive events
     */
    @Override
    public void addItemListener(ItemListener arg0)
    {
        listenerList.add(ItemListener.class, arg0);
    }

    /**
     * Removes an item listener. If l is null, no exception is thrown and no
     * action is performed.
     *
     * @param arg0 the listener being removed
     */
    @Override
    public void removeItemListener(ItemListener arg0)
    {
        listenerList.remove(ItemListener.class, arg0);
    }

    /**
     * Notifies all ItemListeners registered with this component that the
     * parameter item has been selected or deselected. Selected is true if
     * selected, false if it was deselected.
     *
     * @param item the object that was selected
     * @param selected true if item was selected, false if it was deselected
     */
    void fireItemEvent(Object item, boolean selected)
    {
        // create event
        ItemEvent event = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                labels[selectedRow][selectedColumn],
                selected ? ItemEvent.SELECTED : ItemEvent.DESELECTED);

        // get each of the ItemEvents registered with this object
        ItemListener[] listeners = listenerList.getListeners(ItemListener.class);

        // send event to each of the listeners
        for (ItemListener listener : listeners)
        {
            listener.itemStateChanged(event);
        }
    }

    /**
     * GridLabel extends JLabel to include information about its position in
     * this grid and and to fire events when clicked
     */
    private class GridLabel extends JLabel implements MouseListener
    {

        private int row;
        private int column;

        /**
         * Creates a GridLabel located at row and column
         */
        public GridLabel(int row, int column)
        {
            super();
            this.row = row;
            this.column = column;

            addMouseListener(this);
            // pass the mouse click event up to a parent which may be listening 
            // for it
            addMouseListener(new RedispatchMouseListener());

        }

        /**
         * returns this label's row
         */
        public int getRow()
        {
            return row;
        }

        /**
         * returns this label's column
         */
        public int getColumn()
        {

            return column;
        }

        /**
         * Sets this label as selected when clicked
         */
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

        public void actionPerformed(ActionEvent e)
        {
            return;
        }

    }

}
