package spreadsheetControl;

import javax.swing.UIManager;
import spreadsheetGUI.ISpreadsheetWindow;
import spreadsheetGUI.SpreadsheetGUI;

/**
 * Entry point for this program
 * 
 * Displays the GUI and attaches it to the controller
 */
public class Program
{
    
    public static void main(String args[])
    {
        /* Set the Nimbus look and feel  to system default */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try
        {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels())
            {
                String systemLandF = UIManager.getSystemLookAndFeelClassName();
                if (systemLandF.equals(info.getClassName()))
                {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        }
        catch (ClassNotFoundException ex)
        {
            java.util.logging.Logger.getLogger(SpreadsheetGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (InstantiationException ex)
        {
            java.util.logging.Logger.getLogger(SpreadsheetGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (IllegalAccessException ex)
        {
            java.util.logging.Logger.getLogger(SpreadsheetGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        catch (javax.swing.UnsupportedLookAndFeelException ex)
        {
            java.util.logging.Logger.getLogger(SpreadsheetGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the GUI and attach it to controller */
        java.awt.EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                SpreadsheetGUI view = new SpreadsheetGUI();
                SpreadsheetController spreadsheetController = new SpreadsheetController((ISpreadsheetWindow)view);
                view.setVisible(true);
            }
        });
    }
}
