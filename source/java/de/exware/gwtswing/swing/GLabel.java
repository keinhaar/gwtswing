package de.exware.gwtswing.swing;

import java.util.HashMap;
import java.util.Map;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPImageElement;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFontMetrics;

public class GLabel extends GComponent
{
    private GPImageElement img;
    private GIcon icon;
    private static Map<Long, GDimension> sizeCache = new HashMap<>();
    private static Boolean canDoFastMeasuring;

    public GLabel()
    {
        this("");
    }

    public GLabel(String text)
    {
        setText(text);
        setOpaque(false);
    }

    public GLabel(GIcon icon)
    {
        setText(null);
        setIcon(icon);
        setOpaque(false);
    }

    public void setHorizontalTextPosition(int pos)
    {
        if(pos == GSwingConstants.CENTER)
        {
            getPeer().getStyle().setTextAlign("center");
        }
        else
        if(pos == GSwingConstants.LEFT)
        {
            getPeer().getStyle().setTextAlign("left");
        }
        else
        if(pos == GSwingConstants.RIGHT)
        {
            getPeer().getStyle().setTextAlign("right");
        }
    }
    
//    @Override
//    public void setBounds(int x, int y, int width, int height)
//    {
//        super.setBounds(x, y, width, height);
//        Style style = getPeer().getStyle();
//        style.setLineHeight(height, Unit.PX);
//    }
//
//    @Override
//    public void setSize(int width, int height)
//    {
//        super.setSize(width, height);
//        Style style = getPeer().getStyle();
//        style.setLineHeight(height, Unit.PX);
//    }
//    
    public String getText()
    {
        GPElement peer = getPeer();
        if(img != null)
        {
            peer.removeChild(img);
        }
        String text = peer.getInnerHTML();
        {
            if(img != null)
            {
                peer.insertFirst(img);
            }
        }
        return text;
    }
    
    public void setText(String text)
    {
        GPElement peer = getPeer();
        peer.setInnerHTML(text);
        if(img != null)
        {
            getPeer().insertFirst(img);
        }
        setCachedPreferredSize(null);
    }
    
    public void setIcon(GIcon icon)
    {
        this.icon = icon;
        GPElement peer = getPeer();
        if(icon == null)
        {
            if(img != null)
            {
                img.removeFromParent();
                img = null;
            }
        }
        else
        {
            if(img != null)
            {
                img.removeFromParent();
            }
            img = GImageIcon.createImageElement(icon);
            peer.insertFirst(img);
        }
        setCachedPreferredSize(null);
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
                String text = getText();
                if(dim == null)
                {
                    if (img != null)
                    {
                        img.removeFromParent();
                    }
                    if(canDoFastMeasuring == null)
                    {
                        dim = super.getPreferredSize();
                        GDimension dim2 = new GDimension();
                        dim2.width += GPlatform.getInstance().stringWidth(getFont().toCSS(), text);
                        dim2.width += getStyleExtraWidth();
                        if(img == null && text.contains("<") == false)
                        {
                            canDoFastMeasuring = dim2.width >= dim.width - 1 && dim2.width <= dim.width +1;
                        }
                    }
                    else if(canDoFastMeasuring
                        && img == null && text.contains("<") == false)
                    {
                        dim = new GDimension();
                        dim.width += GPlatform.getInstance().stringWidth(getFont().toCSS(), text);
                        dim.width += getStyleExtraWidth();
                        dim.width ++;
                        GFontMetrics fm = getFontMetrics(getFont());
                        dim.height = fm.getHeight() + getStyleExtraHeight();
                    }
                    else
                    {
                        dim = super.getPreferredSize();
                    }
                    dim.width ++;
                    if (img != null)
                    {
                        if(text == null || text.length() == 0)
                        {
                            dim.width = getStyleExtraWidth();
                        }
                        getPeer().insertFirst(img);
                        dim.width += icon.getIconWidth();
                        int extraheight = getStyleExtraHeight();
                        dim.height = dim.height > icon.getIconHeight() ? dim.height : icon.getIconHeight() + extraheight;
                    }
                    if(img == null)
                    {
                        setCachedPreferredSize(dim);
                    }
                }
            }
        }
        return dim;
    }
    
    @Override
    public String toString()
    {
        return super.toString() + "; text="+getText();
    }
}
