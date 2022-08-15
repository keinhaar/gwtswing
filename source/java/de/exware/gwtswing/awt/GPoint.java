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

    @Override
    public boolean equals(Object obj)
    {
        boolean eq = obj == this;
        if (eq == false && obj instanceof GPoint)
        {
            GPoint gp = (GPoint) obj;
            eq = x == gp.x && y == gp.y;
        }
        return eq;
    }

    @Override
    public int hashCode()
    {
        return x + y;
    }

    
    public double getX()
    {
        return x;
    }
    
    public double getY()
    {
        return y;
    }
    
    @Override
    public String toString()
    {
        return "GPoint (" + x + ", " + y +")";
    }
}
