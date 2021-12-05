package de.exware.gwtswing.awt;

public class GDimension
{
    public int width, height;
    
    public GDimension(int w, int h)
    {
        this.width = w;
        this.height = h;
    }

    public GDimension()
    {
    }

    public GDimension(GDimension dim)
    {
        this.width = dim.width;
        this.height = dim.height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    @Override
    public String toString()
    {
        return "GDimension " + width + " / " + height;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if(obj == this) return true;
        if(obj instanceof GDimension)
        {
            GDimension dim = (GDimension) obj;
            return width == dim.width && height == dim.height;
        }
        return false;
    }
}
