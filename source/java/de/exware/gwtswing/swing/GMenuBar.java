package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;

public class GMenuBar extends GComponent
{
    private GGridBagConstraints gbc = new GGridBagConstraints();
    
    public GMenuBar()
    {
        setLayout(new GGridBagLayout());
        gbc.gridx = 1;
        gbc.gridy = 1;
    }
    
    public GMenu add(GMenu menu)
    {
        super.add(menu, gbc);
        gbc.gridx++;
        return menu;
    }

    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        if(constraints != gbc)
        {
            constraints = gbc;
            gbc.gridx++;
        }
        super.addImpl(comp, constraints, index);
    }
    
    public int getMenuCount() 
    {
        return getComponentCount();
    }
    
    public GMenu getMenu(int i)
    {
        return (GMenu) getComponent(i);
    }
}
