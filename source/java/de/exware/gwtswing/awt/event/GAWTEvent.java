package de.exware.gwtswing.awt.event;

import de.exware.gwtswing.util.GEventObject;

public abstract class GAWTEvent extends GEventObject
{
    private int id;
    protected boolean consumed = false;
    
    protected GAWTEvent(Object source)
    {
        super(source);
    }

    public int getId()
    {
        return id;
    }

    protected void setId(int id)
    {
        this.id = id;
    }

    public boolean isConsumed()
    {
        return consumed;
    }

    public void consume()
    {
        this.consumed = true;
    }
}
