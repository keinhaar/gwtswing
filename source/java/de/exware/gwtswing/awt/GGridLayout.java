package de.exware.gwtswing.awt;

import de.exware.gwtswing.swing.GComponent;

public class GGridLayout implements GLayoutManager
{
    private int rowCount;
    private int columnCount;
    private int horGap;
    private int vertGap;

    public GGridLayout(int rows,int cols)
    {
        this(rows,cols,0,0);
    }

    public GGridLayout(int rows,int cols,int hgap,int vgap)
    {
        rowCount = rows;
        columnCount = cols;
        horGap = hgap;
        vertGap = vgap;
    }

    /**
     * Calculates the preferred size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     *
     * @see #minimumLayoutSize
     */
    @Override
    public GDimension preferredLayoutSize(GComponent parent)
    {
        int count = parent.getComponentCount();
        GInsets insets = parent.getInsets();
        GDimension dim = new GDimension();
        for(int i=0;i<count;i++)
        {
            GDimension pref = parent.getComponent(i).getPreferredSize();
            dim.width = dim.width > pref.width ? dim.width : pref.width;
            dim.height = dim.height > pref.height ? dim.height : pref.height;
        }
        dim.width = (dim.width * columnCount) + ((columnCount-1) * horGap);// + insets.left + insets.right;
        int rows = rowCount;
        if(rowCount == 0)
        {
            rows = Math.round(1.0F * parent.getComponentCount() / columnCount);
        }
        dim.height = (dim.height * rows) + ((rows-1) * vertGap); // + insets.top + insets.bottom;
        return dim;
    }

    /**
     * Calculates the minimum size dimensions for the specified
     * panel given the components in the specified parent container.
     * @param parent the component to be laid out
     * @see #preferredLayoutSize
     */
    @Override
    public GDimension minimumLayoutSize(GComponent parent)
    {
        int count = parent.getComponentCount();
        GDimension dim = new GDimension();
        for(int i=0;i<count;i++)
        {
            GDimension pref = parent.getComponent(i).getMinimumSize();
            dim.width = dim.width > pref.width ? dim.width : pref.width;
            dim.height = dim.height > pref.height ? dim.height : pref.height;
        }
        return dim;
    }

    /**
     * Lays out the container in the specified panel.
     * @param parent the component which needs to be laid out
     */
    @Override
    public void layoutContainer(GComponent parent)
    {
        if(parent.getComponentCount() == 0)
        {
            return;
        }
        GDimension size = parent.getSize();
        if(size.width<1 && size.height<1)
        {
            return;
        }
        GInsets insets = parent.getInsets();
        int colWidth = size.width - insets.left - insets.right - ((columnCount-1)*horGap);
        colWidth = colWidth / columnCount;
        int rows = rowCount;
        if(rowCount == 0)
        {
            rows = Math.round(1.0F * parent.getComponentCount() / columnCount);
        }
        int rowHeight = size.height - insets.top - insets.bottom - ((rows-1)*vertGap);
        rowHeight = rowHeight / rows;
        int ypos = 0;
        int i =0;
        for(;i<parent.getComponentCount();)
        {
            int xpos = 0;
            for(int x=0;x<columnCount && i+x < parent.getComponentCount();x++)
            {
                GComponent comp = parent.getComponent(i+x);
                if(comp != null)
                {
                    comp.setBounds(xpos,ypos,colWidth,rowHeight);
                    xpos += colWidth + horGap;
                }
            }
            i += columnCount;
            ypos += rowHeight + vertGap;
        }
    }

    @Override
    public void addLayoutComponent(GComponent comp, Object constraints)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void addLayoutComponent(String name, GComponent comp)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeLayoutComponent(GComponent comp)
    {
        // TODO Auto-generated method stub

    }

}
