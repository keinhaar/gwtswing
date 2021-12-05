package de.exware.gwtswing.awt;

import de.exware.gwtswing.awt.geom.Rectangle2D;

public interface GShape
{
    public boolean contains(GPoint p);
    public boolean contains(int x, int y);
    
    public boolean intersects(Rectangle2D rect);
}
