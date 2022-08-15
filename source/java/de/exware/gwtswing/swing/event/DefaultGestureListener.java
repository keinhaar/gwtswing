package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;
import de.exware.gwtswing.swing.GUtilities;

public class DefaultGestureListener implements GTouchListener
{
    private GPoint startPoint;
    private GPoint[] startPoints;
    boolean isScaling;
    
    @Override
    public void touchStart(GTouchEvent evt)
    {
        isScaling = false;
        startPoints = null;
        startPoint = evt.getPoints()[0];
    }

    @Override
    public void touchEnd(GTouchEvent evt)
    {
        if(isScaling == false)
        {
            GPoint a = startPoint;
            GPoint b = evt.getPoints()[0];
            double x = b.getX() - a.getX();
            double x2 = x * x;
            double y = b.getY() - a.getY();
            double y2 = y * y;
            onSwipe(a, b);
            evt.consume();
        }
        isScaling = false;
        startPoint = null;
        startPoints = null;
    }

    @Override
    public void touchMove(GTouchEvent evt)
    {
        GPoint[] points = evt.getPoints();
        if(points.length == 2)
            //Is it a Scale Gesture?
        {
            if(isScaling == false)
            {
                isScaling = true;
                startPoints = points;
            }
            if(startPoints.length == 2 && points != startPoints)
            {
                double distance1 = GUtilities.getDistance(startPoints[0], startPoints[1]);
                double distance2 = GUtilities.getDistance(points[0], points[1]);
                double scale = 100 / distance1 * distance2;
                onScale(scale);
                evt.consume();
            }
        }
        else
        {
            isScaling = false;
            startPoints = null;
        }
    }

    public void onSwipe(GPoint start, GPoint end)
    {
        
    }
    
    public void onScale(double scale)
    {
    }
}
