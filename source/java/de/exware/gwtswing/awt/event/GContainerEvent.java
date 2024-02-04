package de.exware.gwtswing.awt.event;

import de.exware.gwtswing.swing.GComponent;

public class GContainerEvent extends GAWTEvent
{
    private GComponent comp;
    
    public GContainerEvent(GComponent source, GComponent comp)
    {
        super(source);
        this.comp = comp;
    }
    
    public GComponent getContainer()
    {
        return (GComponent) getSource();
    }
    
    public GComponent getChild()
    {
        return comp;
    }
}
