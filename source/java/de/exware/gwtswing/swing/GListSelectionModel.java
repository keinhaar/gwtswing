package de.exware.gwtswing.swing;

import de.exware.gwtswing.swing.event.GListSelectionListener;

public interface GListSelectionModel
{
    public static final int SINGLE_SELECTION = 0;
    public static final int MULTIPLE_INTERVAL_SELECTION = 2;

    void addListSelectionListener(GListSelectionListener listener);

    void removeListSelectionListener(GListSelectionListener listener);

    void setSelectionMode(int selectionMode);

    void addSelectionInterval(int from, int to);
    void setSelectionInterval(int from, int to);
    void clearSelection();
}
