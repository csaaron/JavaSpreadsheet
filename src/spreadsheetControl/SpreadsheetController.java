package spreadsheetControl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import spreadsheet.InvalidNameException;
import spreadsheet.Spreadsheet;

import spreadsheet.CircularException;
import spreadsheet.SpreadsheetReadWriteException;
import spreadsheetGUI.ISpreadsheetWindow;
import spreadsheetGUI.SpreadsheetPanel;
import ssUtils.FormulaFormatException;

/**
 * Controller for the spreadsheet gui. Contains reference to view and model.
 */
public class SpreadsheetController
{

    private Spreadsheet sheet; // reference to the model
    private ISpreadsheetWindow window; // referrence to the view (gui)

    private String file; // holds name of file for updating spreadsheet window text

    public SpreadsheetController(ISpreadsheetWindow view)
    {
        sheet = new Spreadsheet(new CellValidator(), new CellNormalizer(), "ps6");
        window = view;

        initView();
        initController();
    }

    private void initView()
    {

        // set the window name at the top of the form
        window.setFocusToContentBox();
        setWindowText("untitled.sprd", true);
        window.getSpreadsheetPanel().setSelection(0, 0);
        window.getFileChooser().setFileFilter(new SpreadsheetFileFilter());

        updateCurrentCellBoxes();
    }

    private void initController()
    {
        window.getSpreadsheetPanel().addItemListener(new SpreadsheetSelectedCellItemListener());
        window.addActionListenerToEnterContentsButton(new SpreadsheetAddContentsToCellActionListener());
        window.addActionListenerToContentsBox(new SpreadsheetAddContentsToCellActionListener());
        window.addActionListenerToOpenMenuItem(new SpreadsheetOpenActionListener());
        window.addActionListenerToSaveMenuItem(new SpreadsheetSaveActionListener());
        window.addActionListenerToNewMenuItem(new SpreadsheetNewActionListener());
        window.addFormClosingAction(new SpreadsheetCloseWindowListener());
        window.addActionListenerToCloseMenuItem(new SpreadsheetCloseActionListener());
        window.addActionListenerToAboutMenuItem(new SpreadsheetAboutActionListener());
        window.addActionListenerToHowToUseMenuItem(new SpreadsheetHowToUSeActionListener());
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
        String colName = Character.toString((char) (col + (int) 'A'));

        return colName + rowName;
    }

