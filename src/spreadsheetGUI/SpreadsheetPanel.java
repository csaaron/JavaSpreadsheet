package spreadsheetGUI;

import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

/**
 * SpreadsheetPanel is a JPanel which provides an ItemSelectable interface
 * consisting of a scrollable grid of selectable cells
 *
 * Rows of cells are labeled 1-99 and columns of cells are labeled A-Z
 */
public class SpreadsheetPanel extends javax.swing.JScrollPane implements ItemSelectable
{

    GridColumnLegend columnNames;
    GridRowLegend rowNames;
    LabelGrid grid;

    /**
     * Constructs a new scrollable SpreadsheetPanel consisting of 99 x 26
     * selectable cells with each row labeled 1-99 and columns labeled A-Z
     */
    public SpreadsheetPanel()
    {
        super();

        // base sizing off of label in order to account for system font sizes
        JLabel example = new JLabel("0000000000");
        int width = example.getPreferredSize().width;
        int height = example.getPreferredSize().height;

        columnNames = new GridColumnLegend(width, height);
        rowNames = new GridRowLegend(new JLabel("000").getPreferredSize().width, height);
        grid = new LabelGrid(99, 26, width, height);

        initComponents();

        addItemListener(new SpreadsheetPanelSelectItemListener());
    }

    /**
     * Initializes the components of this SpreadsheetPanel and sets their
     * parameters
     */
    private void initComponents()
    {
        setViewportView(grid);
        setColumnHeaderView(columnNames);
        setRowHeaderView(rowNames);

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setUnitIncrement(10); //changes how fast scroll bar moves
    }

    /**
     * Returns the zero indexed row of the currently selected cell
     */
    public int getSelectionRow()
    {
        return grid.getSelectionRow();
    }

    /**
     * Returns the zero indexed column of the currently selected cell
     */
    public int getSelectionColumn()
    {
        return grid.getSelectionColumn();
    }

    /**
     * Sets the text of the cell located at the specified zero indexed row and
     * col to value
     *
     * @param row zero indexed row of the cell to be set to value
     * @param col zero indexed column of the cell to be set to value
     * @param value the text to be set as the value of the specified cell
     * @return true if the cell was updated, else returns false
     */
    public boolean setValue(int row, int col, String value)
    {
        return grid.setValue(row, col, value);
    }

    /**
     * Returns true if a cell at the specified zero indexed row and column is in
     * this panel, else returns false
     */
    public boolean inRange(int row, int col)
    {
        return grid.inRange(row, col);
    }

    /**
     * Sets the cell at the specified zero indexed row and column as selected
     */
    public boolean setSelection(int row, int col)
    {
        return grid.setSelection(row, col) && columnNames.setSelection(col) && rowNames.setSelection(row);
    }

    /**
     * Returns the currently selected JLabel
     */
    @Override
    public Object[] getSelectedObjects()
    {
        return grid.getSelectedObjects();
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
        grid.addItemListener(arg0);
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
        grid.removeItemListener(arg0);
    }

    /**
     * An ItemListener that sets the column and row headers as selected when an
     * item in this sheet is selected
     */
    private class SpreadsheetPanelSelectItemListener implements ItemListener
    {

        @Override
        public void itemStateChanged(ItemEvent arg0)
        {
            if (arg0.getStateChange() == ItemEvent.SELECTED)
            {
                columnNames.setSelection(getSelectionColumn());
                rowNames.setSelection(getSelectionRow());
            }
        }
    }

}
