package de.exware.gwtswing.awt;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPCanvasElement;
import de.exware.gwtswing.swing.GComponent;

public class GCanvas extends GComponent
{
    public GCanvas()
    {
        super(GPlatform.getDoc().createCanvasElement());
    }
    
    public GGraphics2D getGraphics()
    {
        GPCanvasElement ce = (GPCanvasElement) getPeer();
        return new GGraphics2D(ce.getContext2d());
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
        GPCanvasElement ce = (GPCanvasElement) getPeer();
        ce.setWidth(width);
        ce.setHeight(height);
    }
}
