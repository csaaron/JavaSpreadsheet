package spreadsheetGUI;

import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
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
     * Shows an error message box corresponding message
     */
    public void showErrorMessageBox(String message);

    /**
     * Shows a message box which can be canceled with a corresponding message
     * and caption
     */
    public int showYesNoCancelMessageBox(String message, String caption);

    /**
     * Shows a generic message box with the corresponding message and caption
     */
    public void showOkayMessageBox(String message, String caption);

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
     * Closes this window
     */
    public void closeWindow();

    /**
     * Adds ActionListener l to the contents box
     */
    public void addActionListenerToContentsBox(ActionListener l);

    /**
     * Sets the focus to the contents text box
     */
    public void setFocusToContentBox();

    /**
     * Adds WindowListener l to the FormClosing event
     */
    public void addFormClosingAction(WindowListener l);

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
     * Adds a listener to the Save menu item
     */
    public void addActionListenerToSaveMenuItem(ActionListener l);

    /**
     * Adds a listener to the New menu item
     */
    public void addActionListenerToNewMenuItem(ActionListener l);

    /**
     * Adds a listener to the Close menu item l
     */
    public void addActionListenerToCloseMenuItem(ActionListener l);

    /**
     * Adds a listener to the How To Use menu item l
     */
    public void addActionListenerToHowToUseMenuItem(ActionListener l);

    /**
     * Adds a listener to the About menu item l
     */
    public void addActionListenerToAboutMenuItem(ActionListener l);

    /**
     * Returns the file chooser used by this window.
     *
     * Allows controller to control settings offered by file chooser
     */
    public JFileChooser getFileChooser();

}
