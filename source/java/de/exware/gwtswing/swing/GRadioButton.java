package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.LabelElement;

import de.exware.gwtswing.awt.GDimension;

public class GRadioButton extends GAbstractButton
{
    private static int count;
    private LabelElement labelElement;
    private DivElement textElement;
    boolean selected;
    
    public GRadioButton(String text)
    {
        super(createElement());
        getPeer().removeChild(buttonElement);
        labelElement = Document.get().createLabelElement();
        textElement = Document.get().createDivElement();
        getPeer().appendChild(labelElement);
        labelElement.appendChild(buttonElement);
        labelElement.appendChild(textElement);
        setText(text);
    }
    
    private static InputElement createElement()
    {
        InputElement el = Document.get().createRadioInputElement("__internal_" + count++);
        return el;
    }
    
    void setButtonGroup(String name)
    {
        buttonElement.setAttribute("name", name);
    }
    
    @Override
    public void setText(String text)
    {
        textElement.setInnerHTML(text);
        setCachedPreferredSize(null);
    }
  
    @Override
    public boolean isSelected()
    {
        return selected;
    }
    
    @Override
    public void setSelected(boolean sel)
    {
        selected = sel;
        buttonElement.setChecked(sel);
        buttonElement.setDefaultChecked(sel);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        buttonElement.setDisabled(!enabled);
    }
    
    @Override
    public void validate()
    {
        super.validate();
        //Ugly Workaround for Radio Buttons, that wont display as selected, if
        //the RadioButton is not on a visible panel.
        setSelected(isSelected());
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
            DivElement div = GUtilities.getMeasureElement();
            dim = new GDimension(0, 0);
            Element clone = (Element) getPeer().cloneNode(true);
            clone.getStyle().clearWidth();
            clone.getStyle().clearHeight();
            div.appendChild(clone);
            Element label = (Element)clone.getChild(0);
            Element radio = (Element)label.getChild(0);
            Element text = (Element)label.getChild(1);
            dim.width = radio.getOffsetWidth() + text.getOffsetWidth();
            dim.height = label.getOffsetHeight() > text.getOffsetHeight() ? label.getOffsetHeight() : text.getOffsetHeight();
            div.removeChild(clone);
            setCachedPreferredSize(dim);
        }
        return dim;
    }
}
