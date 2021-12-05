package de.exware.gwtswing.swing;

public class GPopupFactory
{
    private static GPopupFactory instance;
    
    protected GPopupFactory()
    {}
    
    public static GPopupFactory getSharedInstance()
    {
        if(instance == null)
        {
            instance = new GPopupFactory();
        }
        return instance;
    }
    
    public GPopup getPopup(GComponent owner, GComponent contents,
        int x, int y) throws IllegalArgumentException
    {
        if(contents == null)
        {
            throw new IllegalArgumentException("contents must not be null.");
        }
        GPopup popup = new GPopup(owner,contents,x,y);
        return popup;
    }
}
