package de.exware.gwtswing.swing;

public class GDefaultListCellRenderer<T> implements GListCellRenderer<T>
{
    @Override
    public GComponent getListCellRendererComponent(GList<T> list, T value, boolean selected)
    {
        GLabel label = new GLabel(value == null ? "" : value.toString());
        label.setOpaque(selected);
        if(selected)
        {
            label.setBackground(GUIManager.getColor(".gwts-GList-selected/background-color"));
        }
        return label;
    }
}
