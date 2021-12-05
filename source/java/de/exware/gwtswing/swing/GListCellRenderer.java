package de.exware.gwtswing.swing;

public interface GListCellRenderer<T>
{
    /**
     * Im Gegensatz zur originalen Implementierung muss hier immer eine neue Komponente geliefert werden.
     * @param value
     * @param selected 
     * @param value2 
     * @return
     */
    public GComponent getListCellRendererComponent(GList<T> list, T value, boolean selected);
}
