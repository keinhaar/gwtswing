package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TextAreaElement;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GFont;

public class GTextArea extends GTextComponent
{
    protected TextAreaElement textElement;

    public GTextArea(String text)
    {
        this(text, 4, 20);
    }

    public GTextArea(String text, int rows, int columns)
    {
        super();
        textElement = Document.get().createTextAreaElement();
        setPeer(textElement);
        setClassNames(textElement, "");
//      getPeer().appendChild(textElement);
        textElement.setCols(columns);
        textElement.setRows(rows);
        setFont(textElement, getFont());
        setText(text);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        textElement.setDisabled(!enabled);
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
//        width -= getStyleExtraWidth();
//        height -= getStyleExtraHeight();
//        if(width < 0) width = 0;
//        if(height < 0) height = 0;
//        Style style = textElement.getStyle();
//        style.setLeft(x, Unit.PX);
//        style.setTop(y, Unit.PX);
//        style.setWidth(width, Unit.PX);
//        style.setHeight(height, Unit.PX);
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
        if(text == null)
        {
            text = "";
        }
        textElement.setValue(text);
        setCachedPreferredSize(null);
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        GDimension dim = getCachedPreferredSize();
        if(dim == null)
        {
            DivElement div = GUtilities.getMeasureElement();
            TextAreaElement span = Document.get().createTextAreaElement();
            span.setCols(textElement.getCols());
            span.setRows(textElement.getRows());
            span.setValue(getText());
            double fontSize = getFont().getSize2D();
            String family = getFont().getFamily();
            span.getStyle().setFontSize(fontSize, Unit.PX);
            span.getStyle().setProperty("fontFamily", family);
//            span.setSize(textElement.getStyle().getWidth());
            div.appendChild(span);
            int w = span.getOffsetWidth() + getStyleExtraWidth();
            int h = span.getOffsetHeight() + getStyleExtraHeight();
            div.removeChild(span);
            dim = new GDimension(w, h);
            setCachedPreferredSize(dim);
        }
        return dim;
    }

//    @Override
//    public GDimension getPreferredSize()
//    {
//        Element peer = getPeer();
//        PixelStyle pstyle = GUtilities.getComputedStyle(peer);
//        DivElement div = GUtilities.getMeasureElement();
//        InputElement span = Document.get().createTextInputElement();
//        span.setValue(getText());
//        double fontSize = pstyle.getFontSize();
//        span.getStyle().setFontSize(fontSize, Unit.PX);
//        span.setSize(5);
//        div.appendChild(span);
//        int w = span.getOffsetWidth() + getStyleExtraWidth(pstyle);
//        int h = span.getOffsetHeight() + getStyleExtraHeight(pstyle);
//        div.removeChild(span);
//        return new GDimension(w, h);
//    }


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
