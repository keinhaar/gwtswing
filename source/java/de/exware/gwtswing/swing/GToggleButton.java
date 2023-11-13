package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GInsets;

public class GToggleButton extends GAbstractButton
{
    private GPElement img;
    private GIcon icon;
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
    }

    @Override
    protected void fireActionEvent()
    {
        if(isEnabled())
        {
            setSelected(!selected);
        }
        super.fireActionEvent();
    }
    
    @Override
    public void setBackground(GColor col)
    {
        super.setBackground(col);
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
        GPElement peer = getPeer();
        return peer.getInnerText();
    }

    @Override
    public void setText(String text)
    {
        GPElement peer = getPeer();
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
        GPElement peer = getPeer();
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
            GPElement old = img;
            img = GImageIcon.createImageElement(icon);
            if (old != null && old.getParentElement() != null)
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
            getPeer().addClassName("gwts-GToggleButton-active");
        }
        else
        {
            getPeer().removeClassName("gwts-GToggleButton-active");
        }
    }
}
