package de.exware.gwtswing.swing.tree;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.swing.event.GTreeModelEvent;
import de.exware.gwtswing.swing.event.GTreeModelListener;

public class GDefaultTreeModel implements GTreeModel
{
    private List<GTreeModelListener> listeners;
    private GTreeNode root;
    
    public GDefaultTreeModel(GTreeNode root)
    {
        this.root = root;
    }
    
    @Override
    public Object getRoot()
    {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index)
    {
        GTreeNode tn = (GTreeNode) parent;
        return tn.getChildAt(index);
    }

    @Override
    public int getChildCount(Object parent)
    {
        GTreeNode tn = (GTreeNode) parent;
        return tn.getChildCount();
    }

    @Override
    public boolean isLeaf(Object node)
    {
        GTreeNode tn = (GTreeNode) node;
        return tn.isLeaf();
    }

    @Override
    public void valueForPathChanged(GTreePath path, Object newValue)
    {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
        GTreeNode tn = (GTreeNode) parent;
        return tn.getIndex((GTreeNode) child);
    }

    @Override
    public void addTreeModelListener(GTreeModelListener l)
    {
        if(listeners == null)
        {
            listeners = new ArrayList<>();
        }
        if(listeners.contains(l) == false)
        {
            listeners.add(l);
        }
    }

    @Override
    public void removeTreeModelListener(GTreeModelListener l)
    {
        if(listeners != null)
        {
            listeners.remove(l);
        }
    }
    
    protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        if(listeners != null && listeners.size() > 0)
        {
            GTreeModelEvent evt = new GTreeModelEvent(this);
            for(int i=0;i<listeners.size();i++)
            {
                listeners.get(i).treeStructureChanged(evt);
            }
        }
    }
    
    protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children)
    {
        if(listeners != null && listeners.size() > 0)
        {
            GTreeModelEvent evt = new GTreeModelEvent(this, new GTreePath(path), childIndices);
            for(int i=0;i<listeners.size();i++)
            {
                listeners.get(i).treeNodesChanged(evt);
            }
        }
    }
    
}
