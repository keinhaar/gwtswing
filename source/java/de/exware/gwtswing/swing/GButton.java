package de.exware.gwtswing.swing;

import java.util.Objects;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.element.GPImageElement;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;

public class GButton extends GAbstractButton
{
    private GPImageElement img;

    public GButton()
    {
        this("");
    }

    public GButton(GAction action)
    {
        this("");
        setAction(action);
    }
    
    public GButton(String text)
    {
        setText(text);
        setActionCommand(text);
        addMouseListener(new GMouseAdapter()
        {
            @Override
            public void mousePressed(GMouseEvent evt)
            {
                super.mousePressed(evt);
                getPeer().addClassName("gwts-GButton-active");
            }
            
            @Override
            public void mouseReleased(GMouseEvent evt)
            {
                super.mouseReleased(evt);
                getPeer().removeClassName("gwts-GButton-active");
            }
        });
        getPeer().setTabIndex(0);
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
        if(img != null)
        {
            if(enabled)
            {
                img.removeClassName("disabled");
            }
            else
            {
                img.addClassName("disabled");
            }
        }
    }
    
    public GButton(GIcon icon)
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
        if(Objects.equals(icon, getIcon()) == false)
        {
            super.setIcon(icon);
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
                GPImageElement old = img;
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
    }

    @Override
    public GInsets getPadding()
    {
        return super.getPadding();
    }
    
    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
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
                GIcon icon = getIcon();
                dim.width += icon.getIconWidth();
                int extraheight = getStyleExtraHeight();
                dim.height = dim.height > icon.getIconHeight() ? dim.height : icon.getIconHeight() + extraheight;
            }
        }
        return dim;
    }
}
