package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gplatform.element.GPInputElement;
import de.exware.gplatform.event.GPEvent;
import de.exware.gwtswing.awt.GToolkit;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.awt.event.GKeyEvent;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.swing.event.GPropertyChangeEvent;
import de.exware.gwtswing.swing.event.GPropertyChangeListener;

abstract public class GAbstractButton extends GComponent
{
    protected GPInputElement buttonElement;
    private List<GActionListener> actionListeners;
    private GIcon icon;
    private GAction action;
    private String actionCommand;
    private GPropertyChangeListener propertyListener;

    public GAbstractButton(GPInputElement element)
    {
        this();
        buttonElement = element;
        getPeer().appendChild(buttonElement);
    }

    protected GAbstractButton()
    {
        initEventListener(GPEvent.Type.ONCLICK
            , GPEvent.Type.ONDBLCLICK 
            , GPEvent.Type.ONKEYUP);
    }
    
    public void setAction(GAction action)
    {
        if(propertyListener == null)
        {
            propertyListener = createActionPropertyChangeListener(action);
        }
        if(this.action != null)
        {
            this.action.removePropertyChangeListener(propertyListener);
        }
        this.action = action;
        if(action != null)
        {
            action.addPropertyChangeListener(propertyListener);
            setText((String) action.getValue(action.NAME));
            setToolTipText((String) action.getValue(GAction.SHORT_DESCRIPTION));
            Object val = action.getValue(action.ICON);
            GIcon icon = (GIcon) val;
            setIcon(icon);
            setEnabled(action.isEnabled());
        }
    }
    
    protected GPropertyChangeListener createActionPropertyChangeListener(GAction a) 
    {
        return new PropertyListener();
    }

    public boolean isSelected()
    {
        return false;
    }
    
    public void setSelected(boolean sel)
    {
    }
    
    @Override
    public void handleEvent(GAWTEvent event)
    {
        super.handleEvent(event);
        if(event instanceof GMouseEvent)
        {
            GMouseEvent evt = (GMouseEvent) event;
            if(evt.getId() == GMouseEvent.MOUSE_CLICKED)
            {
                fireActionEvent();
            }
        }
        else if(event instanceof GKeyEvent)
        {
            GKeyEvent evt = (GKeyEvent) event;
            if(evt.getId() == GKeyEvent.KEY_RELEASED
                && evt.getKeyCode() == GKeyEvent.VK_SPACE)
            {
                fireActionEvent();
            }
        }
    }
    
    protected void fireActionEvent()
    {
        if(isEnabled())
        {
            GActionEvent evt = new GActionEvent(this, actionCommand);
            for(int i=0;actionListeners != null && i< actionListeners.size();i++)
            {
                actionListeners.get(i).actionPerformed(evt);
            }
            if(action != null)
            {
                action.actionPerformed(evt);
            }
            GToolkit.getDefaultToolkit().handleAWTEvent(evt);
        }
    }

    public void addActionListener(GActionListener listener)
    {
        if(actionListeners == null)
        {
            actionListeners = new ArrayList<>();
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
    
    public String getText()
    {
        return buttonElement.getValue();
    }
    
    public void setText(String text)
    {
        buttonElement.setValue(text);
    }

    public String getActionCommand()
    {
        return actionCommand;
    }

    public void setActionCommand(String actionCommand)
    {
        this.actionCommand = actionCommand;
    }

    public void doClick()
    {
        fireActionEvent();
    }

    protected GAction getAction()
    {
        return action;
    }
    
    class PropertyListener
        implements GPropertyChangeListener
    {
        @Override
        public void propertyChanged(GPropertyChangeEvent evt)
        {
            if (evt.getProperty() == GAction.ENABLED_PROPERTY)
            {
                GIcon icon = (GIcon) ((GAction) evt.getSource()).getValue(GAction.ICON);
                if (icon != null)
                {
                    setIcon(icon);
                }
                setEnabled((Boolean) evt.getValue());
            }
            if (evt.getProperty() == GAction.SHORT_DESCRIPTION)
            {
                setToolTipText((String) action.getValue(GAction.SHORT_DESCRIPTION));
            }
        }
    }

    public void setIcon(GIcon icon)
    {
        this.icon = icon;
    }
    
    public GIcon getIcon()
    {
        return icon;
    }
}
