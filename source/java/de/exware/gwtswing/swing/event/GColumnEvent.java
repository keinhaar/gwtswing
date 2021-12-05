package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.util.GEventObject;

public class GColumnEvent
    extends GEventObject
{
    private int oldWidth;
    private int newWidth;
    private int column;
    
    public GColumnEvent(Object source, int column, int oldWidth, int newWidth)
    {
        super(source);
        this.oldWidth = oldWidth;
        this.newWidth = newWidth;
        this.column = column;
    }

    public int getOldWidth()
    {
        return oldWidth;
    }

    public int getNewWidth()
    {
        return newWidth;
    }

    public int getColumn()
    {
        return column;
    }
}