    /**
     * Creates a new sheet
     */
    private void openNewSheet()
    {

        // empty the sheet
        emptyAllCells(sheet.getNamesOfAllNonemptyCells());
        // open the spreadsheet
        sheet = new spreadsheet.Spreadsheet(new CellValidator(), new CellNormalizer(), "ps6");
        initView();

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
            }
            else
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
                window.setContentsBoxText(contents.toString());
            }
            else
            {
                window.setContentsBoxText("=" + contents.toString());
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
     * occurs, creates a message box with the message that an error occurred.
     */
    private void setCellContentsFromContentsBox()
    {
        // locate the current cell in the grid and convert to a variable
        int row = window.getCellSelectionRow();
        int col = window.getCellSelectionColumn();
        String cellName = convertRowColToCellName(row, col);

        try
        {
            Set<String> cellsToUpdate = sheet.setContentsOfCell(cellName, window.getContentsBoxText().trim());
            setSpreadsheetPanelValues(cellsToUpdate);
            updateCurrentCellBoxes();
        }
        catch (CircularException e)
        {
            window.showErrorMessageBox("Circular dependency detected");
            //window.showErrorMessageBox("Cell name: " + cellName + "\n Cell Contents: " + sheet.getCellContents(cellName));

        }
        catch (InvalidNameException | FormulaFormatException e)
        {
            window.showErrorMessageBox(e.getMessage());
            //window.showErrorMessageBox("Cell name: " + cellName + "\n Cell Contents: " + sheet.getCellContents(cellName));
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
    private void setSpreadsheetPanelValues(Iterable<String> cellsToUpdate)
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
    private void setSpreadsheetPanelValue(String cell)
    {
        Object value;
        try
        {
            value = sheet.getCellValue(cell);

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
        catch (InvalidNameException ex)
        {
            window.showErrorMessageBox(ex.getMessage());
        }
    }

    /**
     * Helper method for OpenSpreadsheetFromFile Empties the contents of the
     * spreadsheet pane
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
     * Empties the spreadsheet pane and sets its contents to the new spreadsheet
     * model fileLocation
     */
    private void openSpreadsheetFromFile(String fileLocation)
    {

        try
        {
            // open the spreadsheet
            Spreadsheet newSheet = new spreadsheet.Spreadsheet(fileLocation, new CellValidator(), new CellNormalizer(), "ps6");

            // Opening new spreadsheet did not throw exception
            Spreadsheet oldSheet = sheet;
            sheet = newSheet;

            // empty the sheet
            emptyAllCells(oldSheet.getNamesOfAllNonemptyCells());

            // window title is name of new file 
            String fileName = Paths.get(fileLocation).getFileName().toString();
            setWindowText(fileName, false);

            // set contents of the spreadsheet pane
            Iterable<String> nonEmpty = sheet.getNamesOfAllNonemptyCells();
            setSpreadsheetPanelValues(nonEmpty);

            // update the current window selection
            window.setCellSelectionToDefault();
            updateCurrentCellBoxes();
        }
        catch (SpreadsheetReadWriteException ex)
        {
            window.showErrorMessageBox(ex.getMessage());

        }

    }

    /**
     * Opens the about file in the default text editor
     */
    private void openAboutDiolog()
    {
        //read file contents
        String aboutFile = "resources/About.html";
        String aboutFileText = readFileToString(aboutFile);
        
        // place text in popup
        window.showOkayMessageBox(aboutFileText, "About");
    }

    /**
     * Opens the how to use file in the default text editor
     */
    private void openHowToUseDiolog()
    {
        // read file contents
        String howToUseFile = "resources/HowToUse.html";
        String howToUseFileText = readFileToString(howToUseFile);
        
        // place text in popup
        window.showOkayMessageBox(howToUseFileText, "How to Use");
        
    }

    
    private String readFileToString(String filePath)
    {
        StringBuilder contents = new StringBuilder();
        try (Scanner scan = new Scanner(new File(filePath)))
        {
            while (scan.hasNextLine())
            {
                contents.append(scan.nextLine());
            }
        }
        catch (FileNotFoundException ex)
        {
            window.showErrorMessageBox("Error reading the file \"" + filePath + "\"");
        }
        
        return contents.toString();
    }
    
    /**
     * Saves sheet to file
     */
    private boolean save(String fileName)
    {
        if (fileName != null && !fileName.equals(""))
        {
            try
            {
                sheet.save(fileName);
                return true;
            }
            catch (SpreadsheetReadWriteException ex)
            {
                window.showErrorMessageBox(ex.getMessage());
                return false;
            }
        }

        return false;
    }

    /**
     * Opens a file dialogue box and opens the chosen file in this window. If
     * information will be changed, prompts user to save.
     */
    private void open()
    {
        String fileName = window.showOpenFileDialogue();
        if (fileName != null && !fileName.trim().equals(""))
        {
            openSpreadsheetFromFile(fileName);
        }
    }

    /**
     * Shows dialogue box that prompts user to save any unsaved changes
     *
     * Returns false if no further action should be made(cancel), else returns
     * true
     */
    private boolean modifiedSpreadsheetDialogueBox()
    {

        // prompt to save
        StringBuilder message = new StringBuilder();
        message.append("Unsaved changes detected in current spreadsheet " + window.getWindowText());
        message.append("\n\nSave changes?");
        String caption = "Save Changes?";
        int save = window.showOkayCancelMessageBox(message.toString(), caption);

        // cancel selected, do nothing and return false
        if (save == JOptionPane.CANCEL_OPTION)
        {
            return false;
        }

        // yes selected, show file choser, save file and return true.
        // if save operation canceled, return false 
        if (save == JOptionPane.YES_OPTION)
        {
            String file = window.showSaveFileDialogue();
            if (file != null && !file.trim().equals(""))
            {
                if (save(file))
                {
                    setWindowText(getFileNameFromPath(file), false);
                    return true;
                }
            }

            return false;

        }

        // user does not wish to save and does not want to cancel action, do nothing and return true
        if (save == JOptionPane.NO_OPTION)
        {
            return true;
        }

        return false;

    }

    private String getFileNameFromPath(String file)
    {
        if (file != null && !file.trim().equals(""))
        {
            int fileNamePosition = file.lastIndexOf('/');

            if (fileNamePosition > 0 && fileNamePosition < file.length() - 1)
            {
                return file.substring(fileNamePosition + 1);
            }
            else
            {
                return file;
            }
        }

        return file;
    }

    private void setWindowText(String fileName, boolean changed)
    {
        String text = changed ? fileName + " | Unsaved" : fileName + " | Saved";
        file = fileName;
        window.setWindowText(text);
    }

    private class SpreadsheetSelectedCellItemListener implements ItemListener
    {

        @Override
        public void itemStateChanged(ItemEvent arg0)
        {
            updateCurrentCellBoxes();
        }

    }

    private class SpreadsheetAddContentsToCellActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            setCellContentsFromContentsBox();
            setWindowText(file, sheet.getChanged());
            updateCurrentCellBoxes();
        }

    }

    private class SpreadsheetOpenActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            if (!sheet.getChanged() || modifiedSpreadsheetDialogueBox())
            {
                open();
            }
        }

    }

    private class SpreadsheetSaveActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            String file = window.showSaveFileDialogue();
            if (file != null && !file.trim().equals("") && save(file))
            {
                setWindowText(getFileNameFromPath(file), false);
            }
        }
    }

    private class SpreadsheetNewActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            if (!sheet.getChanged() || modifiedSpreadsheetDialogueBox())
            {
                openNewSheet();
            }
        }

    }

    private class SpreadsheetCloseActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            if (!sheet.getChanged() || modifiedSpreadsheetDialogueBox())
            {
                window.closeWindow();
            }
        }
    }

    private class SpreadsheetCloseWindowListener extends WindowAdapter
    {

        @Override
        public void windowClosing(WindowEvent e)
        {
            if (!sheet.getChanged() || modifiedSpreadsheetDialogueBox())
            {
                window.closeWindow();
            }

        }
    }
    
    private class SpreadsheetAboutActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            openAboutDiolog();
        }
        
    }
    
    private class SpreadsheetHowToUSeActionListener implements ActionListener
    {

        @Override
        public void actionPerformed(ActionEvent arg0)
        {
            openHowToUseDiolog();
        }
        
    }

}
