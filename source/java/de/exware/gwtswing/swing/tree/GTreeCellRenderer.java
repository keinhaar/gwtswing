package de.exware.gwtswing.swing.tree;

import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GTree;

public interface GTreeCellRenderer 
{
    GComponent getTreeCellRendererComponent(GTree tree, Object value,
                                   boolean selected, boolean expanded,
                                   boolean leaf, int row, boolean hasFocus);
}
