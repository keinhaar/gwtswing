package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

public class GDefaultComboBoxModel<E> extends GAbstractListModel<E>
    implements GMutableComboBoxModel<E>
{
    private E selectedItem;
    private List<E> items = new ArrayList<>();
    
    public GDefaultComboBoxModel()
    {
    }
    
    public GDefaultComboBoxModel(E[] el)
    {
        for(int i=0;i<el.length;i++)
        {
            addElement(el[i]);
        }
    }
    
    @Override
    public int getSize()
    {
        return items.size();
    }

    @Override
    public E getElementAt(int index)
    {
        return items.get(index);
    }

    @Override
    public void setSelectedItem(E anItem)
    {
        selectedItem = anItem;
    }

    @Override
    public E getSelectedItem()
    {
        return selectedItem;
    }

    @Override
    public void addElement(E item)
    {
        items.add(item);
    }

    @Override
    public void removeElement(E obj)
    {
        items.remove(obj);
    }

    @Override
    public void insertElementAt(E item, int index)
    {
        items.add(index, item);
    }

    @Override
    public void removeElementAt(int index)
    {
        items.remove(index);
    }

    @Override
    public int indexOf(E el)
    {
        return items.indexOf(el);
    }
}
