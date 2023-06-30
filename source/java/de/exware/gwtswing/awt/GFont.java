package de.exware.gwtswing.awt;


public class GFont
{
    public static final int PLAIN = 0;
    public static final int BOLD = 1;
    public static final int ITALIC = 2;
    
    private String familyName;
    private int size;
    private int style = PLAIN;
    
    public GFont(String familyName, int style, int size)
    {
        this.familyName = familyName;
        this.size = size;
        this.style = style;
    }

    public float getSize2D()
    {
        return size;
    }

    public GFont deriveFont(float size)
    {
        return new GFont(familyName, style, (int) size);
    }
    
    public GFont deriveFont(int style)
    {
        return new GFont(familyName, style, size);
    }
    
    public String getFamily()
    {
        return familyName;
    }
    
    public int getStyle()
    {
        return style;
    }
    
    @Override
    public String toString()
    {
        return familyName + ";" + size + ";" + style;
    }
    
    /**
     * Gibt den Font CSS kompatibel aus.
     * @return
     */
    public String toCSS()
    {
        return (getStyle() == GFont.BOLD ? "bold" : "normal")
            + " " + getSize2D() + "px " 
            + " " + getFamily();         
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean eq = obj == this;
        if(eq == false && obj instanceof GFont)
        {
            eq = toCSS().equals(((GFont)obj).toCSS());
        }
        return eq;
    }
    
    @Override
    public int hashCode()
    {
        return size + style + familyName.hashCode();
    }
}
