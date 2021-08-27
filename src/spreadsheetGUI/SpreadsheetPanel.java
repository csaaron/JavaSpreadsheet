/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JScrollPane;

/**
 *
 * @author aaron
 */
public class SpreadsheetPanel extends javax.swing.JScrollPane implements ItemSelectable
{

    private static final int COLUMN_WIDTH = 80;
    private static final int ROW_NUMBER_WIDTH = 20;

    GridColumnLegend columnNames;
    GridRowLegend rowNames;
    LabelGrid grid;

    public SpreadsheetPanel()
    {
        super();
        columnNames = new GridColumnLegend();
        rowNames = new GridRowLegend();
        grid = new LabelGrid();

        initComponents();

        addItemListener(new SpreadsheetPanelSelectItemListener());
    }

    private void initComponents()
    {
        setViewportView(grid);
        setColumnHeaderView(columnNames);
        setRowHeaderView(rowNames);

        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }

    public int getSelectionRow()
    {
        return grid.getSelectionRow();
    }

    public int getSelectionColumn()
    {
        return grid.getSelectionColumn();
    }

    public boolean setValue(int row, int col, String value)
    {
        return grid.setValue(row, col, value);
    }

    public boolean inRange(int row, int col)
    {
        return grid.inRange(row, col);
    }

    public boolean setSelection(int row, int col)
    {
        return grid.setSelection(row, col) && columnNames.setSelection(col) && rowNames.setSelection(row);
    }

    @Override
    public Object[] getSelectedObjects()
    {
        return grid.getSelectedObjects();
    }

    @Override
    public void addItemListener(ItemListener arg0)
    {
        grid.addItemListener(arg0);
    }

    @Override
    public void removeItemListener(ItemListener arg0)
    {
        grid.removeItemListener(arg0);
    }

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
