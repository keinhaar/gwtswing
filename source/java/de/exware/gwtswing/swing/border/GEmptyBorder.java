package de.exware.gwtswing.swing.border;

import de.exware.gplatform.GPStyle;
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
        GPStyle style = component.getPeer().getStyle();
        style.setBorderStyle("solid");
//        String col = GwtGPStyleSheet.get(0).getCSSRule(".gwts-GComponent").getProperty("background-color");
        style.setBorderColor("transparent");
        style.setProperty("borderTopWidth", top + "px");
        style.setProperty("borderLeftWidth", left + "px");
        style.setProperty("borderBottomWidth", bottom + "px");
        style.setProperty("borderRightWidth", right + "px");
        style.setProperty("borderRadius", 0 + "px");
    }

    @Override
    public void uninstall(GComponent component)
    {
        component.getPeer().getStyle().clearProperty("borderColor");
        component.getPeer().getStyle().clearProperty("borderStyle");
        component.getPeer().getStyle().clearProperty("borderTopWidth");
        component.getPeer().getStyle().clearProperty("borderLeftWidth");
        component.getPeer().getStyle().clearProperty("borderBottomWidth");
        component.getPeer().getStyle().clearProperty("borderRightWidth");
        component.getPeer().getStyle().clearProperty("borderRadius");
    }
    
}
