package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.swing.event.GPropertyChangeListener;

public interface GAction extends GActionListener
{
    public static final String NAME = "name";
    public static final String ICON = "imageurl";
    public static final String ENABLED_PROPERTY = "enabled";
    public static final String SHORT_DESCRIPTION = "short";

    public void setEnabled(boolean enabled);

    public void putValue(String key, Object value);

    public Object getValue(String key);

    public boolean isEnabled();

    void addPropertyChangeListener(GPropertyChangeListener li);

    public void removePropertyChangeListener(GPropertyChangeListener propertyListener);
}
