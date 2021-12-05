package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.RepeatingCommand;
import com.google.gwt.dom.client.Element;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseListener;
import de.exware.gwtswing.swing.event.GListDataEvent;
import de.exware.gwtswing.swing.event.GListDataListener;
import de.exware.gwtswing.swing.event.GListSelectionEvent;
import de.exware.gwtswing.swing.event.GListSelectionListener;

public class GList<T> extends GComponent
{
    private GListCellRenderer<T> renderer = new GDefaultListCellRenderer<>();
    private GListModel<T> model;
    private int visibleRowCount = 8;
    private int preferredHeight;
    private int preferredWidth;
    private List<GComponent> renderedItems = new ArrayList<>();
    private List<GListSelectionListener> listSelectionListeners;
    private List<Integer> selectedItems = new ArrayList<>();
    protected GList<T>.ClickHandler clickHandler;
    private GListDataListener listener = new GListDataListener()
    {
        
        @Override
        public void intervalRemoved(GListDataEvent e)
        {
            setModel(model);
        }
        
        @Override
        public void intervalAdded(GListDataEvent e)
        {
            setModel(model);
        }
        
        @Override
        public void contentsChanged(GListDataEvent e)
        {
            setModel(model);
        }
    };

    public GList(T[] data)
    {
        setListData(data);
    }

    public GList()
    {
    }

    public GList(GDefaultListModel<T> model)
    {
        setModel(model);
    }

    public void addListSelectionListener(GListSelectionListener listener)
    {
        if(listSelectionListeners == null)
        {
            listSelectionListeners = new ArrayList<>();
        }
        listSelectionListeners.add(listener);
    }
    
    public void setListData(final T[] data)
    {        
        GListModel<T> model = new GDefaultListModel<>(data);
        setModel(model);
    }
    
    public void setModel(GListModel<T> model)
    {
        if(this.model != null)
        {
            this.model.removeListDataListener(listener);
        }
        this.model = model;
        model.addListDataListener(listener);
        getPeer().removeAllChildren();
        renderedItems.clear();
        selectedItems.clear();
        indexesToRevalidate.clear();
        preferredHeight = 0;
        preferredWidth = 0;
        for(int i=0;i<model.getSize();i++)
        {
            GComponent comp = renderer.getListCellRendererComponent(this, model.getElementAt(i), false);
            comp.putClientProperty("value", model.getElementAt(i));
            comp.addMouseListener(selectionListener);
            comp.setOpaque(false);
            renderedItems.add(comp);
            getPeer().appendChild(comp.getPeer());
        }
        setCachedPreferredSize(null);
        validate();
        GComponent parent = getParent();
        if(parent instanceof GScrollPane)
        {
            ((GScrollPane)parent).refitContent();
        }
    }

    @Override
    public void validate()
    {
        super.validate();
        preferredHeight = 0;
        preferredWidth = 0;
        GDimension size = getSize();
        for(int i=0;i<renderedItems.size();i++)
        {
            GComponent comp = renderedItems.get(i);
            GDimension dim = comp.getPreferredSize();
            comp.setBounds(0, preferredHeight, size.width, dim.height);
            preferredHeight += dim.height;
            if(preferredWidth < dim.width)
            {
                preferredWidth = dim.width;
            }
        }
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getExplizitPreferredSize();
        if(dim == null)
        {
            dim = getCachedPreferredSize();
            if(dim == null)
            {
                int w = preferredWidth + getStyleExtraWidth();
                int h = preferredHeight + getStyleExtraHeight();
                dim = new GDimension(w, h);
                setCachedPreferredSize(dim);
            }
        }
        return dim;
    }

    private List<Integer> indexesToRevalidate = new ArrayList<>();
    
    private GMouseListener selectionListener = new GMouseAdapter()
    {
        @Override
        public void mouseClicked(GMouseEvent evt) 
        {
            if(evt.getClickCount() == 1)
            {
                if(clickHandler == null)
                {
                    int current = renderedItems.indexOf(evt.getSource());
                    boolean selected = selectedItems.contains(current);
                    for(int i=0;i<selectedItems.size();i++)
                    {
                        indexesToRevalidate.add(selectedItems.get(i));
                    }
                    selectedItems.clear();
                    if(!selected)
                    {
                        selected = true;
                        selectedItems.add(current);
                    }
                    indexesToRevalidate.add(current);
                
                    clickHandler = new ClickHandler();
                    Scheduler.get().scheduleFixedDelay(clickHandler, 300);
                }
            }
        };
    };

    class ClickHandler implements RepeatingCommand
    {
        private boolean canceled;
        
        public ClickHandler()
        {
        }
        
        public void cancel()
        {
            canceled = true;
            clickHandler = null;
        }
        
        @Override
        public boolean execute()
        {
            if(canceled == false)
            {
                for(int i=0;i<indexesToRevalidate.size();i++)
                {
                    revalidateRenderedItem(indexesToRevalidate.get(i));
                }
                if(listSelectionListeners != null)
                {
                    GListSelectionEvent tevt = new GListSelectionEvent(GList.this);
                    for(int i=0;i<listSelectionListeners.size();i++)
                    {
                        listSelectionListeners.get(i).valueChanged(tevt);
                    }
                }
                validate();
                GUtilities.clearSelection();
            }
            clickHandler = null;
            return false;
        }
    }
    
    public int getSelectedIndex()
    {
        if(selectedItems.size() > 0)
        {
            return selectedItems.get(0);
        }
        return -1;
    }
    
    private void revalidateRenderedItem(int index)
    {
        GComponent comp = renderedItems.get(index);
        T value = (T) comp.getClientProperty("value");
        int current = index;
        boolean selected = selectedItems.contains(current);
        Element previous = null;
        if(current > 0)
        {
            GLabel c = (GLabel) renderedItems.get(current-1);
            previous = c.getPeer();
        }
        comp.getPeer().removeFromParent();
        comp.removeMouseListener(selectionListener);
        comp = renderer.getListCellRendererComponent(GList.this, value, selected);
        comp.putClientProperty("value", value);
        comp.addMouseListener(selectionListener);
        renderedItems.set(current, comp);
        getPeer().insertAfter(comp.getPeer(), previous);
    }
    
    public GListCellRenderer getCellRenderer()
    {
        return renderer;
    }

    public void setCellRenderer(GListCellRenderer renderer)
    {
        this.renderer = renderer;
        if(model != null)
        {
            setModel(model);
        }
    }

    public List<T> getSelectedValuesList()
    {
        List<T> items = new ArrayList<>();
        for(int i=0;i<selectedItems.size();i++)
        {
            items.add(model.getElementAt(selectedItems.get(i)));
        }
        return items;
    }
}
