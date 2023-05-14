package de.exware.gwtswing.awt.event;

import java.util.List;

import de.exware.gplatform.event.GPEvent;
import de.exware.gplatform.event.GPTouch;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.swing.GComponent;

public class GTouchEvent extends GAWTEvent
{
    private GPoint[] points;
    private GPoint[] changedPoints;
    
    public GTouchEvent(Object source, GPEvent jsEvent)
    {
        super(source, jsEvent); 
        GComponent c = (GComponent) source;
        GPoint loc = c.getLocationOnScreen();
        GInsets insets = c.getInsets();
        List<GPTouch> touches = jsEvent.getTouches();
        points = new GPoint[touches.size()];
        for(int i=0;i<points.length;i++)
        {
            GPTouch t = touches.get(i);
            int x = t.getClientX() - loc.x - insets.left;
            int y = t.getClientY() - loc.y - insets.top;
            points[i] = new GPoint(x, y);
        }
        touches = jsEvent.getChangedTouches();
        changedPoints = new GPoint[touches.size()];
        for(int i=0;i<changedPoints.length;i++)
        {
            GPTouch t = touches.get(i);
            int x = t.getClientX() - loc.x - insets.left;
            int y = t.getClientY() - loc.y - insets.top;
            changedPoints[i] = new GPoint(x, y);
        }
    }

    public GPoint[] getPoints()
    {
        return points;
    }

    public GPoint[] getChangedPoints()
    {
        return changedPoints;
    }

}
