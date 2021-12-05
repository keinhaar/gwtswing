package de.exware.gwtswing.swing;

public interface GMutableComboBoxModel<E> extends GComboBoxModel<E>
{
    public void addElement( E item );

    public void removeElement( E obj );

    public void insertElementAt( E item, int index );

    public void removeElementAt( int index );
}
