package de.exware.gwtswing.swing.table;

import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GTable;

public interface GTableCellRenderer<T> 
{
    GComponent getTableCellRendererComponent(GTable<T> table, Object value,
                                            boolean isSelected, boolean hasFocus,
                                            int row, int column);
}
