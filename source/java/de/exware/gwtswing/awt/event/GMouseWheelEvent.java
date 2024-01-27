package de.exware.gwtswing.awt.event;

import de.exware.gplatform.event.GPEvent;

public class GMouseWheelEvent extends GMouseEvent
{
    private int amount;
    
    public GMouseWheelEvent(Object source, int x, int y, int clickcount, int button)
    {
        super(source, x, y, clickcount, button);
    }
    
    public GMouseWheelEvent(Object source, GPEvent jsEvent)
    {
        this(source, jsEvent, 0);
    }
    
    public GMouseWheelEvent(Object source, GPEvent jsEvent, int clickCount)
    {
        this(source, jsEvent, clickCount, GMouseEvent.NOBUTTON);
    }
    
    public GMouseWheelEvent(Object source, GPEvent jsEvent, int clickCount, int button)
    {        
        super(source, jsEvent, true, clickCount, button);
        amount = jsEvent.getMouseWheelVelocityY();
        if(jsEvent.getType() == GPEvent.Type.ONMOUSEWHEEL)
        {
            setId(MOUSE_WHEEL);
        }
    }

    public int getAmount()
    {
        return amount;
    }

}
