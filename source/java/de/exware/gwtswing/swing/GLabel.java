package de.exware.gwtswing.swing;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.dom.client.Style.TextAlign;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFontMetrics;
import de.exware.gwtswing.awt.GGraphics2D;

public class GLabel extends GComponent
{
    private ImageElement img;
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
            getPeer().getStyle().setTextAlign(TextAlign.CENTER);
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
        Element peer = getPeer();
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
        Element peer = getPeer();
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
        Element peer = getPeer();
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
                        GGraphics2D g2d = GUtilities.getMeasureCanvas().getGraphics();
                        g2d.setFont(getFont());
                        dim2.width += g2d.stringWidth(text);
                        dim2.width += getStyleExtraWidth();
                        if(img == null && text.contains("<") == false)
                        {
                            canDoFastMeasuring = dim2.width >= dim.width - 1 && dim2.width <= dim.width +1;
                        }
                        System.out.print("Use fast Measuring: " + canDoFastMeasuring);
                    }
                    else if(canDoFastMeasuring
                        && img == null && text.contains("<") == false)
                    {
                        dim = new GDimension();
                        GGraphics2D g2d = GUtilities.getMeasureCanvas().getGraphics();
                        g2d.setFont(getFont());
                        dim.width += g2d.stringWidth(text);
                        dim.width += getStyleExtraWidth();
                        dim.width ++;
                        GFontMetrics fm = g2d.getFontMetrics();
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
