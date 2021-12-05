package de.exware.gwtswing.swing;

import de.exware.gwtswing.swing.event.GListDataListener;

public interface GListModel<T>
{
    public int getSize();
    
    public T getElementAt(int index);
    
    int indexOf(T el);

    void addListDataListener(GListDataListener l);

    void removeListDataListener(GListDataListener l);
}
