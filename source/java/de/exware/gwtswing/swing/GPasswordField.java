package de.exware.gwtswing.swing;

import de.exware.gplatform.GPlatform;
import de.exware.gplatform.element.GPInputElement;

public class GPasswordField extends GTextField
{
    public GPasswordField(String text)
    {
        super(text);
    }
    
    public GPasswordField(String text, String placeholder)
    {
    	super(text, placeholder);
    }

    public GPasswordField(int cols)
    {
        super(cols);
    }
    
    public GPasswordField(int cols, String placeholder)
    {
    	super(cols, placeholder);
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
