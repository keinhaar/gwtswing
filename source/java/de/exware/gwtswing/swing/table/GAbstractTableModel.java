package de.exware.gwtswing.swing.table;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.swing.event.GTableModelEvent;
import de.exware.gwtswing.swing.event.GTableModelListener;

abstract public class GAbstractTableModel implements GTableModel
{
    private List<GTableModelListener> listeners;
    
    @Override
    public String getColumnName(int col)
    {
        return "" + col;
    }

    @Override
    public Class<?> getColumnClass(int col)
    {
        return Object.class;
    }

    @Override
    public void addTableModelListener(GTableModelListener listener)
    {
        if(listeners == null)
        {
            listeners = new ArrayList<>();
        }
        if(listeners.contains(listener) == false)
        {
            listeners.add(listener);
        }
    }

    @Override
    public void removeTableModelListener(GTableModelListener listener)
    {
        if(listeners != null)
        {
            listeners.remove(listener);
        }
    }

    public void fireTableStructureChanged() 
    {
        fireTableChanged(new GTableModelEvent(this, GTableModelEvent.HEADER_ROW));
    }
    
    public void fireTableChanged(GTableModelEvent e) 
    {
        if(listeners != null)
        {
            List<GTableModelListener> listeners = new ArrayList<>(this.listeners);
            for(int i=0;listeners != null && i<listeners.size();i++)
            {
                listeners.get(i).tableChanged(e);
            }
        }
    }
    
    public void fireTableRowsInserted(int from, int to)
    {
        List<GTableModelListener> listeners = new ArrayList<>(this.listeners);
        GTableModelEvent evt = new GTableModelEvent(this, from, to, 0, GTableModelEvent.INSERT);
        for(int i=0;listeners != null && i<listeners.size();i++)
        {
            listeners.get(i).tableChanged(evt);
        }
    }
    
    public void fireTableRowsDeleted(int from, int to)
    {
        List<GTableModelListener> listeners = new ArrayList<>(this.listeners);
        GTableModelEvent evt = new GTableModelEvent(this, from, to, 0, GTableModelEvent.DELETE);
        for(int i=0;listeners != null && i<listeners.size();i++)
        {
            listeners.get(i).tableChanged(evt);
        }
    }

    public void fireTableRowsUpdated(int from, int to)
    {
        List<GTableModelListener> listeners = new ArrayList<>(this.listeners);
        GTableModelEvent evt = new GTableModelEvent(this, from, to, 0, GTableModelEvent.UPDATE);
        for(int i=0;listeners != null && i<listeners.size();i++)
        {
            listeners.get(i).tableChanged(evt);
        }
    }
}
