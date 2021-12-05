package de.exware.gwtswing.awt;

public class GPoint
{

    public int x,y;

    public GPoint(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public GPoint()
    {
    }

    public String toString()
    {
        return "GPoint (" + x + ", " + y +")";
    }
}
