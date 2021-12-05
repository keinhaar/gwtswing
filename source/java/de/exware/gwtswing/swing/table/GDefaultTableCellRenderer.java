package de.exware.gwtswing.swing.table;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GTable;
import de.exware.gwtswing.swing.border.GBorderFactory;

public class GDefaultTableCellRenderer implements GTableCellRenderer
{
    @Override
    public GComponent getTableCellRendererComponent(GTable table, Object value,
        boolean isSelected, boolean hasFocus,
        int row, int column)
    {        
        GLabel label = new GLabel(value == null ? "" : value.toString());
        label.setOpaque(isSelected);
        if(isSelected)
        {
            label.setBackground(new GColor(200,200,255));
        }
        label.setBorder(GBorderFactory.createEmptyBorder(4, 0, 2, 0));
        return label;
    }
}
