package de.exware.gwtswing.swing.tree;

import java.util.ArrayList;
import java.util.List;

public class GDefaultMutableTreeNode implements GMutableTreeNode
{
    private List<GTreeNode> children;
    private GTreeNode parent;
    private Object userObject;
    
    public GDefaultMutableTreeNode()
    {
    }
    
    public GDefaultMutableTreeNode(Object userObject)
    {
        this.userObject = userObject;
    }

    @Override
//    @JsMethod
    public String toString()
    {
        String str = "";
        if(userObject != null)
        {
            str = userObject.toString();
        }
        return str;
    }
    
    @Override
    public GTreeNode getChildAt(int childIndex)
    {
        if(children != null)
        {
            return children.get(childIndex);
        }
        return null;
    }

    @Override
    public int getChildCount()
    {
        if(children != null)
        {
            return children.size();
        }
        return 0;
    }

    @Override
    public GTreeNode getParent()
    {
        return parent;
    }

    @Override
    public int getIndex(GTreeNode node)
    {
        if(children != null)
        {
            return children.indexOf(node);
        }
        return -1;
    }

    @Override
    public boolean getAllowsChildren()
    {
        return false;
    }

    @Override
    public boolean isLeaf()
    {
        return children == null;
    }

    @Override
    public void insert(GMutableTreeNode child, int index)
    {
        if(children == null)
        {
            children = new ArrayList<>();
        }
        children.add(index, child);
        child.setParent(this);
    }

    @Override
    public void remove(int index)
    {
        if(children != null)
        {
            children.remove(index);
        }
    }

    @Override
    public void remove(GMutableTreeNode node)
    {
        if(children != null)
        {
            children.remove(node);
        }
    }

    @Override
    public void setUserObject(Object object)
    {
        userObject = object;
    }

    @Override
    public void setParent(GMutableTreeNode newParent)
    {
        parent = newParent;
    }

    @Override
    public void removeFromParent()
    {
        if(parent instanceof GMutableTreeNode)
        {
            ((GMutableTreeNode)parent).remove(this);
        }
    }

    public Object getUserObject()
    {
        return userObject;
    }

    public void add(GMutableTreeNode node)
    {
        if(children == null)
        {
            children = new ArrayList<>();
        }
        children.add(node);
        node.setParent(this);
    }

}
