package de.exware.gwtswing.swing.border;

import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public class GLineBorder implements GBorder
{
    private int thickness = 1;
    private GColor color = new GColor(0, 0, 0);
    private BorderStyle linestyle = BorderStyle.SOLID;

    public GLineBorder(GColor col)
    {
        this(col, 1);
    }

    public GLineBorder(GColor col, int thickness)
    {
        this(col, thickness, BorderStyle.SOLID);
    }

    public GLineBorder(GColor col, int thickness, BorderStyle linestyle)
    {
        this.color = col;
        this.thickness = thickness;
        this.linestyle = linestyle;
    }

    @Override
    public GInsets getBorderInsets(GComponent c)
    {
        return new GInsets(thickness,thickness,thickness,thickness);
    }
    
    @Override
    public void install(GComponent component)
    {
        component.getPeer().getStyle().setBorderStyle(linestyle);
        component.getPeer().getStyle().setBorderColor(color.toHex());
        component.getPeer().getStyle().setBorderWidth(thickness, Unit.PX);
    }
}
