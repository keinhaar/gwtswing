package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;

public class GImageIcon implements GIcon
{
    private String url;
    private int w;
    private int h;
    
    public GImageIcon(String url, int w, int h)
    {
        this.url = url;
        this.w = w;
        this.h = h;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        boolean eq = obj == this;
        if (eq == false && obj instanceof GImageIcon)
        {
            GImageIcon other = (GImageIcon) obj;
            eq = this.url.equals(other.url) && w == other.w && h == other.h;
        }
        return eq;
    }

    @Override
    public int hashCode()
    {
        return url.hashCode();
    }

    
    @Override
    public String getURL()
    {
        return url;
    }

    @Override
    public int getIconWidth()
    {
        return w;
    }

    @Override
    public int getIconHeight()
    {
        return h;
    }

    static ImageElement createImageElement(GIcon icon)
    {
        ImageElement img = Document.get().createImageElement();
        img.setSrc(icon.getURL());
        img.setWidth(icon.getIconWidth());
        img.setHeight(icon.getIconHeight());
        return img;
    }
}
