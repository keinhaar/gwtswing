package de.exware.gwtswing.swing;

abstract public class GTextComponent extends GComponent
{
    public GTextComponent()
    {
    }

    abstract public String getText();
    
    abstract public void setText(String text);
    
    abstract public int getCaretPosition();
    abstract public void setCaretPosition(int pos);
    
    abstract public int getSelectionStart();
    abstract public int getSelectionEnd();
    

    abstract public void select(int selectionStart, int selectionEnd);
}
