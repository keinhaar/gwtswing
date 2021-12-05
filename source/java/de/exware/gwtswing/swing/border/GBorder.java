package de.exware.gwtswing.swing.border;

import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public interface GBorder
{
    public void install(GComponent component);
    
    GInsets getBorderInsets(GComponent c);
}
