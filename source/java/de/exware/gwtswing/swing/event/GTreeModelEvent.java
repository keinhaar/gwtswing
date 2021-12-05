package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.swing.tree.GTreePath;
import de.exware.gwtswing.util.GEventObject;

public class GTreeModelEvent extends GEventObject
{
    private GTreePath parent;
    
    public GTreeModelEvent(Object source)
    {
        super(source);
    }

    public GTreeModelEvent(Object source, GTreePath parent, int[] children)
    {
        super(source);
        this.parent = parent;
    }
    
    public GTreePath getTreePath() 
    { 
        return parent; 
    }

}
