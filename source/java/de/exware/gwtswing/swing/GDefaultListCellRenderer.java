package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GColor;

public class GDefaultListCellRenderer<T> implements GListCellRenderer<T>
{
    @Override
    public GComponent getListCellRendererComponent(GList<T> list, T value, boolean selected)
    {
        GLabel label = new GLabel(value == null ? "" : value.toString());
        label.setOpaque(selected);
        if(selected)
        {
            label.setBackground(new GColor(200,200,255));
        }
        return label;
    }
}
