package de.exware.gwtswing.awt;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GUtilities;

public class GFontMetrics
{
    private GFont font;
    private int height = -1;
    
    public GFontMetrics(GFont font)
    {
        this.font = font;
    }
    
    public int stringWidth(String text)
    {
        return (int) GPlatform.getInstance().stringWidth(font.toCSS(), text);
    }
    
    public int getHeight()
    {
        if(height == -1)
        {
            GPElement el = GUtilities.getMeasureElement();
            GLabel label = new GLabel("agZÜW");
            label.setFont(font);
            GPElement peer = label.getPeer();
            peer.getStyle().setPosition("relative");
            peer.getStyle().setDisplay("inline");
            peer.getStyle().setVisibility("visible");
            el.appendChild(peer);
            height = peer.getOffsetHeight();
            el.removeChild(peer);
        }
        return height;
    }
}
