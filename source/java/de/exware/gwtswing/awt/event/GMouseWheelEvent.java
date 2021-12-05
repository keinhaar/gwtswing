package de.exware.gwtswing.awt.event;

import com.google.gwt.user.client.Event;

public class GMouseWheelEvent extends GMouseEvent
{
    private int amount;
    
    public GMouseWheelEvent(Object source, int x, int y, int clickcount)
    {
        super(source, x, y, clickcount);
    }
    
    public GMouseWheelEvent(Object source, com.google.gwt.user.client.Event jsEvent)
    {
        this(source, jsEvent, 0);
    }
    
    public GMouseWheelEvent(Object source, Event jsEvent, int clickCount)
    {
        this(source, jsEvent, clickCount, GMouseEvent.NOBUTTON);
    }
    
    public GMouseWheelEvent(Object source, Event jsEvent, int clickCount, int button)
    {        
        super(source, jsEvent, clickCount, button);
        amount = jsEvent.getMouseWheelVelocityY();
        if(jsEvent.getTypeInt() == jsEvent.ONMOUSEWHEEL)
        {
            setId(MOUSE_WHEEL);
        }
    }

    public int getAmount()
    {
        return amount;
    }

}
