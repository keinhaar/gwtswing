package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.swing.table.GTableModel;
import de.exware.gwtswing.util.GEventObject;

public class GTableModelEvent
    extends GEventObject
{
    public static final int INSERT = 0;
    public static final int UPDATE = 1;
    public static final int DELETE = 2;
    public static final int HEADER_ROW = -1;
    private int type;
    private int firstRow;
    private int lastRow;
    private int column;

    public GTableModelEvent(GTableModel source)
    {
        this(source, 0, Integer.MAX_VALUE, 0, UPDATE);    
    }

    public GTableModelEvent(GTableModel source, int row)
    {
        this(source, row, row, 0, UPDATE);
    }

    public GTableModelEvent(GTableModel source, int firstRow, int lastRow, int column, int type) 
    {
        super(source);
        this.type = type;
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.column = column;
    }

    public int getType()
    {
        return type;
    }

    public int getFirstRow()
    {
        return firstRow;
    }

    public int getLastRow()
    {
        return lastRow;
    }

    public int getColumn()
    {
        return column;
    }
}
