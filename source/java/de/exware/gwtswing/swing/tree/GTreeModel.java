package de.exware.gwtswing.swing.tree;

import de.exware.gwtswing.swing.event.GTreeModelListener;

public interface GTreeModel
{
    public Object getRoot();

    public Object getChild(Object parent, int index);

    public int getChildCount(Object parent);

    public boolean isLeaf(Object node);

    public void valueForPathChanged(GTreePath path, Object newValue);

    public int getIndexOfChild(Object parent, Object child);

    void addTreeModelListener(GTreeModelListener l);

    void removeTreeModelListener(GTreeModelListener l);
}
