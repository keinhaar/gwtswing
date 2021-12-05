package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.InputElement;

public class GPasswordField extends GTextField
{
    public GPasswordField(String text)
    {
        super(text);
    }

    public GPasswordField(int cols)
    {
        super(cols);
    }

    @Override
    protected InputElement createInputElement()
    {
        return Document.get().createPasswordInputElement();      
    }

    public char[] getPassword()
    {
        return getText().toCharArray();
    }
}
