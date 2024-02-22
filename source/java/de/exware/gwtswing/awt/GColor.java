package de.exware.gwtswing.awt;

/**
 * Allows to use Colors like in Swing
 */
public class GColor
{
    public static final GColor RED = new GColor(255, 0, 0);
    public static final GColor GREEN = new GColor(0, 255, 0);
    public static final GColor BLUE = new GColor(0, 0, 255);
    public static final GColor BLACK = new GColor(0, 0, 0);
    public static final GColor WHITE = new GColor(255, 255, 255);
    public static final GColor LIGHT_GRAY = new GColor(192, 192, 192);
    public static final GColor GRAY = new GColor(128, 128, 128);
    public static final GColor DARK_GRAY = new GColor(64, 64, 64);
    public static final GColor PINK = new GColor(255, 175, 175);
    public static final GColor ORANGE = new GColor(255, 200, 0);
    public static final GColor YELLOW = new GColor(255, 255, 0);
    public static final GColor NONE = new GColor(0,0,0);

    private int r, g, b, a;

    public GColor(int r, int g, int b)
    {
        this(r, g, b, 255);
    }

    public GColor(int r, int g, int b, int a)
    {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    /**
     * This Method will be used to create the browsers hex color value.
     * 
     * @return
     */
    public String toHex()
    {
        if(this == NONE)
        {
            return "";
        }
        String r = Integer.toHexString(this.r);
        String g = Integer.toHexString(this.g);
        String b = Integer.toHexString(this.b);
        String a = Integer.toHexString(this.a);
        if (r.length() == 1) r = "0" + r;
        if (g.length() == 1) g = "0" + g;
        if (b.length() == 1) b = "0" + b;
        if (a.length() == 1) a = "0" + a;
        return "#" + r + g + b;
    }

    /**
     * This Method will be used to create the browsers hex color value.
     * 
     * @return
     */
    public String toRGBA()
    {
        if(this == NONE)
        {
            return "";
        }
        return "rgba(" + r + ", " + g + ", " + b + ", " + (a/255.0) + ")";
    }

    public GColor darker()
    {
        return darker(this, 30);
    }

    public GColor brighter()
    {
        return brighter(this, 30);
    }

    public static GColor brighter(GColor color, int value)
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        r = r + value > 255 ? 255 : r + value;
        g = g + value > 255 ? 255 : g + value;
        b = b + value > 255 ? 255 : b + value;
        return new GColor(r, g, b);
    }

    private int getBlue()
    {
        return b;
    }

    private int getGreen()
    {
        return g;
    }

    private int getRed()
    {
        return r;
    }

    public static GColor darker(GColor color, int value)
    {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        r = r - value < 0 ? 0 : r - value;
        g = g - value < 0 ? 0 : g - value;
        b = b - value < 0 ? 0 : b - value;
        return new GColor(r, g, b);
    }

    /**
     * Diese Methode parsed den übergebenen String und versucht daraus ein Color
     * Objekt zu generieren. <BR>
     * Die Farbe kann als Hexadezimaler String angegeben werden, dabei kann das
     * '#' optional angegeben sein oder weggelassen werden. Z.B.: #AABBCC oder
     * #123456 <BR>
     * Alternativ kann der Farbwert auch als rgb(r,g,b) angegeben werden.
     * Diese Methode wirft eine RuntimeException wenn die Farbangabe nicht dem
     * Format entspricht.
     * 
     * @param color
     *            Der die Farbe beschreibende String
     * @return Die erzeugte Farbe
     */
    public static final GColor decode(String color)
    {
        GColor col = null;
        if (color != null && color.length() > 0)
        {
            try
            {
                if("transparent".equals(color))
                {
                    
                }
                else if(color.startsWith("rgba"))
                {
                	color = color.substring(5, color.length()-1);
                    String[] str = color.split(",");
                    int r = Integer.parseInt(str[0].trim());
                    int gr = Integer.parseInt(str[1].trim());
                    int b = Integer.parseInt(str[2].trim());
                    int a = Integer.parseInt(str[3].trim());
                    col = new GColor(r, gr, b, a);
                }
                else if(color.startsWith("rgb"))
                {
                    color = color.substring(4, color.length()-1);
                    String[] str = color.split(",");
                    int r = Integer.parseInt(str[0].trim());
                    int gr = Integer.parseInt(str[1].trim());
                    int b = Integer.parseInt(str[2].trim());
                    col = new GColor(r, gr, b);
                }
                else
                {
                    if (color.charAt(0) == '#')
                    {
                        color = color.substring(1);
                    }
                    int r = Integer.parseInt(color.substring(0, 2), 16);
                    int gr = Integer.parseInt(color.substring(2, 4), 16);
                    int b = Integer.parseInt(color.substring(4, 6), 16);
                    col = new GColor(r, gr, b);
                }
            }
            catch (Exception ex)
            {
                throw new RuntimeException("Could not parse Color: " + color);
            }
        }
        return col;
    }
}
