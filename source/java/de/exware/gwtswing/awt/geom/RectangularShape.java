package de.exware.gwtswing.awt.geom;

import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.GShape;

abstract public class RectangularShape implements GShape
{

    @Override
    public boolean contains(GPoint p)
    {
        return contains(p.x, p.y);
    }

    @Override
    public boolean contains(int x, int y)
    {
        return x > getX() && x < getX()+getWidth() 
            && y > getY() && y < getY()+getHeight();
    }

    abstract public double getX();
    abstract public double getY();
    abstract public double getWidth();
    abstract public double getHeight();
}
