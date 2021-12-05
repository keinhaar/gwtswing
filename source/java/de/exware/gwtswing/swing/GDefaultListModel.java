package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

public class GDefaultListModel<T> extends GAbstractListModel<T>
    implements GListModel<T>
{
    private List<T> list = new ArrayList<>();
    
    public GDefaultListModel()
    {
    }
    
    public GDefaultListModel(List<T> data)
    {
        list.addAll(data);
    }
    
    public GDefaultListModel(T[] data)
    {
        for(int i=0;i<data.length;i++)
        {
            list.add(data[i]);
        }
    }
    
    @Override
    public int getSize()
    {
        return list.size();
    }

    @Override
    public T getElementAt(int index)
    {
        return list.get(index);
    }

    @Override
    public int indexOf(Object el)
    {
        return list.indexOf(el);
    }

    public void removeAllElements()
    {
        list.clear();
        fireContentsChanged();
    }

    public void addElement(T item)
    {
        list.add(item);
        fireIntervalAdded(list.size()-1, list.size()-1);
    }

    public void removeElement(T item)
    {
        int index = list.indexOf(item);
        list.remove(item);
        fireIntervalRemoved(index, index);
    }

    public boolean contains(T item)
    {
        return list.contains(item);
    }

    public List<T> getData()
    {
        return new ArrayList<>(list);
    }
}
