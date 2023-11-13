package de.exware.gwtswing.awt;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.GPWindow;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;

public class GToolkit
{
    private static GToolkit instance = new GToolkit();
    private List<GAWTEventListener> awtListeners;
    
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
        }
    }
    
    public void handleAWTEvent(GAWTEvent evt)
    {
        for(int i=0;awtListeners != null && i<awtListeners.size();i++)
        {
            awtListeners.get(i).eventDispatched(evt);
        }
    }
}
