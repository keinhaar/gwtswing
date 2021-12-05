package de.exware.gwtswing.swing.tree;

public class GTreePath
{
    private Object[] path;
    
    public GTreePath(Object[] path)
    {
        this.path = path;
    }
    
    public GTreePath(Object lastPathComponent)
    {
        this.path = new Object[]{lastPathComponent};
    }
    
    public Object getLastPathComponent()
    {
        return path[path.length-1];
    }
}
