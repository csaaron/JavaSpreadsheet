/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JScrollPane;

/**
 *
 * @author aaron
 */
public class SpreadsheetPanel extends javax.swing.JScrollPane implements MouseListener
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
        
        this.getViewport().getView().addMouseListener(this);
    }

    private void initComponents()
    {
        setViewportView(grid);
        setColumnHeaderView(columnNames);
        setRowHeaderView(rowNames);
        
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    }
    
    private boolean setSelection(int row, int col)
    {
        return grid.setSelection(row,col) && columnNames.setSelection(col) && rowNames.setSelection(row);
    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
        setSelection(grid.getSelectionRow(), grid.getSelectionColumn());
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
