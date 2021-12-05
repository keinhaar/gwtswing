package de.exware.gwtswing.awt;

import de.exware.gwtswing.awt.geom.Rectangle2D;

public class GRectangle extends Rectangle2D
{
    public int x,y,width,height;

    public GRectangle(int x, int y, int width, int height)
    {
        super();
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public GRectangle()
    {
    }

    @Override
    public double getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    @Override
    public double getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public double getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    @Override
    public double getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }
    
    public void setBounds(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
