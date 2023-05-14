package de.exware.gwtswing.awt;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.event.GPEvent;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GKeyEvent;
import de.exware.gwtswing.awt.event.GKeyListener;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
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
        }
        if(awtListeners.contains(listener) == false)
        {
            awtListeners.add(listener);
        }
    }
    
    class BaseMouseListener extends GMouseAdapter
        implements GMouseMotionListener, GKeyListener
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
    }
    
    class BaseComponent extends GComponent
    {
        public BaseComponent()
        {
            setPeer(GPlatform.getDoc().getBody());
        }
        
        @Override
        public GAWTEvent handleEvent(GPEvent event)
        {
            return super.handleEvent(event);
        }
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
                baseListener = null;
            }
        }
    }
    
    public void handleAWTEvent(GAWTEvent evt)
    {
//        if(evt.getSource() == rootcomp)
//        {
//            System.out.println("X");
//        }
        for(int i=0;awtListeners != null && i<awtListeners.size();i++)
        {
            awtListeners.get(i).eventDispatched(evt);
        }
    }
}
