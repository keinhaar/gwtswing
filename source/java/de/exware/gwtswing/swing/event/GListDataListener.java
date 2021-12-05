package de.exware.gwtswing.swing.event;

public interface GListDataListener
{
    void intervalAdded(GListDataEvent e);

    void intervalRemoved(GListDataEvent e);

    void contentsChanged(GListDataEvent e);
}