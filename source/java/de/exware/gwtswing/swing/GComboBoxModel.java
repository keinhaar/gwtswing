package de.exware.gwtswing.swing;

public interface GComboBoxModel<E> extends GListModel<E>
{
    void setSelectedItem(E anItem);

    E getSelectedItem();
}
