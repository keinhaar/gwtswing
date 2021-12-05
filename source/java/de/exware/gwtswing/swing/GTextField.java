package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;
import de.exware.gwtswing.awt.event.GKeyListener;
import de.exware.gwtswing.lang.StyleSheet;

public class GTextField extends GTextComponent
{
    protected InputElement textElement;

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
    protected Element getInputElement()
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
    
    protected InputElement createInputElement()
    {
        return Document.get().createTextInputElement();        
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
        Style style = textElement.getStyle();
        int pw = StyleSheet.getInt(".gwts-textInput", "padding-right")
            + StyleSheet.getInt(".gwts-textInput", "padding-left")
            + StyleSheet.getInt(".gwts-textInput", "border-left-width")
            + StyleSheet.getInt(".gwts-textInput", "border-right-width")
            ;
        width = width - pw;
        int ph = StyleSheet.getInt(".gwts-textInput", "padding-top")
            + StyleSheet.getInt(".gwts-textInput", "padding-bottom")
            + StyleSheet.getInt(".gwts-textInput", "border-top-width")
            + StyleSheet.getInt(".gwts-textInput", "border-bottom-width")
            ;
        height = height - ph;
        style.setWidth(width, Unit.PX);
        style.setHeight(height, Unit.PX);
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getCachedPreferredSize();
        if(dim == null)
        {
            DivElement div = GUtilities.getMeasureElement();
            InputElement span = Document.get().createTextInputElement();
            span.setValue(getText());
            double fontSize = getFont().getSize2D();
            String family = getFont().getFamily();
            span.getStyle().setFontSize(fontSize, Unit.PX);
            span.getStyle().setProperty("fontFamily", family);
            span.setSize(textElement.getSize());
            span.getStyle().setBorderWidth(1, Unit.PX);
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
