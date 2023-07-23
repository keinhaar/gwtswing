package de.exware.gwtswing.swing.border;

import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public class GEtchedBorder implements GBorder
{
    public static final int LOWERED = 0;
    public static final int RAISED = 1;
    private int type = LOWERED;

    public GEtchedBorder()
    {
    }

    public GEtchedBorder(int type)
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
        if(type == RAISED)
        {
            component.getPeer().getStyle().setProperty("borderStyle","ridge");
        }
        else
        {
            component.getPeer().getStyle().setProperty("borderStyle","groove");
        }
        component.getPeer().getStyle().setBorderWidth(2);
    }
    
    public int getEtchType()
    {
        return type;
    }
}
