package de.exware.gwtswing.swing;

/**
 * This interface allows us to Filter Data in a GTable.
 * @author martin
 *
 */
public interface TableFilter
{
    /**
     * Should return true, if the value should NOT be filtered.
     * @param value The Value of a given column.
     * @return
     */
    public boolean accept(Object value);
}
