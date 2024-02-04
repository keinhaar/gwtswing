package de.exware.gwtswing.awt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.exware.gplatform.GPWindow;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GComponentEvent;
import de.exware.gwtswing.awt.event.GContainerEvent;
import de.exware.gwtswing.awt.event.GFocusEvent;
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
    private Map<Class, List<GAWTEventListener>> awtListeners;
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
        if((eventMask & GAWTEvent.COMPONENT_EVENT_MASK) > 0)
        {
            addAWTEventListener(listener, GComponentEvent.class);
        }
        if((eventMask & GAWTEvent.CONTAINER_EVENT_MASK) > 0)
        {
            addAWTEventListener(listener, GContainerEvent.class);
        }
        if((eventMask & GAWTEvent.FOCUS_EVENT_MASK) > 0)
        {
            addAWTEventListener(listener, GFocusEvent.class);
        }
        if((eventMask & GAWTEvent.MOUSE_EVENT_MASK) > 0)
        {
            addAWTEventListener(listener, GMouseEvent.class);
        }
    }
    
    public void addAWTEventListener(GAWTEventListener listener, Class eventClass)
    {
        if(awtListeners == null)
        {
            awtListeners = new HashMap<>();
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
        List<GAWTEventListener> list = awtListeners.get(eventClass);
        if(list == null)
        {
            list = new ArrayList<>();
            awtListeners.put(eventClass, list);
        }
        if(list.contains(listener) == false)
        {
            list.add(listener);
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
            for(List<GAWTEventListener> list: awtListeners.values())
            {
                list.remove(listener);
            }
//            if(awtListeners.size() == 0)
//            {
//                rootcomp.removeMouseListener(baseListener);
//                rootcomp.removeMouseMotionListener(baseListener);
//                rootcomp.removeKeyListener(baseListener);
//                rootcomp.removeTouchListener(baseListener);
//                baseListener = null;
//            }
        }
    }
    
    public void handleAWTEvent(GAWTEvent evt)
    {
        if(awtListeners != null)
        {
            List<GAWTEventListener> list = awtListeners.get(evt.getClass());
            if(list != null)
            {
                List<GAWTEventListener> listeners = new ArrayList<>(list);
                for(int i=0;i<listeners.size();i++)
                {
                    listeners.get(i).eventDispatched(evt);
                }
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
