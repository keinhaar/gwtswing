package de.exware.gwtswing.swing.tree;

public interface GTreeNode
{
    GTreeNode getChildAt(int childIndex);

    int getChildCount();

    GTreeNode getParent();

    int getIndex(GTreeNode node);

    boolean getAllowsChildren();

    boolean isLeaf();
}
