package de.exware.gwtswing.swing;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPInputElement;

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
    protected GPInputElement createInputElement()
    {
        return GPlatform.getDoc().createPasswordInputElement();      
    }

    public char[] getPassword()
    {
        return getText().toCharArray();
    }
}
