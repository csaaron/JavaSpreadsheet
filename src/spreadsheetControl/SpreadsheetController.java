/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetControl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import spreadsheet.AbstractSpreadsheet;
import spreadsheet.InvalidNameException;
import spreadsheet.CircularException;
import spreadsheet.SpreadsheetReadWriteException;
import spreadsheetGUI.ISpreadsheetWindow;
import spreadsheetGUI.SpreadsheetPanel;

/**
 * Controller for the spreadsheet gui. Contains reference to view and model.
 */
public class SpreadsheetController
{

    private AbstractSpreadsheet sheet; // reference to the model
    private ISpreadsheetWindow window; // referrence to the view (gui)
    
    private CellValidator validator; // a precompiled default validator for the model
    private CellNormalizer normalizer; // a precompiled default normalizer for the model

    public SpreadsheetController(AbstractSpreadsheet model, ISpreadsheetWindow view)
    {
        sheet = model;
        window = view;

        validator = new CellValidator();
        normalizer = new CellNormalizer();
    }

    public void initView()
    {
        // set the window name at the top of the form
        window.setWindowText("untitled.sprd");

        window.getSpreadsheetPanel().setSelection(0, 0);
        updateCurrentCellBoxes();

    }

    public void initController()
    {

    }

    /**
     * Gets the currently selected cell's zero indexed row and column and sets
     * the current cell text to the normalized cell name
     */
    private void displayCurrentCellName(SpreadsheetPanel ss)
    {
        int row, col;
        row = ss.getSelectionRow();
        col = ss.getSelectionColumn();
        window.setCurrentCellText(convertRowColToCellName(row, col));
    }

    /**
     * Takes in the zero indexed row and col and converts it to the string
     * representation of a cell name
     */
    private String convertRowColToCellName(int row, int col)
    {
        int rowName = row + 1;
        String colName = String.valueOf(col + (int) 'A');
        return colName + rowName;
    }

    /**
     * Creates a new sheet
     */
    private void OpenNewSheet()
    {
        window.createNew();
    }

    /**
     * Takes in a valid cell name and returns the equivalent zero indexed row
     */
    private int convertCellNameToRow(String cellName)
    {
        String stringRow = cellName.substring(1);
        int row = Integer.parseInt(stringRow);
        return row - 1;
    }

    /**
     * Takes in a valid cell name and returns the equivalent zero indexed column
     */
    private int convertCellNameToColumn(String cellName)
    {
        return (int) cellName.charAt(0) - (int) 'A';
    }

    /**
     * Replaces the current value text box to the looked-up value of the cell
     */
    private void setCellValueBox(SpreadsheetPanel panel)
    {
        // locate the current cell in the grid
        int row = panel.getSelectionRow();
        int col = panel.getSelectionColumn();
        String cellName = convertRowColToCellName(row, col);

        // set the value of the cell. Value will be string or double, else will 
        // be the string "FormulaError"
        try
        {
            Object value = sheet.getCellValue(cellName);
            if (value instanceof String || value instanceof Double)
            {
                window.setValueBoxText(value.toString());
            } else
            {
                window.setValueBoxText("FormulaError");
            }
        }
        catch (InvalidNameException e)
        {
            window.setValueBoxText("FormulaError");
        }

    }

    /**
     * Gets the contents of a given cell from the model and places it in the
     * current cell contents box for updating
     */
    private void setCellContentsBox(SpreadsheetPanel panel)
    {
        // locate the current cell in the grid and convert to a variable
        int row = panel.getSelectionRow();
        int col = panel.getSelectionColumn();
        String cellName = convertRowColToCellName(row, col);

        try
        {
            //set the contents text to the current contents of the cell
            Object contents = sheet.getCellContents(cellName);

            if (contents instanceof String || contents instanceof Double)
            {
                window.setValueBoxText(contents.toString());
            } else
            {
                window.setValueBoxText("FormulaError");
            }
        }
        catch (InvalidNameException ex)
        {
            window.setValueBoxText("FormulaError");
        }
    }

