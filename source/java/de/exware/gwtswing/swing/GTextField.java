package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPStyle;
import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPInputElement;
import de.exware.gplatform.style.GPStyleSheet;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.event.GKeyListener;

public class GTextField extends GTextComponent
{
    protected GPInputElement textElement;

    public GTextField(int length)
    {
        this(null,length);
    }
    
    public GTextField(String text)
    {
        this(text, 5);
    }

    protected GTextField(String text, int length)
    {
        super();
        textElement = createInputElement();
        textElement.addClassName("gwts-textInput");
        getPeer().appendChild(textElement);
        textElement.setSize(length);
        GFont font = getFont();
        setFont(textElement, font);
        setText(text);
    }

    @Override
    protected GPInputElement getInputElement()
    {
        return textElement;
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        textElement.setDisabled(!enabled);
    }
    
    @Override
    public void addKeyListener(GKeyListener listener)
    {
        super.addKeyListener(listener);
//        initEventListener(textElement, Event.ONKEYDOWN | Event.ONKEYUP | Event.ONKEYPRESS);
    }
    
    protected GPInputElement createInputElement()
    {
        return GPlatform.getDoc().createTextInputElement();        
    }
    
    @Override
    public void setFont(GFont font)
    {
        super.setFont(font);
        setFont(textElement, font);
    }
    
    @Override
    public String getText()
    {
        return textElement.getValue();
    }
    
    @Override
    public void setText(String text)
    {
        textElement.setValue(text);
        setCachedPreferredSize(null);
    }
    
    @Override
    public void setSize(int width, int height)
    {
        super.setSize(width, height);
        GPStyle style = textElement.getStyle();
        int pw = GPStyleSheet.getInt(".gwts-textInput", "padding-right")
            + GPStyleSheet.getInt(".gwts-textInput", "padding-left")
            + GPStyleSheet.getInt(".gwts-textInput", "border-left-width")
            + GPStyleSheet.getInt(".gwts-textInput", "border-right-width")
            ;
        width = width - pw;
        int ph = GPStyleSheet.getInt(".gwts-textInput", "padding-top")
            + GPStyleSheet.getInt(".gwts-textInput", "padding-bottom")
            + GPStyleSheet.getInt(".gwts-textInput", "border-top-width")
            + GPStyleSheet.getInt(".gwts-textInput", "border-bottom-width")
            ;
        height = height - ph;
        style.setWidth(width);
        style.setHeight(height);
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getCachedPreferredSize();
        if(dim == null)
        {
            GPElement div = GUtilities.getMeasureElement();
            GPInputElement span = GPlatform.getDoc().createTextInputElement();
            span.setValue(getText());
            float fontSize = getFont().getSize2D();
            String family = getFont().getFamily();
            span.getStyle().setFontSize(fontSize);
            span.getStyle().setProperty("fontFamily", family);
            span.setSize(textElement.getSize());
            span.getStyle().setBorderWidth(1);
            div.appendChild(span);
            int w = span.getOffsetWidth() + getStyleExtraWidth();
            int h = span.getOffsetHeight() + getStyleExtraHeight() + 3;
            div.removeChild(span);
            dim = new GDimension(w, h);
            setCachedPreferredSize(dim);
        }
        return dim;
    }

    @Override
    public int getCaretPosition()
    {
        return textElement.getPropertyInt("selectionStart");
    }
    
    @Override
    public void setCaretPosition(int pos)
    {
        textElement.setPropertyInt("selectionStart", pos);
    }

    @Override
    public void select(int selectionStart, int selectionEnd)
    {
        textElement.setPropertyInt("selectionStart", selectionStart);
        textElement.setPropertyInt("selectionEnd", selectionEnd);
    }
    
    @Override
    public void requestFocus()
    {
        GSwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                textElement.focus();
            }
        });
    }

    @Override
    public int getSelectionStart()
    {
        return textElement.getPropertyInt("selectionStart");
    }

    @Override
    public int getSelectionEnd()
    {
        return textElement.getPropertyInt("selectionEnd");
    }
}
