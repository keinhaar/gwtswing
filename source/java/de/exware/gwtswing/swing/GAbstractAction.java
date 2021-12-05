package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.swing.event.GPropertyChangeEvent;
import de.exware.gwtswing.swing.event.GPropertyChangeListener;

/**
 * Actions can be used in Menus and/or Toolbars.
 */
abstract public class GAbstractAction implements GAction
{
    private Map<String, Object> values = new HashMap<>();
    private boolean enabled = true;
    private List<GPropertyChangeListener> listeners;

    public GAbstractAction(String name)
    {
        values.put(NAME, name);
    }

    @Override
    public void actionPerformed(GActionEvent evt)
    {
    }

    @Override
    public void putValue(String key, Object value)
    {
        values.put(key, value);
        firePropertyChanged(key, value);
    }

    @Override
    public Object getValue(String key)
    {
        return values.get(key);
    }

    @Override
    public void addPropertyChangeListener(GPropertyChangeListener li)
    {
        if (listeners == null) listeners = new ArrayList<>();
        if(listeners.contains(li) == false)
        {
            listeners.add(li);
        }
    }

    @Override
    public void removePropertyChangeListener(GPropertyChangeListener li)
    {
        if (listeners != null)
        {
            listeners.remove(li);
        }
    }

    @Override
    public void setEnabled(boolean b)
    {
        enabled = b;
        firePropertyChanged(ENABLED_PROPERTY, b);
    }

    @Override
    public boolean isEnabled()
    {
        return enabled;
    }

    private void firePropertyChanged(String property, Object value)
    {
        GPropertyChangeEvent evt = new GPropertyChangeEvent(this, property, value);
        for (int i = 0; listeners != null && i < listeners.size(); i++)
        {
            GPropertyChangeListener li = listeners.get(i);
            li.propertyChanged(evt);
        }
    }
}
