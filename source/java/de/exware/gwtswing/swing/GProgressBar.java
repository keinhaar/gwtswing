package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;

import de.exware.gwtswing.awt.GDimension;

public class GProgressBar extends GComponent
{
    private Element valueElement;
    private Element textElement;
    private int min = 0;
    private int max = 100;
    private int value = 0;
    
    public GProgressBar()
    {
        super();
        Element el = getPeer();
        valueElement = Document.get().createDivElement();
        el.appendChild(valueElement);
        valueElement.getStyle().setWidth(30, Unit.PCT);
        valueElement.getStyle().setHeight(100, Unit.PCT);
        valueElement.addClassName("gwts-GProgressBar-indicator");
        textElement = Document.get().createDivElement();
        textElement.getStyle().setProperty("textAlign", "center");
        textElement.getStyle().setWidth(100, Unit.PCT);
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
        double val = value * 100 / max;
        valueElement.getStyle().setWidth(val, Unit.PCT);
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
        textElement.getStyle().setVisibility(painted ? Visibility.VISIBLE: Visibility.HIDDEN);
    }
}
