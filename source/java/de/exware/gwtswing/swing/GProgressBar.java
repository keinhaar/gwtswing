package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.GDimension;

public class GProgressBar extends GComponent
{
    private GPElement valueElement;
    private GPElement textElement;
    private int min = 0;
    private int max = 100;
    private int value = 0;
    
    public GProgressBar()
    {
        super();
        GPElement el = getPeer();
        valueElement = GPlatform.getDoc().createDivElement();
        el.appendChild(valueElement);
        valueElement.getStyle().setWidthInPercent(30);
        valueElement.getStyle().setHeightInPercent(100);
        valueElement.addClassName("gwts-GProgressBar-indicator");
        textElement = GPlatform.getDoc().createDivElement();
        textElement.getStyle().setProperty("textAlign", "center");
        textElement.getStyle().setWidthInPercent(100);
        el.appendChild(textElement);
        setPreferredSize(new GDimension(100, new GLabel("Hallo").getPreferredSize().height + getStyleExtraHeight()));
    }
    
    public GProgressBar(int min, int max)
    {
        this();
        setMaximum(max);
    }

    public void setValue(int value)
    {
        if(value > max)
        {
            value = max;
        }
        this.value = value;
        double val = value * 100.0 / max;
        valueElement.getStyle().setWidthInPercent((int)val);
    }

    public int getValue()
    {
        return value;
    }
    
    public void setMaximum(int max)
    {
        this.max = max;
    }
    
    public void setString(String str)
    {
        textElement.setInnerText(str);
    }
    
    public void setStringPainted(boolean painted)
    {
        textElement.getStyle().setVisibility(painted ? "visible": "hidden");
    }
}
