package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GInsets;

public class GMenuItem extends GButton
{
    public GMenuItem(String text)
    {
        super();
        setText(text);
    }

    public GMenuItem(GAction action)
    {
        super(action);
    }
    
    @Override
    public GInsets getPadding()
    {
        return super.getPadding();
    }
    
    @Override
    protected void fireActionEvent()
    {
        super.fireActionEvent();
        GComponent parent = this;
        while(parent != null && parent instanceof GPopupMenu == false)
        {
            parent = parent.getParent();
        }
        if(parent instanceof GPopupMenu)
        {
            ((GPopupMenu)parent).setVisible(false);
        }
    }
}
