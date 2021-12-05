package de.exware.gwtswing.awt.event;

public interface GMouseListener
{
    public void mouseEntered(GMouseEvent evt);
    public void mouseExited(GMouseEvent evt);
    public void mouseClicked(GMouseEvent evt);
    /**
     * Event thrown if the mouseClick was longer then 500ms.
     * @param evt
     */
    public void mouseClickedLong(GMouseEvent evt);
    public void mousePressed(GMouseEvent evt);
    public void mouseReleased(GMouseEvent evt);
}
