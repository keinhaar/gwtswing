package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.swing.border.GBorderFactory;

public class GScrollPane extends GComponent
{
    private static int scrollbarWidth;
    private GPElement innerDiv;
    private GDimension compDimensions;
    
    static
    {
        GPElement div = GPlatform.getDoc().createDivElement();
        div.getStyle().setOverflow("scroll");
        div.getStyle().setWidth(100);
        div.getStyle().setHeight(100);
        GPElement idiv = GPlatform.getDoc().createDivElement();
        idiv.getStyle().setWidth(200);
        idiv.getStyle().setHeight(200);
        div.appendChild(idiv);
        GPlatform.getDoc().getBody().appendChild(div);
        int w = div.getClientWidth();
        int ow = div.getOffsetWidth();
        scrollbarWidth = ow - w;
        div.removeFromParent();
    }
    
    public GScrollPane(GComponent comp)
    {
        setViewportView(comp);
        /*innerDiv = GPlatform.getDoc().createDivElement();
        innerDiv.addClassName("gwts-GScrollPane-Viewport");
        getPeer().appendChild(innerDiv);
        setPreferredSize(new GDimension(200,100));
        add(comp);
        innerDiv.appendChild(comp.getPeer());
        setBorder(GBorderFactory.createLineBorder(GColor.DARK_GRAY, 1));*/
    }
    
    public void setViewportView(GComponent comp) {
        if(innerDiv != null) {
            innerDiv.removeFromParent();
        }

        setPreferredSize(new GDimension(200,100));

        if(comp == null) {
            return;
        }

        innerDiv = GPlatform.getDoc().createDivElement();
        innerDiv.addClassName("gwts-GScrollPane-Viewport");
        getPeer().appendChild(innerDiv);
        add(comp);
        innerDiv.appendChild(comp.getPeer());
        setBorder(GBorderFactory.createLineBorder(GColor.DARK_GRAY, 1));
    }

    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        innerDiv.getStyle().setVisibility(visible ? "visible" : "hidden");
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = super.getPreferredSize();
        return dim;
    }

    @Override
    public void validate()
    {
        super.validate();
        GComponent comp = getComponent(0);
        GDimension dim = comp.getPreferredSize();
        if(dim.equals(compDimensions) == false)
        {
            refitContent();
        }
    }
    
    public void refitContent()
    {
        GPoint p = getLocation();
        GDimension dim = getSize();
        GInsets insets = getInsets();
        int width = dim.width;
        int height = dim.height;
        GComponent comp = getComponent(0);
        if(width > 0)
        {
            dim = comp.getPreferredSize();
            int inh = insets.left + insets.right;
            int inv = insets.top + insets.bottom;
            int iw = width - inh;
            int ih = height - inv;
            innerDiv.getStyle().setWidth(iw);
            innerDiv.getStyle().setHeight(ih);
            int w = dim.width > iw ? dim.width : iw;
            int h = dim.height > ih ? dim.height : ih;
            if(dim.width < iw)
            {
                if(dim.height > ih)
                {
                    w = iw - scrollbarWidth;
                }
            }
            if(dim.height < ih)
            {
                if(dim.width > iw)
                {
                    h = ih - scrollbarWidth;
                }
            }
            comp.setBounds(0, 0, w, h);
            compDimensions = new GDimension(w,h);
        }
        else
        {
            comp.setBounds(0, 0, 0, 0);
            innerDiv.getStyle().setWidth(0);
            innerDiv.getStyle().setHeight(0);
//            compDimensions = new GDimension(0,0);
        }
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
        refit();
    }

    private void refit()
    {
        GComponent comp = getComponent(0);
        comp.validate();
        refitContent();
    }
    
    @Override
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        refit();
    }
    
    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        refit();
    }
    
    @Override
    public void setSize(GDimension dim)
    {
        super.setSize(dim);
        refit();
    }

    public int getVerticalScrollValue()
    {
        String value = innerDiv.getPropertyString("scrollTop");
        int ret = 0;
        if(value != null)
        {
            ret = Integer.parseInt(value);
        }
        return ret;
    }
}