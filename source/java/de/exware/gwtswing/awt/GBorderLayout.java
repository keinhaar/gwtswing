package de.exware.gwtswing.awt;

import java.util.Objects;

import de.exware.gwtswing.swing.GComponent;

public class GBorderLayout implements GLayoutManager
{
    public static final String CENTER = "Center";
    public static final String WEST = "West";
    public static final String EAST = "East";
    public static final String NORTH = "North";
    public static final String SOUTH = "South";

    private GComponent centerComp;
    private GComponent westComp;
    private GComponent eastComp;
    private GComponent northComp;
    private GComponent southComp;
    private int hgap, vgap;

    public GBorderLayout(int hgap, int vgap)
    {
        this.hgap = hgap;
        this.vgap = vgap;
    }

    public GBorderLayout()
    {
        // TODO Auto-generated constructor stub
    }

    /**
     * Calculates the preferred size dimensions for the specified panel given
     * the components in the specified parent container.
     * 
     * @param parent
     *            the component to be laid out
     * 
     * @see #minimumLayoutSize
     */
    @Override
    public GDimension preferredLayoutSize(GComponent parent)
    {
        GInsets insets = parent.getInsets();
        int w;
        int h;
        GDimension wPref = new GDimension(0, 0);
        GDimension nPref = wPref;
        GDimension ePref = wPref;
        GDimension sPref = wPref;
        GDimension cPref = wPref;
        int horCount = 0;
        int vertCount = 0;
        if (westComp != null && westComp.isVisible())
        {
            wPref = westComp.getPreferredSize();
            horCount++;
        }
        if (eastComp != null && eastComp.isVisible())
        {
            ePref = eastComp.getPreferredSize();
            horCount++;
        }
        if (northComp != null && northComp.isVisible())
        {
            nPref = northComp.getPreferredSize();
            vertCount++;
        }
        if (southComp != null && southComp.isVisible())
        {
            sPref = southComp.getPreferredSize();
            vertCount++;
        }
        if (centerComp != null && centerComp.isVisible())
        {
            cPref = centerComp.getPreferredSize();
            horCount++;
            vertCount++;
        }
        int northLine = nPref.width;
        int centerLine = wPref.width + cPref.width + ePref.width + (horCount - 1) * hgap;
        int southLine = sPref.width;
        int westColumn = nPref.height + wPref.height + sPref.height + (vertCount - 1) * vgap;
        int eastColumn = nPref.height + ePref.height + sPref.height + (vertCount - 1) * vgap;
        int centerColumn = nPref.height + cPref.height + sPref.height + (vertCount - 1) * vgap;
        w = northLine > centerLine ? northLine : centerLine;
        w = w > southLine ? w : southLine;
        h = westColumn > centerColumn ? westColumn : centerColumn;
        h = h > eastColumn ? h : eastColumn;
//        h = westColumn > centerColumn ? westColumn : (centerColumn > eastColumn ? centerColumn : eastColumn);

        return new GDimension(w, h);
    }

    /**
     * Calculates the minimum size dimensions for the specified panel given the
     * components in the specified parent container.
     * 
     * @param parent
     *            the component to be laid out
     * @see #preferredLayoutSize
     */
    @Override
    public GDimension minimumLayoutSize(GComponent parent)
    {
        int count = parent.getComponentCount();
        GDimension dim = new GDimension();
        for (int i = 0; i < count; i++)
        {
            GDimension pref = parent.getComponent(i).getMinimumSize();
            dim.width = dim.width > pref.width ? dim.width : pref.width;
            dim.height = dim.height > pref.height ? dim.height : pref.height;
        }
        return dim;
    }

    /**
     * Lays out the container in the specified panel.
     * 
     * @param parent
     *            the component which needs to be laid out
     */
    @Override
    public void layoutContainer(GComponent parent)
    {
        GDimension dim = parent.getSize();
        GInsets insets = parent.getInsets();
        int x = 0;
        int y = 0;
        if (northComp != null && northComp.isVisible())
        {
            northComp.setMaxWidthForPreferredSize(dim.width - insets.left - insets.right );
            GDimension prefSize = northComp.getPreferredSize();
            int w = dim.width - x - insets.left - insets.right;
            northComp.setBounds(x, y, w, prefSize.height);
            y += prefSize.height + vgap;
        }
        if (southComp != null && southComp.isVisible())
        {
            GDimension prefSize = southComp.getPreferredSize();
            int w = dim.width - x - insets.left - insets.right;
            southComp.setBounds(x, dim.height - prefSize.height - insets.top - insets.bottom,w, prefSize.height);
            dim.height = dim.height - prefSize.height - vgap;
        }
        if (westComp != null && westComp.isVisible())
        {
            GDimension prefSize = westComp.getPreferredSize();
            westComp.setBounds(x,y,prefSize.width, dim.height - insets.top - insets.bottom - y);
            x += prefSize.width + hgap;
        }
        if (eastComp != null && eastComp.isVisible())
        {
            GDimension prefSize = eastComp.getPreferredSize();
            eastComp.setBounds(dim.width - prefSize.width - insets.right - insets.left, y,prefSize.width, dim.height - insets.top - insets.bottom -y);
            dim.width = dim.width - prefSize.width - hgap;
        }
        if (centerComp != null && centerComp.isVisible())
        {
            int w = dim.width - x - insets.right - insets.left;
            int h = dim.height - y - insets.bottom - insets.top;
            centerComp.setBounds(x,y,w, h);
        }
    }

    @Override
    public void addLayoutComponent(String name, GComponent comp)
    {
        addLayoutComponent(comp, name);
    }

    @Override
    public void addLayoutComponent(GComponent comp, Object constraints)
    {
        if (constraints.equals(CENTER))
        {
            removeIfExists(centerComp);
            centerComp = comp;
        }
        else if (constraints.equals(WEST))
        {
            removeIfExists(westComp);
            westComp = comp;
        }
        else if (constraints.equals(EAST))
        {
            removeIfExists(eastComp);
            eastComp = comp;
        }
        else if (constraints.equals(NORTH))
        {
            removeIfExists(northComp);
            northComp = comp;
        }
        else if (constraints.equals(SOUTH))
        {
            removeIfExists(southComp);
            southComp = comp;
        }
    }

    private void removeIfExists(GComponent comp)
    {
        if(comp != null && comp.getParent() != null)
        {
            comp.getParent().remove(comp);
        }
    }
    
    @Override
    public void removeLayoutComponent(GComponent comp)
    {
        if(Objects.equals(comp, eastComp))
        {
            eastComp = null;
        }
        else if(Objects.equals(comp, westComp))
        {
            westComp = null;
        }
        else if(Objects.equals(comp, centerComp))
        {
            centerComp = null;
        }
        else if(Objects.equals(comp, northComp))
        {
            northComp = null;
        }
        else if(Objects.equals(comp, southComp))
        {
            southComp = null;
        }
    }
}
