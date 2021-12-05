package de.exware.gwtswing.awt;

public class GInsets
{
    public int top;
    public int left;
    public int right;
    public int bottom;
    
    public GInsets(int top, int left, int bottom, int right)
    {
        super();
        this.top = top;
        this.left = left;
        this.right = right;
        this.bottom = bottom;
    }

    public GInsets()
    {
    }
    
    @Override
    public String toString()
    {
        return "GInsets: " + top + ", " + left + ", " + bottom + ", " + right;
    }
}
