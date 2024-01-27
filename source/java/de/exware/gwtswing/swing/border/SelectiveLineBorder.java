package de.exware.gwtswing.swing.border;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPStyle;
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
        GPElement peer = component.getPeer();
        GPStyle style = peer.getStyle();
        style.setBorderStyle("solid");
        style.setBorderColor(color.toHex());
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
