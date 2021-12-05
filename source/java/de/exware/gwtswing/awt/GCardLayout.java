package de.exware.gwtswing.awt;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.swing.GComponent;

public class GCardLayout implements GLayoutManager
{
    private int current = -1;
    private List<String> names = new ArrayList<>();
    
    /**
     * Adds the specified component with the specified name to the layout.
     * 
     * @param name
     *            the component name
     * @param comp
     *            the component to be added
     */
    @Override
    public void addLayoutComponent(String name, GComponent comp)
    {
        names.add(name);
    }

    /**
     * Removes the specified component from the layout.
     * 
     * @param comp
     *            the component to be removed
     */
    @Override
    public void removeLayoutComponent(GComponent comp)
    {
    }

    public void show(GComponent comp, String name)
    {
        for(int i=0;i<names.size();i++)
        {
            if(names.get(i).equals(name))
            {
                current = i;
                comp.revalidate();
            }
        }
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
        int count = parent.getComponentCount();
        GDimension dim = new GDimension();
        GInsets insets = parent.getInsets();
        for (int i = 0; i < count; i++)
        {
            GDimension pref = parent.getComponent(i).getPreferredSize();
            dim.width = dim.width > pref.width ? dim.width : pref.width;
            dim.height = dim.height > pref.height ? dim.height : pref.height;
        }
        dim.width += insets.left + insets.right;
        dim.height += insets.top + insets.bottom;
        return dim;
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
        int count = parent.getComponentCount();
        for (int i = 0; i < count; i++)
        {
            GComponent comp = parent.getComponent(i);
            GInsets insets = parent.getInsets();
            GDimension dim = parent.getSize();
            if(i == current)
            {
                comp.setBounds(0, 0, dim.width - insets.left - insets.right,
                    dim.height - insets.top - insets.bottom);
                comp.setVisible(true);
            }
            else
            {
                comp.setBounds(insets.left, insets.top, 0,0);
                comp.setVisible(false);
            }
        }
    }

    @Override
    public void addLayoutComponent(GComponent comp, Object constraints)
    {
        addLayoutComponent((String)constraints, comp);
        if(names.size() == 1)
        {
            current = 0;
        }
    }
}
