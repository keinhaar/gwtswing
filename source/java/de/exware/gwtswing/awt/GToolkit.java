package de.exware.gwtswing.awt;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.GPWindow;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GKeyEvent;
import de.exware.gwtswing.awt.event.GKeyListener;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;
import de.exware.gwtswing.swing.GComponent;

public class GToolkit
{
    private static GToolkit instance = new GToolkit();
    private List<GAWTEventListener> awtListeners;
    private BaseComponent rootcomp;
    private BaseMouseListener baseListener;
    
    private GToolkit()
    {
    }
    
    public static GToolkit getDefaultToolkit()
    {
        return instance;
    }
    
    public void addAWTEventListener(GAWTEventListener listener, long eventMask)
    {
        if(awtListeners == null)
        {
            awtListeners = new ArrayList<>();
            rootcomp = new BaseComponent();
        }
        if(awtListeners.size() == 0)
        {
            baseListener = new BaseMouseListener();
            rootcomp.addMouseListener(baseListener);
            rootcomp.addMouseMotionListener(baseListener);
            rootcomp.addKeyListener(baseListener);
            rootcomp.addTouchListener(baseListener);
        }
        if(awtListeners.contains(listener) == false)
        {
            awtListeners.add(listener);
        }
    }

    /**
     * Does not really return the Screen Size, but the Size of the "virtual" Screen - the Browsers Window.
     * @return the Size of the Browsers Window without menu, toolbar, address bar a.s.o.
     */
    public GDimension getScreenSize() 
    {
        GPWindow window = GPlatform.getWin();
        return new GDimension(window.getClientWidth(), window.getClientHeight());
    }
    
    public void removeAWTEventListener(GAWTEventListener listener)
    {
        if(awtListeners != null)
        {
            awtListeners.remove(listener);
            if(awtListeners.size() == 0)
            {
                rootcomp.removeMouseListener(baseListener);
                rootcomp.removeMouseMotionListener(baseListener);
                rootcomp.removeKeyListener(baseListener);
                rootcomp.removeTouchListener(baseListener);
                baseListener = null;
            }
        }
    }
    
    public void handleAWTEvent(GAWTEvent evt)
    {
        if(awtListeners != null)
        {
            List<GAWTEventListener> listeners = new ArrayList<>(awtListeners);
            for(int i=0;i<listeners.size();i++)
            {
                listeners.get(i).eventDispatched(evt);
            }
        }
    }

    /**
     * Component on Body Element to catch all Events, that aren't processed by any other component. 
     * @author martin
     *
     */
    class BaseComponent extends GComponent
    {
        public BaseComponent()
        {
            setPeer(GPlatform.getDoc().getBody());
        }
        
        @Override
        public void handleEvent(GAWTEvent event)
        {
            super.handleEvent(event);
        }
    }

    /**
     * Dummy Listener, just to anable event handling on Body.
     */
    class BaseMouseListener extends GMouseAdapter
        implements GMouseMotionListener, GKeyListener, GTouchListener
    {
        @Override
        public void mouseMoved(GMouseEvent e)
        {
        }
    
        @Override
        public void keyReleased(GKeyEvent evt)
        {
        }
    
        @Override
        public void keyPressed(GKeyEvent keyEvent)
        {
        }
    
        @Override
        public void keyTyped(GKeyEvent keyEvent)
        {
        }

        @Override
        public void touchStart(GTouchEvent evt)
        {
        }

        @Override
        public void touchEnd(GTouchEvent evt)
        {
        }

        @Override
        public void touchMove(GTouchEvent evt)
        {
        }
    }
}
