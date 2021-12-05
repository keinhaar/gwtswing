package de.exware.gwtswing.awt.event;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.Touch;
import com.google.gwt.user.client.Event;

import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.swing.GComponent;

public class GTouchEvent extends GAWTEvent
{
    private int x, y;
    
    public GTouchEvent(Object source, Event jsEvent)
    {
        super(source); 
        GComponent c = (GComponent) source;
        GPoint loc = c.getLocationOnScreen();
        GInsets insets = c.getInsets();
        JsArray<Touch> touches = jsEvent.getTouches();
        x = -1;
        y = -1;
        if(touches.length() > 0)
        {
            Touch t = touches.get(0);
            x = t.getClientX() - loc.x - insets.left;
            y = t.getClientY() - loc.y - insets.top;
        }
    }

    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }

    public GPoint getPoint()
    {
        return new GPoint(x, y);
    }
}
