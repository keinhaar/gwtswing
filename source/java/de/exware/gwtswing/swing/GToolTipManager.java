package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Timer;

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
    private Element popup;
    private long popupTime;
    private MouseListener listener = new MouseListener();
    private Timer showTimer;

    private GToolTipManager()
    {
        popup = Document.get().createDivElement();;
        popup.addClassName("gwts-GComponent");
        popup.addClassName(DEFAULT_TOOLTIP_STYLE);
    }

    private void show(String text, int x, int y)
    {        
        showTimer = new Timer()
        {
            private boolean canceled = false;
            
            @Override
            public void run()
            {
                if(canceled) return;
                showTimer = null;
                popupTime = System.currentTimeMillis();
                popup.setInnerHTML(text);
                popup.getStyle().setLeft(x, Unit.PX);
                popup.getStyle().setTop(y, Unit.PX);
                BodyElement root = Document.get().getBody();
                root.appendChild(popup);
                Timer t = new Timer()
                {
                    @Override
                    public void run()
                    {
                        if (popupTime + delay <= System.currentTimeMillis())
                        {
                            popup.removeFromParent();
                        }
                    }
                };
                t.schedule(delay);
            }
            
            @Override
            public void cancel()
            {
                super.cancel();
                canceled = true;
            }
        };
        showTimer.schedule(800);
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
            if(showTimer != null)
            {
                showTimer.cancel();
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
                if(showTimer != null)
                {
                    showTimer.cancel();
                }
                show(text, loc.x + evt.getX() + DEFAULT_OFFSET_X, loc.y + evt.getY() + DEFAULT_OFFSET_Y);
            }
        }
    }
}
