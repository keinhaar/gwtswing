package de.exware.gwtswing.swing;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPImageElement;

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

    static GPImageElement createImageElement(GIcon icon)
    {
        GPImageElement img = GPlatform.getDoc().createImageElement();
        img.setSrc(icon.getURL());
        img.setWidth(icon.getIconWidth());
        img.setHeight(icon.getIconHeight());
        return img;
    }
}
