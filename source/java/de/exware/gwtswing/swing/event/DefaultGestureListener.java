package de.exware.gwtswing.swing.event;

import de.exware.gplatform.log.Log;
import de.exware.gplatform.log.LogFactory;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GUtilities;

public class DefaultGestureListener implements GTouchListener
{
    private Log LOG = LogFactory.getLog(DefaultGestureListener.class);
    private GPoint startPoint;
    private GPoint lastPoint;
    private GPoint[] startPoints;
    private boolean isScaling;
    private boolean isSwiping;
    private boolean compensateScrolling = true;
    private GPoint startLocation;
    
    public enum SwipeDirection
    {
        UP
        , DOWN
        , LEFT
        , RIGHT
    }
    
    public DefaultGestureListener()
    {
        LOG.debug("DefaultGestureListener created");
    }
    
    public GPoint getStartPoint()
    {
        return startPoint;
    }
    
    @Override
    public void touchStart(GTouchEvent evt)
    {
        isScaling = false;
        startPoints = null;
        startPoint = evt.getPoints()[0];
        startLocation = ((GComponent)evt.getSource()).getLocationOnScreen();
//        evt.consume();
    }

    @Override
    public void touchEnd(GTouchEvent evt)
    {
        if(evt.getPoints().length == 0)
        {
            GPoint start = startPoint;
            GPoint end = new GPoint(lastPoint.x, lastPoint.y);
            if(compensateScrolling)
            {
                GPoint endLocation = ((GComponent)evt.getSource()).getLocationOnScreen();
                int diffX = endLocation.x - startLocation.x;
                int diffY = endLocation.y - startLocation.y;
                end.x += diffX; 
                end.y += diffY; 
            }
            if(isSwiping && GUtilities.getDistance(start, end) > 10)
            {
                double x = end.getX() - start.getX();
                double y = end.getY() - start.getY();
                double absX = Math.abs(x);
                double absY = Math.abs(y);
                SwipeDirection direction = null;
                if(absX > absY)
                {
                    direction = x > 0 ? SwipeDirection.RIGHT : SwipeDirection.LEFT;
                }
                else
                {
                    direction = y > 0 ? SwipeDirection.DOWN : SwipeDirection.UP;
                }
                boolean consume = onSwipe(start, end , direction);
                if(consume)
                {
                    evt.consume();
                }
            }
            else if(isScaling)
            {
            }
            else
            {
                boolean consume = onClick(startPoint);
                if(consume)
                {
                    evt.consume();
                }
            }
            isScaling = false;
            isSwiping = false;
            startPoint = null;
            startPoints = null;
        }
    }

    @Override
    public void touchMove(GTouchEvent evt)
    {
        GPoint[] points = evt.getPoints();
        if( ! isScaling && points.length == 1 && points[0] != startPoint)
            //Is it a Swipe Gesture?
        {
            if(isSwiping == false && startPoint != null && GUtilities.getDistance(startPoint, points[0]) > 5)
            {
                isSwiping = true;
            }
            if(isSwiping)
            {
                lastPoint = points[0];
            }
        }
        if( ! isSwiping && points.length == 2)
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
                GPoint s1 = startPoints[0];
                GPoint s2 = startPoints[1];
                double cx = s1.getX() + (s2.getX() - s1.getX())/2;
                double cy = s1.getY() + (s2.getY() - s1.getY())/2;
                GPoint center = new GPoint((int)cx, (int)cy);
                double scale = distance2 / distance1;
                onScale(center, scale);
                evt.consume();
            }
        }
    }

    public boolean onSwipe(GPoint start, GPoint end, SwipeDirection direction)
    {
        return true;
    }
    
    public void onScale(GPoint center, double scale)
    {
    }
    
    public boolean onClick(GPoint startPoint)
    {
        return false;
    }

    protected boolean isScaling()
    {
        return isScaling;
    }

    protected boolean isSwiping()
    {
        return isSwiping;
    }
}
