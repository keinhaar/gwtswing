package de.exware.gwtswing.awt;

public class GGridBagConstraints
{
    /**
     * Do not resize the component.
     */
    public static final int NONE = 0;

    /**
     * Resize the component both horizontally and vertically.
     */
    public static final int BOTH = 1;

    /**
     * Resize the component horizontally but not vertically.
     */
    public static final int HORIZONTAL = 2;

    /**
     * Resize the component vertically but not horizontally.
     */
    public static final int VERTICAL = 3;

    public static final int CENTER = 10;
    public static final int NORTH = 11;
    public static final int NORTHEAST = 12;
    public static final int EAST = 13;
    public static final int SOUTHEAST = 14;
    public static final int SOUTH = 15;
    public static final int SOUTHWEST = 16;
    public static final int WEST = 17;
    public static final int NORTHWEST = 18;

    public static final int RELATIVE = -1;
    public static final int REMAINDER = 0;

    public static final int PAGE_START = 19;

    public static final int PAGE_END = 20;

    public static final int LINE_START = 21;

    public static final int LINE_END = 22;

    public static final int FIRST_LINE_START = 23;

    public static final int FIRST_LINE_END = 24;

    public static final int LAST_LINE_START = 25;

    public static final int LAST_LINE_END = 26;

    public int gridx = 0;
    public int gridy = 0;
    public int gridwidth = 1;
    public int gridheight = 1;
    public GInsets insets = new GInsets();
    public int fill = -1;
    public int anchor = -1;
    public int ipadx;
    public double weighty;
    public int ipady;
    public double weightx;

    public GGridBagConstraints clone()
    {
        GGridBagConstraints clone = new GGridBagConstraints();
        clone.gridx = gridx;
        clone.gridy = gridy;
        clone.gridwidth = gridwidth;
        clone.gridheight = gridheight;
        clone.insets.left = insets.left;
        clone.insets.top = insets.top;
        clone.insets.right = insets.right;
        clone.insets.bottom = insets.bottom;
        clone.weightx = weightx;
        clone.weighty = weighty;
        clone.fill = fill;
        clone.anchor = anchor;
        return clone;
    }
}

