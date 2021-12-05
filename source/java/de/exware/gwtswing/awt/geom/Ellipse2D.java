package de.exware.gwtswing.awt.geom;


public abstract class Ellipse2D extends RectangularShape
{
    public static class Double extends Ellipse2D
    {
        public double x;
        public double y;
        public double width;
        public double height;
        
        public Double()
        {
            
        }
        
        public Double(double x,double y,double w,double h)
        {
            this.x = x;
            this.y = y;
            width = w;
            height = h;
        }

        @Override
        public double getX()
        {
            return x;
        }

        @Override
        public double getY()
        {
            return y;
        }

        @Override
        public double getWidth()
        {
            return width;
        }

        @Override
        public double getHeight()
        {
            return height;
        }
    }
    
    @Override
    public boolean intersects(Rectangle2D rect)
    {
        double x = getX();
        double y = getY();
        return rect.getX() + rect.getWidth() > x
            && rect.getY() + rect.getHeight() > y
            && rect.getX() < x + getWidth()
            && rect.getY() < y + getHeight();
    }
    
    @Override
    public String toString()
    {
        return getClass().getSimpleName() + " (" + getX() + ","+ getY() + "," + getWidth() + "," + getHeight() + ")";
    }
}
