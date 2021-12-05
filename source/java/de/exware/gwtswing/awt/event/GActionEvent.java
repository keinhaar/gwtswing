package de.exware.gwtswing.awt.event;

public class GActionEvent extends GAWTEvent
{
    private String actionCommand;
    
    public GActionEvent(Object source)
    {
        this(source, null);
    }

    public GActionEvent(Object source, String actionCommand)
    {
        super(source);
        this.actionCommand = actionCommand;
    }

    public String getActionCommand()
    {
        return actionCommand;
    }
}
