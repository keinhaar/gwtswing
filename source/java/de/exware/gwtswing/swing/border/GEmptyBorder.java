package de.exware.gwtswing.swing.border;

import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;

import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;

public class GEmptyBorder implements GBorder
{
    private int top, left, bottom, right;

    public GEmptyBorder(int top, int left, int bottom, int right)
    {
        this.top = top;
        this.left = left;
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
        Style style = component.getPeer().getStyle();
        style.setBorderStyle(BorderStyle.SOLID);
//        String col = StyleSheet.get(0).getCSSRule(".gwts-GComponent").getProperty("background-color");
        style.setBorderColor("transparent");
        style.setProperty("borderTopWidth", top, Unit.PX);
        style.setProperty("borderLeftWidth", left, Unit.PX);
        style.setProperty("borderBottomWidth", bottom, Unit.PX);
        style.setProperty("borderRightWidth", right, Unit.PX);
        style.setProperty("borderRadius", 0, Unit.PX);
    }
}
