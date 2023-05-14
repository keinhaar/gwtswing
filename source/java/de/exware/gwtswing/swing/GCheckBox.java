package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.GDimension;

public class GCheckBox extends GAbstractButton
{
    private GPElement labelElement;
    private GPElement textElement;
    
    public GCheckBox(String text)
    {
        super(GPlatform.getDoc().createCheckInputElement());
        getPeer().removeChild(buttonElement);
        labelElement = GPlatform.getDoc().createElement("label");
        textElement = GPlatform.getDoc().createElement("div");
        getPeer().appendChild(labelElement);
        labelElement.appendChild(buttonElement);
        labelElement.appendChild(textElement);
        setText(text);
    }
    
    public GCheckBox()
    {
        this("");
    }

    @Override
    public boolean isSelected()
    {
        return buttonElement.isChecked();
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        buttonElement.setDisabled(!enabled);
    }
    
    @Override
    public void setSelected(boolean sel)
    {
        buttonElement.setChecked(sel);
    }
    
    @Override
    public void setText(String text)
    {
        textElement.setInnerHTML(text);
    }
    
    @Override
    public String getText()
    {
        return textElement.getInnerHTML();
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getCachedPreferredSize();
        if(dim == null)
        {
            GPElement div = GUtilities.getMeasureElement();
            // SpanElement span = Document.get().createSpanElement();
            // span.setInnerText(getText());
            // double fontSize = getFont().getSize2D();
            // span.getStyle().setFontSize(fontSize, Unit.PX);
            // div.appendChild(span);
            // int w = span.getOffsetWidth() + getStyleExtraWidth();
            // int h = span.getOffsetHeight() + getStyleExtraHeight();
            // div.removeChild(span);
            dim = new GDimension(0, 0);
            GPElement clone = getPeer().cloneNode(true);
            clone.getStyle().clearWidth();
            clone.getStyle().clearHeight();
            div.appendChild(clone);
            GPElement label = clone.getChild(0);
            GPElement cb = label.getChild(0);
            GLabel gl = new GLabel(getText());
            GPElement text = label.getChild(1);
            dim.width = cb.getOffsetWidth() + text.getOffsetWidth();
            dim.height = label.getOffsetHeight() > gl.getSize().getHeight() ? label.getOffsetHeight()
                : gl.getSize().getHeight();
            div.removeChild(clone);
            setCachedPreferredSize(dim);
        }
        return dim;
    }
}