    /**
     * Gets the current text in the current cell contents box sets it to the
     * model's cell contents and updates the view cell value. If an error
     * occurs, creates a message box with the message that an error occured.
     */
    private void setCellContentsFromContentsBox()
    {
        // locate the current cell in the grid and convert to a variable
        int row = window.getCellSelectionRow();
        int col = window.getCellSelectionColumn();
        String cellName = convertRowColToCellName(row, col);

        try
        {
            Set<String> cellsToUpdate = sheet.setContentsOfCell(cellName, window.getContentsBoxText());
            setSpreadsheetPanelValues(cellsToUpdate);
            updateCurrentCellBoxes();
        }
        catch (CircularException e)
        {
            window.showErrorMessageBox("Circular dependency detected");
        }
        catch (InvalidNameException e)
        {
            window.showErrorMessageBox(e.getMessage());
        }

        window.setFocusToContentBox();
    }

    /**
     * Updates text boxes at the top of a spreadsheet window
     */
    private void updateCurrentCellBoxes()
    {
        SpreadsheetPanel panel = window.getSpreadsheetPanel();
        displayCurrentCellName(panel);
        setCellValueBox(panel);
        setCellContentsBox(panel);
    }

    /**
     * Takes a set of cell names, looks up their values then sets the
     * SpreadsheetPanel text for those cells to that value
     */
    private void setSpreadsheetPanelValues(Iterable<String> cellsToUpdate) throws InvalidNameException
    {
        for (String cell : cellsToUpdate)
        {
            setSpreadsheetPanelValue(cell);
        }
    }

    /**
     * takes in a cell name, looks up its value and sets the value to the
     * corresponding cell in the view
     */
    private void setSpreadsheetPanelValue(String cell) throws InvalidNameException
    {
        Object value = sheet.getCellValue(cell);
        int row = convertCellNameToRow(cell);
        int col = convertCellNameToColumn(cell);

        // if value is a string or double, set the cell text, else cell text 
        // should be "FormulaError"
        if (value instanceof String || value instanceof Double)
        {
            window.setCellText(row, col, value.toString());
        } 
        else
        {
            window.setCellText(row, col, "FormulaError");
        }
    }
    
    /**
     * Helper method for OpenSpreadsheetFromFile
     * Empties the contents of the spreadsheet pane
     */
    private void emptyAllCells(Iterable<String> cellsToEmpty)
    {
        for (String cell : cellsToEmpty)
        {
            int row = convertCellNameToRow(cell);
            int col = convertCellNameToColumn(cell);
            window.setCellText(row, col, "");
        }
    }
    
    /**
     * Empties the spreadsheet pane and sets its contents to the new spreadsheet model fileLocation
     */
    private void openSpreadsheetFromFile(String fileLocation)
    {
        
        
        try
        {
            // open the spreadsheet
            AbstractSpreadsheet newSheet = new spreadsheet.Spreadsheet(fileLocation, validator, normalizer, "ps6");
            
            // Opening new spreadsheet did not throw exception
            sheet = newSheet;
            
            // empty the sheet
            emptyAllCells(sheet.getNamesOfAllNonemptyCells());
        
            // window title is name of new file 
            String fileName = Paths.get(fileLocation).getFileName().toString();
            window.setWindowText(fileName);
            
            // set contents of the spreadsheet pane
            Iterable<String> nonEmpty = sheet.getNamesOfAllNonemptyCells();
            setSpreadsheetPanelValues(nonEmpty);
            
            // update the current window selection
            window.setCellSelectionToDefault();
            updateCurrentCellBoxes();
        }
        catch (SpreadsheetReadWriteException | InvalidNameException ex)
        {
            window.showErrorMessageBox(ex.getMessage());
        }
        
    }
    
    /**
     * Opens the about file in the default text editor
     */
    private void openAbout()
    {
        // TODO: complete method
    }
    
    /**
     * Opens the how to use file in the default text editor
     */
    private void howToUse()
    {
        // TODO: complete method
    }
    
    /**
     * Opens a save file dialogue and saves the model to a file
     */
    private void save()
    {
        // TODO: complete method
    }
    
    /**
     * Opens a file dialogue box and opens the chosen file in this window.
     * If information will be changed, prompts user to save.
     */
    private void open()
    {
        // TODO: complete method
    }
    
    /**
     * Dialogue box that prompts the user to save current spreadsheet
     */
    private void modifiedSpreadsheetDialogueBox()
    {
        if (sheet.getChanged())
        {
            // prompt to save
            StringBuilder message = new StringBuilder();
            message.append("Unsaved changes detected in current spreadsheet " + window.getWindowText());
            message.append("\n\nSave changes?");
            String caption = "Save Changes?";
            boolean save = window.showOkayCancelMessageBox(message.toString(), caption);
            
            // if user clicks on save then save the changes
            if (save)
            {
                save();
            }
            
        }
    }
}   

