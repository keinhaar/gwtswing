package de.exware.gwtswing.swing.border;

import de.exware.gwtswing.awt.GColor;

public class GBorderFactory
{
    public static GBorder createEmptyBorder(int top, int left, int bottom, int right)
    {
        return new GEmptyBorder(top, left, bottom, right);
    }

    public static GBorder createDefaultEmptyBorder()
    {
        return new GEmptyBorder(10, 10, 10, 10);
    }

    public static GBorder createLineBorder(GColor col, int thick)
    {
        return new GLineBorder(col, thick);
    }

    public static GBorder createEtchedBorder()    
    {
        return new GEtchedBorder();
    }

    public static GBorder createEtchedBorder(int type)
    {
        return new GEtchedBorder(type);
    }
    
    public static GBorder createBevelBorder(int type)
    {
        return new GBevelBorder(type);
    }
}
