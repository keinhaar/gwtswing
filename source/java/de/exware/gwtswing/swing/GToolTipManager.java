package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gplatform.timer.AbstractGPTimerTask;
import de.exware.gplatform.timer.GPTimer;
import de.exware.gplatform.timer.GPTimerTask;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseMotionListener;

/**
 * Ein besserer Tooltip für GWT. Dieser bietet mehr gestalterische
 * Möglichkeiten.
 */
public class GToolTipManager
{
    private static final String DEFAULT_TOOLTIP_STYLE = "gwts-TooltipPopup";
    private static final int DEFAULT_OFFSET_X = 20;
    private static final int DEFAULT_OFFSET_Y = 0;
    private static GToolTipManager instance = new GToolTipManager();
    private int delay = 3000;
    private GPElement popup;
    private long popupTime;
    private MouseListener listener = new MouseListener();
    private GPTimer timer = GPTimer.createInstance();
    private GPTimerTask task;

    private GToolTipManager()
    {
        popup = GPlatform.getDoc().createDivElement();;
        popup.addClassName("gwts-GComponent");
        popup.addClassName(DEFAULT_TOOLTIP_STYLE);
    }

    private void show(String text, int x, int y)
    {        
        task = new AbstractGPTimerTask()
        {
            @Override
            public void execute()
            {
                if(isCanceled()) return;
                task = null;
                popupTime = System.currentTimeMillis();
                popup.setInnerHTML(text);
                popup.getStyle().setLeft(x);
                popup.getStyle().setTop(y);
                GPElement root = GPlatform.getDoc().getBody();
                root.appendChild(popup);
                GPTimerTask t = new AbstractGPTimerTask()
                {
                    @Override
                    public void execute()
                    {
                        if (popupTime + delay <= System.currentTimeMillis())
                        {
                            popup.removeFromParent();
                        }
                    }
                };
                timer.schedule(t, delay);
            }
        };
        timer.schedule(task, 800);
    }

    public static GToolTipManager getInstance()
    {
        return instance;
    }

    public void registerComponent(final GComponent comp)
    {
        comp.addMouseListener(listener);
        comp.addMouseMotionListener(listener);
    }
    
    public void unregisterComponent(final GComponent comp)
    {
        comp.removeMouseListener(listener);
        comp.removeMouseMotionListener(listener);
    }
    
    class MouseListener extends GMouseAdapter
        implements GMouseMotionListener
    {
        @Override
        public void mouseEntered(GMouseEvent evt)
        {
        }
        
        @Override
        public void mouseExited(GMouseEvent evt)
        {
            super.mouseExited(evt);
            popup.removeFromParent();
            if(task != null)
            {
                task.cancel();
            }
        }

        @Override
        public void mouseMoved(GMouseEvent evt)
        {
            GComponent comp = (GComponent)evt.getSource();
//            if(comp.getClass().getName().contains("TimeFi"))
//            {
//                System.out.println();
//            }
            String text = comp.getToolTipText(evt.getX(), evt.getY());
            if(text != null)
            {
                GPoint loc = comp.getLocationOnScreen();
                if(task != null)
                {
                    task.cancel();
                }
                show(text, loc.x + evt.getX() + DEFAULT_OFFSET_X, loc.y + evt.getY() + DEFAULT_OFFSET_Y);
            }
        }
    }
}
