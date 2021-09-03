/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spreadsheetGUI;

import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.UIManager;

/**
 *
 * @author aaron
 */
public class SpreadsheetGUI extends javax.swing.JFrame implements ISpreadsheetWindow
{

    /**
     * Creates new form SpreadsheetGUI
     */
    public SpreadsheetGUI()
    {
        initComponents();

        fileChooser = new JFileChooser();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents()
    {

        jPanel1 = new javax.swing.JPanel();
        currentCellLabel = new javax.swing.JLabel();
        currentCellTextField = new javax.swing.JTextField();
        valueLabel = new javax.swing.JLabel();
        valueTextField = new javax.swing.JTextField();
        contentsLabel = new javax.swing.JLabel();
        contentsTextField = new javax.swing.JTextField();
        contentsButton = new javax.swing.JButton();
        spreadsheetPanel = new spreadsheetGUI.SpreadsheetPanel();
        windowMenuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        closeMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        howToUseMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        currentCellLabel.setText("Current Cell");

        currentCellTextField.setEditable(false);
        currentCellTextField.setColumns(5);

        valueLabel.setText("Value");

        valueTextField.setEditable(false);
        valueTextField.setColumns(15);

        contentsLabel.setText("Contents");

        contentsButton.setText("Enter Contents");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(currentCellLabel)
                    .addComponent(contentsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(currentCellTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valueLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(valueTextField))
                    .addComponent(contentsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(contentsButton)
                .addGap(0, 141, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(currentCellLabel)
                    .addComponent(currentCellTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(valueLabel)
                    .addComponent(valueTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contentsLabel)
                    .addComponent(contentsTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(contentsButton))
                .addContainerGap())
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.NORTH);

        spreadsheetPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        getContentPane().add(spreadsheetPanel, java.awt.BorderLayout.CENTER);

        fileMenu.setText("File");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        newMenuItem.setText("New");
        fileMenu.add(newMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        openMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        closeMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_DOWN_MASK));
        closeMenuItem.setText("Close");
        fileMenu.add(closeMenuItem);

        windowMenuBar.add(fileMenu);

        helpMenu.setText("Help");

        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        howToUseMenuItem.setText("How to use");
        helpMenu.add(howToUseMenuItem);

        windowMenuBar.add(helpMenu);

        setJMenuBar(windowMenuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem closeMenuItem;
    private javax.swing.JButton contentsButton;
    private javax.swing.JLabel contentsLabel;
    private javax.swing.JTextField contentsTextField;
    private javax.swing.JLabel currentCellLabel;
    private javax.swing.JTextField currentCellTextField;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem howToUseMenuItem;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private spreadsheetGUI.SpreadsheetPanel spreadsheetPanel;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JTextField valueTextField;
    private javax.swing.JMenuBar windowMenuBar;
    // End of variables declaration//GEN-END:variables

    private JFileChooser fileChooser;

    @Override
    public SpreadsheetPanel getSpreadsheetPanel()
    {
        return spreadsheetPanel;
    }

    @Override
    public void setCurrentCellText(String text)
    {
        currentCellTextField.setText(text);
    }

    @Override
    public void setValueBoxText(String text)
    {
        valueTextField.setText(text);
    }

    @Override
    public String getContentsBoxText()
    {
        return contentsTextField.getText();
    }

    @Override
    public void setContentsBoxText(String text)
    {
        contentsTextField.setText(text);
    }

    @Override
    public String getWindowText()
    {
        return getTitle();
    }

    @Override
    public void setWindowText(String text)
    {
        setTitle(text);
    }

    @Override
    public void createNew()
    {
        // TODO: Implement method
    }

    @Override
    public void showErrorMessageBox(String message)
    {
        javax.swing.JOptionPane.showMessageDialog(this, message,
                "Spreadsheet Error", javax.swing.JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public boolean showOkayCancelMessageBox(String message, String caption)
    {
        // TODO: Implement method
        return false;
    }

    @Override
    public void setCellText(int row, int col, String text)
    {
        spreadsheetPanel.setValue(row, col, text);
    }

    @Override
    public int getCellSelectionRow()
    {
        return spreadsheetPanel.getSelectionRow();
    }

    @Override
    public int getCellSelectionColumn()
    {
        return spreadsheetPanel.getSelectionColumn();
    }

    @Override
    public void closeWindow()
    {
        // TODO: Complete method
    }

    @Override
    public void addActionListenerToContentsBox(ActionListener l)
    {
        contentsTextField.addActionListener(l);
    }

    @Override
    public void setFocusToContentBox()
    {
        contentsTextField.requestFocus();
    }

    @Override
    public void addFormClosingAction()
    {
        // TODO: complete method
    }

    @Override
    public void setCellSelectionToDefault()
    {
        spreadsheetPanel.setSelection(0, 0);
    }

    @Override
    public void addActionListenerToEnterContentsButton(ActionListener l)
    {
        contentsButton.addActionListener(l);
    }

    @Override
    public String showOpenFileDialogue()
    {
        int returnVal = fileChooser.showOpenDialog(this);
        
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile().toString();
        } 
        else
        {
            return "";
        }
    }

    @Override
    public String showSaveFileDialogue()
    {
        int returnVal = fileChooser.showSaveDialog(this);

        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
            return fileChooser.getSelectedFile().toString();
        } 
        else
        {
            return "";
        }
    }

    @Override
    public void addActionListenerToOpenMenuItem(ActionListener l)
    {
        openMenuItem.addActionListener(l);
    }

    @Override
    public void addActionListenerToSaveMenuItem(ActionListener l)
    {
        saveMenuItem.addActionListener(l);
    }
    
    @Override
    public void addActionListenerToNewMenuItem(ActionListener l)
    {
        newMenuItem.addActionListener(l);
    }

    @Override
    public JFileChooser getFileChooser()
    {
        return fileChooser;
    }

}
