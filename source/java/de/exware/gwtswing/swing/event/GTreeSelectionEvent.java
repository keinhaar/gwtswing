package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.swing.tree.GTreePath;
import de.exware.gwtswing.util.GEventObject;

public class GTreeSelectionEvent extends GEventObject
{
    private GTreePath path;
    
    public GTreeSelectionEvent(Object source, GTreePath treePath)
    {
        super(source);
        path = treePath;
    }
    
    public GTreePath getPath()
    {
        return path;
    }
}
