package de.exware.gwtswing.awt.event;

import com.google.gwt.user.client.Event;

import de.exware.gwtswing.util.GEventObject;

public abstract class GAWTEvent extends GEventObject
{
    private int id;
    protected boolean consumed = false;
    private Event jsEvent;
    
    protected GAWTEvent(Object source)
    {
        super(source);
    }

    protected GAWTEvent(Object source, Event jsEvent)
    {
        super(source);
        this.jsEvent = jsEvent;
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
        if(jsEvent != null)
        {
            jsEvent.preventDefault();
            jsEvent.stopPropagation();
        }
    }
}
