package de.exware.gwtswing.awt;

import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;

import de.exware.gwtswing.swing.GComponent;

public class GCanvas extends GComponent
{
    public GCanvas()
    {
        super(Document.get().createCanvasElement());
    }
    
    public GGraphics2D getGraphics()
    {
        CanvasElement ce = (CanvasElement) getPeer();
        return new GGraphics2D(ce.getContext2d());
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
        CanvasElement ce = (CanvasElement) getPeer();
        ce.setWidth(width);
        ce.setHeight(height);
    }
}
