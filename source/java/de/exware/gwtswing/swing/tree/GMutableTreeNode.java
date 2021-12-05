package de.exware.gwtswing.swing.tree;

public interface GMutableTreeNode extends GTreeNode
{
    void insert(GMutableTreeNode child, int index);

    void remove(int index);

    void remove(GMutableTreeNode node);

    void setUserObject(Object object);

    void removeFromParent();

    void setParent(GMutableTreeNode newParent);
}
