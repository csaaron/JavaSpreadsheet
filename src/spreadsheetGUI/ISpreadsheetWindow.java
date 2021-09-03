/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.event.ActionListener;
import java.io.FileFilter;
import javax.swing.JFileChooser;

public interface ISpreadsheetWindow
{

    /**
     * Gets the spreadsheet panel component in this window
     */
    public SpreadsheetPanel getSpreadsheetPanel();

    /**
     * Sets the text of the current cell text box
     */
    public void setCurrentCellText(String text);

    /**
     * Sets the text of the value test box
     */
    public void setValueBoxText(String text);

    /**
     * Gets the text of the contents text box
     */
    public String getContentsBoxText();

    /**
     * Sets the text of the contents text box
     */
    public void setContentsBoxText(String text);

    /**
     * Sets the text of this window
     */
    public String getWindowText();

    /**
     * Sets the text of this window
     */
    public void setWindowText(String text);

    /**
     * Creates a new window containing an empty spreadsheet
     */
    public void createNew();

    /**
     * Shows an error message box corresponding message
     */
    public void showErrorMessageBox(String message);

    /**
     * Shows a message box which can be canceled with a corresponding message
     * and caption
     */
    public boolean showOkayCancelMessageBox(String message, String caption);

    /**
     * Sets the cell in the SpreadsheetPanel located at row, col to the String
     * text
     */
    public void setCellText(int row, int col, String text);

    /**
     * Gets the zero indexed row of the currently selected cell 
     */
    public int getCellSelectionRow();

    /**
     * Gets the zero indexed column of the currently selected cell 
     */
    public int getCellSelectionColumn();

    /**
     * Allows controller to close this window
     */
    public void closeWindow();
    
    /**
     * Sets the default accept button as contentsButton
     */
    public void addActionListenerToContentsBox(ActionListener l);

    /**
     * Sets the focus to the contents text box
     */
    public void setFocusToContentBox();

    /**
     * Allows the controller to add an action to the FormClosing event
     */
    public void addFormClosingAction();

    /**
     * Sets the default cell as selection in the spreadsheet panel
     */
    public void setCellSelectionToDefault();
    
    /**
     * Adds a listener to the enterContentsButton
     */
    public void addActionListenerToEnterContentsButton(ActionListener l);
    
    /**
     * Shows an open file dialogue box and returns the file selection
     */
    public String showOpenFileDialogue();
    
    /**
     * Shows an close file dialogue box and returns the file selection
     */
    public String showSaveFileDialogue();
    
    /**
     * Adds a listener to the Open menu item
     */
    public void addActionListenerToOpenMenuItem(ActionListener l);
    
    /**
     * Adds a listener to the Open menu item
     */
    public void addActionListenerToSaveMenuItem(ActionListener l);
    
    /**
     * Returns the file chooser used by this window. 
     * 
     * Allows controller to control settings offered by file chooser
     */
    public JFileChooser getFileChooser();
    
}
