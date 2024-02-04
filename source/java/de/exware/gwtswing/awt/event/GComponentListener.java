package de.exware.gwtswing.awt.event;

public interface GComponentListener
{
    /**
     * Invoked when the component's size changes.
     */
    public void componentResized(GComponentEvent e);
    
    /**
     * Called if the visibility flag is set to true.
     * Doesn't mean, that the component is actually visible. It just could be visible,
     * if all parents are also visible, and any of them is attached to the ui.
     * @param e
     */
    public void componentShown(GComponentEvent e);

    /**
     * Called if the visibility flag is set to false.
     * @param e
     */
    public void componentHidden(GComponentEvent e);
}
