package de.exware.gwtswing.swing.event;

import de.exware.gplatform.log.Log;
import de.exware.gplatform.log.LogFactory;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;
import de.exware.gwtswing.swing.GUtilities;

public class DefaultGestureListener implements GTouchListener
{
    private Log LOG = LogFactory.getLog(DefaultGestureListener.class);
    private GPoint startPoint;
    private GPoint lastPoint;
    private GPoint[] startPoints;
    private boolean isScaling;
    private boolean isSwiping;
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
        evt.consume();
    }

    @Override
    public void touchEnd(GTouchEvent evt)
    {
        if(evt.getPoints().length == 0)
        {
//            LOG.debug("Swipe/Scale: " + isSwiping + "/" + isScaling);
            GPoint a = startPoint;
            GPoint b = lastPoint;
            if(isSwiping && GUtilities.getDistance(startPoint, lastPoint) > 5)
            {
                double x = b.getX() - a.getX();
                double y = b.getY() - a.getY();
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
                onSwipe(a, b , direction);
//                LOG.debug("Swipe END: " + direction.name());
                evt.consume();
            }
            else if(isScaling)
            {
//                LOG.debug("Scale END: ");
            }
            else
            {
                onClick(startPoint);
//                LOG.debug("onClick ");
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

    public void onSwipe(GPoint start, GPoint end, SwipeDirection direction)
    {
        
    }
    
    public void onScale(GPoint center, double scale)
    {
    }
    
    public void onClick(GPoint startPoint)
    {
        
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
