package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPRangeElement;
import de.exware.gplatform.event.GPEvent;
import de.exware.gplatform.event.GPEventListener;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.swing.event.GChangeEvent;
import de.exware.gwtswing.swing.event.GChangeListener;

public class GSlider extends GComponent implements GSwingConstants
{
    private int orientation;
    private List<GChangeListener> changeListeners;
    
    public GSlider()
    {
        this(HORIZONTAL, 0, 100, 50);
    }

    public GSlider(int orientation)
    {
        this(orientation, 0, 100, 50);
    }
    
    public GSlider(int min, int max)
    {
        this(HORIZONTAL, min, max, (min + max) / 2);
    }
    
    public GSlider(int min, int max, int value)
    {
        this(HORIZONTAL, min, max, value);
    }
    
    public GSlider(int orientation, int min, int max, int value)
    {
        super(GPlatform.getDoc().createRangeElement());
        setOrientation(orientation);
        setMinimum(min);
        setMaximum(max);
        setValue(value);
        GPRangeElement range = (GPRangeElement) getPeer();
        range.enabledEvents(GPEvent.Type.ONCHANGE);
        range.setEventListener(new GPEventListener()
        {
            @Override
            public void onBrowserEvent(GPEvent event)
            {
                fireStateChanged();
            }
        });
    }

    public void addChangeListener(GChangeListener l) 
    {
        if(changeListeners == null)
        {
            changeListeners = new ArrayList<>();
        }
        if(changeListeners.contains(l) == false)
        {
            changeListeners.add(l);
        }
    }

    private void fireStateChanged()
    {
        GChangeEvent event = new GChangeEvent(this);
        for(int i=0;changeListeners != null && i < changeListeners.size();i++)
        {
            changeListeners.get(i).stateChanged(event);
        }
    }
    
    public int getValue()
    {
        GPRangeElement range = (GPRangeElement) getPeer();
        return range.getValueInt();
    }
    
    public void setMinimum(int min)
    {
        GPRangeElement range = (GPRangeElement) getPeer();
        range.setMinimum(min);
    }
    
    public void setMaximum(int min)
    {
        GPRangeElement range = (GPRangeElement) getPeer();
        range.setMaximum(min);
    }
    
    public void setValue(int min)
    {
        GPRangeElement range = (GPRangeElement) getPeer();
        range.setValue(min);
    }
    
    private void setOrientation(int orientation)
    {
        this.orientation = orientation;
        GPRangeElement range = (GPRangeElement) getPeer();
        if(orientation == VERTICAL)
        {
            range.addClassName("gwts-GSlider-vertical");
            range.setAttribute("orient", "vertical");
            GDimension dim = new GDimension(20, 200);
            setPreferredSize(dim);
        }
        else
        {
            range.removeClassName("gwts-GSlider-vertical");
            range.setAttribute("orient", "");
            GDimension dim = new GDimension(200, 20);
            setPreferredSize(dim);
        }
    }
    
    public int getOrientation()
    {
        return orientation;
    }
}
