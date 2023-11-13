package de.exware.gwtswing.swing;

import de.exware.gplatform.timer.AbstractGPTimerTask;
import de.exware.gplatform.timer.GPTimer;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.GWindow;
import de.exware.gwtswing.awt.event.GMouseEvent;

public class GSwingUtilities
{
    public static GWindow getWindowAncestor(GComponent comp)
    {
        GWindow window = null;
        GComponent c = comp.getTopLevelAncestor();
        if(c instanceof GWindow)
        {
            window = (GWindow) c;
        }
        return window;
    }

    public static GPoint convertPoint(GComponent source, int x, int y, GComponent target)
    {
        if(source != target) 
        {
            GPoint p = source.getLocationOnScreen();
            x = p.x + x;
            y = p.y + y;
            p = target.getLocationOnScreen();
            x = x - p.x;
            y = y - p.y;
        }
        return new GPoint(x,y);
    }
    
    public static void invokeLater(final Runnable runner)
    {
        GPTimer timer = GPTimer.createInstance();
        timer.schedule(new AbstractGPTimerTask()
        {
            
            @Override
            public void execute()
            {
                runner.run();
            }
        }, 10);
    }
    
    public static boolean isRightMouseButton(GMouseEvent evt)
    {
        return evt.getButton() == GMouseEvent.BUTTON3;
    }
    
    public static boolean isLeftMouseButton(GMouseEvent evt)
    {
        return evt.getButton() == GMouseEvent.BUTTON1;
    }

    public static GComponent getRoot(GComponent parent)
    {
        return parent.getTopLevelAncestor();
    }
}
