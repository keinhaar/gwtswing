package de.exware.gwtswing.awt;

import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Visibility;

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
        int width = 0;
        CanvasElement ce = (CanvasElement) GUtilities.getMeasureCanvas().getPeer();
        Context2d g2d = ce.getContext2d();
        g2d.setFont(font.toCSS());
        TextMetrics tm = g2d.measureText(text);
        width = (int) tm.getWidth();
        return width;
    }
    
    public int getHeight()
    {
        if(height == -1)
        {
            DivElement el = GUtilities.getMeasureElement();
            GLabel label = new GLabel("agZÜW");
            label.setFont(font);
            Element peer = label.getPeer();
            peer.getStyle().setPosition(Position.RELATIVE);
            peer.getStyle().setDisplay(Display.INLINE);
            peer.getStyle().setVisibility(Visibility.VISIBLE);
            el.appendChild(peer);
            height = peer.getOffsetHeight();
            el.removeChild(peer);
        }
        return height;
    }
}
