package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.swing.event.GListDataEvent;
import de.exware.gwtswing.swing.event.GListDataListener;

abstract public class GAbstractListModel<T>
    implements GListModel<T>
{
    private List<GListDataListener> listeners = new ArrayList<>();

    @Override
    public void addListDataListener(GListDataListener l)
    {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(GListDataListener l)
    {
        listeners.remove(l);
    }
    
    protected void fireIntervalAdded(int minIndex, int maxIndex)
    {
        GListDataEvent evt = new GListDataEvent(this, GListDataEvent.INTERVAL_ADDED, minIndex, maxIndex);
        for(int i=0;i<listeners.size();i++)
        {
            GListDataListener l = listeners.get(i);
            l.intervalAdded(evt);
        }
    }
    
    protected void fireIntervalRemoved(int minIndex, int maxIndex)
    {
        GListDataEvent evt = new GListDataEvent(this, GListDataEvent.INTERVAL_REMOVED, minIndex, maxIndex);
        for(int i=0;i<listeners.size();i++)
        {
            GListDataListener l = listeners.get(i);
            l.intervalRemoved(evt);
        }
    }
    
    protected void fireContentsChanged()
    {
        GListDataEvent evt = new GListDataEvent(this, GListDataEvent.CONTENTS_CHANGED, 0, 0);
        for(int i=0;i<listeners.size();i++)
        {
            GListDataListener l = listeners.get(i);
            l.contentsChanged(evt);
        }
    }
}
