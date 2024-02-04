package de.exware.gwtswing.awt.event;

import de.exware.gplatform.event.GPEvent;
import de.exware.gwtswing.util.GEventObject;

public abstract class GAWTEvent extends GEventObject
{
    public final static long COMPONENT_EVENT_MASK = 0x01;
    public final static long CONTAINER_EVENT_MASK = 0x02;
    public final static long FOCUS_EVENT_MASK = 0x04;
    public final static long KEY_EVENT_MASK = 0x08;
    public final static long MOUSE_EVENT_MASK = 0x10;

    private int id;
    protected boolean consumed = false;
    private GPEvent jsEvent;
    
    protected GAWTEvent(Object source)
    {
        super(source);
    }

    protected GAWTEvent(Object source, GPEvent jsEvent)
    {
        super(source);
        this.jsEvent = jsEvent;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
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
