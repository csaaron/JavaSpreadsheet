package spreadsheetGUI;

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.SwingUtilities;

/**
 * A mouse listener that redispatches mouse events to parent swing containers
 */
public class RedispatchMouseListener implements MouseListener
{

    @Override
    public void mouseClicked(MouseEvent e)
    {
        redispatchToParent(e);
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
        redispatchToParent(e);
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        redispatchToParent(e);
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        redispatchToParent(e);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        redispatchToParent(e);
    }

    private void redispatchToParent(MouseEvent e)
    {
        Component source = (Component) e.getSource();
        MouseEvent parentEvent = SwingUtilities.convertMouseEvent(source, e, source.getParent());
        source.getParent().dispatchEvent(parentEvent);
    }

}
