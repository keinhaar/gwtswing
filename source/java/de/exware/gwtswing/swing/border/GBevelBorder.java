package de.exware.gwtswing.swing.border;

import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public class GBevelBorder implements GBorder
{
    public static final int LOWERED = 0;
    public static final int RAISED = 1;
    private int type = LOWERED;

    public GBevelBorder()
    {
    }

    public GBevelBorder(int type)
    {
        this.type = type;
    }

    @Override
    public GInsets getBorderInsets(GComponent c)
    {
        return new GInsets(2,2,2,2);
    }
    
    @Override
    public void install(GComponent component)
    {
        if(getBevelType() == RAISED)
        {
            component.getPeer().getStyle().setProperty("borderStyle","outset");
        }
        else
        {
            component.getPeer().getStyle().setProperty("borderStyle","inset");
        }
        component.getPeer().getStyle().setBorderWidth(2);
    }

    @Override
    public void uninstall(GComponent component)
    {
        component.getPeer().getStyle().clearProperty("borderWidth");
        component.getPeer().getStyle().clearProperty("borderStyle");
    }
    
    public int getBevelType()
    {
        return type;
    }
}
