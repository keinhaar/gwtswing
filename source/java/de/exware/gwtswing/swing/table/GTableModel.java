package de.exware.gwtswing.swing.table;

import de.exware.gwtswing.swing.event.GTableModelListener;

public interface GTableModel
{
    public int getColumnCount();
    public String getColumnName(int col);
    public Class<?> getColumnClass(int col);
    public int getRowCount();
    public void addTableModelListener(GTableModelListener listener);
    public void removeTableModelListener(GTableModelListener listener);
    public Object getValueAt(int row, int column);
}