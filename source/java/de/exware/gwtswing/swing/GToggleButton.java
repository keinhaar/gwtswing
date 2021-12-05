package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.swing.border.GBorderFactory;

public class GToggleButton extends GAbstractButton
{
    private ImageElement img;
    private GIcon icon;
    private GColor background;
    private boolean selected;

    public GToggleButton()
    {
        this("");
    }

    public GToggleButton(GAction action)
    {
        this("");
        setAction(action);
    }
    
    public GToggleButton(String text)
    {
        setText(text);
        setBorder(GBorderFactory.createLineBorder(GColor.DARK_GRAY, 1));
        background = GUIManager.getColor(".gwts-GButton/background-color");
        setBackground(background);
    }

    @Override
    protected GActionEvent fireActionEvent()
    {
        if(isEnabled())
        {
            setSelected(!selected);
        }
        return super.fireActionEvent();
    }
    
    @Override
    public void setBackground(GColor col)
    {
        super.setBackground(col);
        background = col;
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
    }
    
    public GToggleButton(GIcon icon)
    {
        this((String)null);
        setIcon(icon);
    }

    @Override
    public String getText()
    {
        Element peer = getPeer();
        return peer.getInnerText();
    }

    @Override
    public void setText(String text)
    {
        Element peer = getPeer();
        peer.setInnerHTML(text);
        if (img != null)
        {
            getPeer().insertFirst(img);
        }
        setCachedPreferredSize(null);
    }

    @Override
    public void setIcon(GIcon icon)
    {
        this.icon = icon;
        Element peer = getPeer();
        if (icon == null)
        {
            if (img != null)
            {
                img.removeFromParent();
                img = null;
            }
        }
        else
        {
            ImageElement old = img;
            img = GImageIcon.createImageElement(icon);
            if (old != null && old.getParentNode() != null)
            {
                peer.replaceChild(img, old);
            }
            else
            {
                peer.insertFirst(img);
            }
        }
        setCachedPreferredSize(null);
    }

    @Override
    public GInsets getPadding()
    {
        return super.getPadding();
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getCachedPreferredSize();
        if(dim == null)
        {
            if (img != null)
            {
                img.removeFromParent();
            }
            dim = super.getPreferredSize();
            dim.width ++;
            if (img != null)
            {
                getPeer().insertFirst(img);
                dim.width += icon.getIconWidth();
                int extraheight = getStyleExtraHeight();
                dim.height = dim.height > icon.getIconHeight() ? dim.height : icon.getIconHeight() + extraheight;
            }
        }
        return dim;
    }
    
    @Override
    public void setSelected(boolean sel)
    {
        super.setSelected(sel);
        selected = sel;
        if(selected)
        {
            GColor b = background;
            setBackground(b.darker());
            background = b;
        }
        else
        {
            setBackground(background);
        }
    }
}
