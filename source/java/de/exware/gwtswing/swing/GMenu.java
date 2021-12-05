package de.exware.gwtswing.swing;

import java.util.ArrayList;
import java.util.List;

import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;

public class GMenu extends GMenuItem
{
    private List<GMenuItem> items = new ArrayList<GMenuItem>();
    private GPopupMenu menu;
    
    public GMenu(String text)
    {
        super(text);
        addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                if(menu == null)
                {
                    showPopup();
                }
                else
                {
                    menu.setVisible(false);
                    menu = null;
                }
            }
        });
    }

    @Override
    public String toString()
    {
        return getClass().getName() + "; text=" + getText();
    }
    
    protected void showPopup()
    {
        menu = new GPopupMenu()
        {
            @Override
            public void setVisible(boolean visible)
            {
                super.setVisible(visible);
                if(visible == false)
                {
                    menu = null;
                }
            }
        };
        for(int i=0;i<items.size();i++)
        {
            menu.add(items.get(i));
        }
        GDimension dim = getSize();
        menu.show(this, 5, dim.height -2);
    }
    
    public GMenuItem add(GAction action)
    {
        GMenuItem item = new GMenuItem(action);
        items.add(item);
        return item;
    }

    public GMenuItem insert(GAction action, int pos)
    {
        GMenuItem item = new GMenuItem(action);
        items.add(pos,item);
        return item;
    }
}
