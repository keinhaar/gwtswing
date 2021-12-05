package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.OptionElement;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;

import de.exware.gwtswing.DefaultStringRenderer;
import de.exware.gwtswing.StringRenderer;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;

/**
 * Does not support long click!!
 * @author martin
 *
 * @param <T>
 */
public class GComboBox<T> extends GComponent
{
    private SelectElement selectElement;
    private List<GActionListener> actionListeners;
    private GComboBoxModel<T> model;
    private StringRenderer<T> renderer = new DefaultStringRenderer<>();

    public GComboBox(GComboBoxModel<T> model)
    {
        selectElement = Document.get().createSelectElement();
        Event.sinkEvents(selectElement, Event.ONCHANGE);
        Event.setEventListener(selectElement, new EventListener()
        {
            @Override
            public void onBrowserEvent(Event event)
            {
                int index = selectElement.getSelectedIndex();
                setSelectedItem(GComboBox.this.model.getElementAt(index));
                fireActionEvent();
            }
        });
        getPeer().appendChild(selectElement);
        setModel(model);
        setFont(selectElement, getFont());
    }

    public GComboBox(T[] data)
    {
        this(new GDefaultComboBoxModel<>(data));
        setSelectedItem(data[0]);
    }

    public GComboBox()
    {
        this(new GDefaultComboBoxModel<T>());
    }

    @Override
    protected boolean supportsLongClick()
    {
        return false;
    }
    
    public void setModel(GComboBoxModel<T> model)
    {
        selectElement.setSelectedIndex(-1);
        selectElement.clear();
        this.model = model;
        for(int i=0;i<model.getSize();i++)
        {
            _addItem(model.getElementAt(i));
        }
        if(model.getSize() > 0)
        {
            setSelectedItem(model.getElementAt(0));
        }
        setCachedPreferredSize(null);
    }

    private void _addItem(T item)
    {
        OptionElement opt = Document.get().createOptionElement();
        String t = renderer.toString(item);
        opt.setValue(t);
        opt.setInnerHTML(t);
        selectElement.add(opt, null);
    }
    
    public T getSelectedItem()
    {
        T item = model.getSelectedItem();
        return item;
    }
    
    public void addItem(T item)
    {
        if(model instanceof GMutableComboBoxModel)
        {
            ((GMutableComboBoxModel<T>)model).addElement(item);
            _addItem(item);
            setCachedPreferredSize(null);
        }
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        return super.getPreferredSize();
//        Element peer = getPeer();
//        DivElement div = GUtilities.getMeasureElement();
//        SelectElement span = Document.get().createSelectElement();
//        OptionElement opt = Document.get().createOptionElement();
//        opt.setLabel("AAAAAAA");
//        span.add(opt, null);
//        PixelStyle pstyle = GUtilities.getComputedStyle(peer);
//        double fontSize = pstyle.getFontSize();
//        span.getStyle().setFontSize(fontSize, Unit.PX);
//        div.appendChild(span);
//        int w = span.getOffsetWidth() + getStyleExtraWidth(pstyle);
//        int h = span.getOffsetHeight() + getStyleExtraHeight(pstyle);
//        div.removeChild(span);
//        return new GDimension(w, h);
    }

    protected void fireActionEvent()
    {
        GActionEvent evt = new GActionEvent(this);
        for(int i=0;actionListeners != null && i< actionListeners.size();i++)
        {
            actionListeners.get(i).actionPerformed(evt);
        }
    }

    public void addActionListener(GActionListener listener)
    {
        if(actionListeners == null)
        {
            actionListeners = new ArrayList<>();
            initEventListener(Event.ONCLICK);
        }
        if(actionListeners.contains(listener) == false)
        {
            actionListeners.add(listener);
        }
    }
    
    public void removeActionListener(GActionListener listener)
    {
        if(actionListeners != null)
        {
            actionListeners.remove(listener);
        }
    }

    public void removeAllItems()
    {
        if(model instanceof GMutableComboBoxModel)
        {
            GMutableComboBoxModel<T> mm = ((GMutableComboBoxModel<T>)model);
            while(mm.getSize() > 0)
            {
                mm.removeElementAt(0);
            }
        }
        selectElement.clear();
    }

    public void setSelectedItem(T selected)
    {
        model.setSelectedItem(selected);
        selectElement.setSelectedIndex(model.indexOf(selected));
    }

    public void setSelectedIndex(int index)
    {
        T item = model.getElementAt(index);
        setSelectedItem(item);
    }

    public void setRenderer(StringRenderer<T> renderer)
    {
        this.renderer  = renderer;
        setModel(model);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        selectElement.setDisabled( ! enabled);
    }

    public int getSelectedIndex()
    {
        return model.indexOf(getSelectedItem());
    }
}
