package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gplatform.timer.AbstractGPTimerTask;
import de.exware.gplatform.timer.GPTimer;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.GRectangle;
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
            if(e.getIndex0() == 0 && e.getIndex1() == 0)
            {
                setModel(model);
            }
            else
            {
                revalidate();
            }
        }
    };

    public GList(T[] data)
    {
        setListData(data);
        addMouseListener(selectionListener);
    }

    public GList()
    {
        addMouseListener(selectionListener);
    }

    public GList(GDefaultListModel<T> model)
    {
        setModel(model);
        addMouseListener(selectionListener);
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
    
    public GListModel<T> getModel()
    {
        return model;
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
        int maxWidth = getWidth();
        for(int i=0;i<renderedItems.size();i++)
        {
            GComponent comp = renderedItems.get(i);
            comp.setMaxWidthForPreferredSize(maxWidth > 0 ? maxWidth: Integer.MAX_VALUE);
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
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
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
                    int current = locationToIndex(evt.getPoint());
                    boolean selected = selectedItems.contains(current);
                    indexesToRevalidate.clear();
                    indexesToRevalidate.addAll(selectedItems);
                    selectedItems.clear();
                    if(!selected)
                    {
                        selected = true;
                        selectedItems.add(current);
                    }
                    indexesToRevalidate.add(current);
                    clickHandler = new ClickHandler();
                    GPTimer timer = GPlatform.getInstance().createTimer();
                    timer.schedule(clickHandler, 50);
                }
            }
        };
    };

    class ClickHandler extends AbstractGPTimerTask
    {
        public ClickHandler()
        {
        }
        
        @Override
        public void cancel()
        {
            clickHandler = null;
        }
        
        @Override
        public void execute()
        {
            if(isCanceled() == false)
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
        GPElement previous = null;
        if(current > 0)
        {
            GLabel c = (GLabel) renderedItems.get(current-1);
            previous = c.getPeer();
        }
        comp.getPeer().removeFromParent();
        comp = renderer.getListCellRendererComponent(GList.this, value, selected);
        comp.putClientProperty("value", value);
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
            revalidate();
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

    public void setSelectedIndex(int index)
    {
        indexesToRevalidate.clear();
        indexesToRevalidate.addAll(selectedItems);
        indexesToRevalidate.add(index);
        selectedItems.clear();
        selectedItems.add(index);
        for(int i=0;i<indexesToRevalidate.size();i++)
        {
            revalidateRenderedItem(indexesToRevalidate.get(i));
        }
        validate();
    }

    public int locationToIndex(GPoint start)
    {
        for(int i=0;i<renderedItems.size();i++)
        {
            GComponent comp = renderedItems.get(i);
            GRectangle rect = comp.getBounds();
            if(rect.contains(start))
            {
                return i;
            }
        }
        return -1;
    }
}

