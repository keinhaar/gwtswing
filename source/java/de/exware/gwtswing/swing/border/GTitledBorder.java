package de.exware.gwtswing.swing.border;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.GFontMetrics;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GUIManager;

public class GTitledBorder implements GBorder
{
	private GColor color;
	private GFont font;
	private int width = 2;
	private int titleHeight;
	private String title;
    private String style;
	
    public GTitledBorder(String title)
    {
        GLabel gl = new GLabel();
        color = GUIManager.getColor(".gwts-GTitledBorder/color");
        font = gl.getFont();
        GFontMetrics metrics = new GFontMetrics(font);
        titleHeight = metrics.getHeight();
        this.title = title;
        style = "solid";
    }

    public GTitledBorder(GBorder border, String title)
    {
        this(title);
        GInsets insets = border.getBorderInsets(null);
        width = insets.left;
        if(border instanceof GBevelBorder)
        {
            color = GColor.NONE;
            GBevelBorder b = (GBevelBorder) border;
            if(b.getBevelType() == GBevelBorder.LOWERED)
            {
                style = "inset";
            }
            else
            {
                style = "outset";
            }
        }
        else if(border instanceof GEtchedBorder)
        {
            color = GColor.NONE;
            GEtchedBorder b = (GEtchedBorder) border;
            if(b.getEtchType() == GBevelBorder.LOWERED)
            {
                style = "groove";
            }
            else
            {
                style = "ridge";
            }
        }
        else if(border instanceof GLineBorder)
        {
            GLineBorder b = (GLineBorder) border;
            color = b.getLineColor();
            width = b.getThickness();
        }
    }

    public void setColor(GColor color)
    {
        this.color = color;
    }
    
    public GColor getColor()
    {
        return color;
    }
    
    public void setFont(GFont font)
    {
        this.font = font;
    }
    
    
    
    @Override
    public GInsets getBorderInsets(GComponent c)
    {
        return new GInsets(titleHeight, width, width, width);
    }
    
    @Override
    public void install(GComponent component)
    {
        component.getPeer().getStyle().setBorderWidth(width);
        component.getPeer().getStyle().setBorderColor(color.toHex());
        component.getPeer().getStyle().setBorderStyle(style);
        component.getPeer().getStyle().setOverflow("unset");
        component.getPeer().getStyle().setProperty("borderTopWidth", titleHeight + "px");
        component.getPeer().getStyle().setProperty("borderTopColor", component.getBackground().toHex());
        
        GPElement divB = GPlatform.getDoc().createDivElement();
        divB.getStyle().setPosition("absolute");
        divB.getStyle().setTop(-titleHeight);
        divB.getStyle().setLeft(-width);
        divB.getStyle().setProperty("width", "calc(100% + " + (width*2) + "px");
        divB.getStyle().setHeight(titleHeight);
        divB.getStyle().setBackgroundColor(component.getBackground().toHex());
        component.getPeer().appendChild(divB);

        GPElement divT = GPlatform.getDoc().createDivElement();
        divT.getStyle().setPosition("absolute");
        divT.getStyle().setTop(-titleHeight/2-width/2);
        divT.getStyle().setLeft(-width);
        divT.getStyle().setProperty("width", "calc(100%)");
        divT.getStyle().setHeight(titleHeight/2);
        divT.getStyle().setBackgroundColor(component.getBackground().toHex());        
        divT.getStyle().setProperty("borderLeft", style + " " + width + "px " + color.toHex());
        divT.getStyle().setProperty("borderTop", style + " " + width + "px " + color.toHex());
        divT.getStyle().setProperty("borderRight", style + " " + width + "px " + color.toHex());
        component.getPeer().appendChild(divT);

        GPElement div = GPlatform.getDoc().createDivElement();
        div.setInnerText(title);
        div.getStyle().setPosition("absolute");
        div.getStyle().setTop(-titleHeight);
        div.getStyle().setLeft(5);
        div.getStyle().setPaddingLeft(4);
        div.getStyle().setPaddingRight(4);
        div.getStyle().setColor(color.toHex());
        div.getStyle().setBackgroundColor(component.getBackground().toHex());
        component.getPeer().appendChild(div);
    }
}
