package de.exware.gwtswing.swing.border;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public class SelectiveLineBorder implements GBorder
{
    private GColor color;
    private int left;
    private int top;
    private int right;
    private int bottom;

    public SelectiveLineBorder(GColor col, int top, int left, int bottom, int right)
    {
        this.color = col;
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
    }

    @Override
    public GInsets getBorderInsets(GComponent c)
    {
        return new GInsets(top, left, bottom, right);
    }
    
    @Override
    public void install(GComponent component)
    {
        Element peer = component.getPeer();
        Style style = peer.getStyle();
        style.setBorderStyle(BorderStyle.SOLID);
        style.setBorderColor(color.toHex());
        style.setProperty("borderTopWidth", top, Unit.PX);
        style.setProperty("borderLeftWidth", left, Unit.PX);
        style.setProperty("borderBottomWidth", bottom, Unit.PX);
        style.setProperty("borderRightWidth", right, Unit.PX);
        style.setProperty("borderRadius", 0, Unit.PX);
    }
}
