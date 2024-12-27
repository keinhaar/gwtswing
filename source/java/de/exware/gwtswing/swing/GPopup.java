package de.exware.gwtswing.swing;

import de.exware.gplatform.GPElement;
import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.GDimension;

public class GPopup
{
    private GPElement popup;
    private GComponent owner;
    private GComponent contents;
    
    GPopup(GComponent owner, GComponent contents, int x, int y)
    {
        popup = GPlatform.getDoc().createDivElement();
        popup.addClassName("gwts-GPopup");
//        popup.getStyle().setZIndex(Integer.MAX_VALUE);
        GDimension dim = contents.getPreferredSize();
        if(x + dim.width > GPlatform.getWin().getClientWidth())
        {
            x = GPlatform.getWin().getClientWidth() - dim.width;
        }
        if(y + dim.height > GPlatform.getWin().getClientHeight())
        {
            y = GPlatform.getWin().getClientHeight() - dim.height;
        }
        if(x < 0)
        {
            x = 0;
        }
        if(y < 0)
        {
            y = 0;
        }
        popup.getStyle().setLeft(x);
        popup.getStyle().setTop(y);
        this.owner = owner;
        this.contents = contents;
    }
    
    public void hide()
    {
        popup.removeFromParent();
    }

    public void show()
    {
        GUtilities.addToWidget(popup, contents);
        GPElement body = GPlatform.getDoc().getBody();
        body.appendChild(popup);
        GDimension dim = contents.getPreferredSize();
        contents.setBounds(0, 0, dim.getWidth(), dim.getHeight());
        contents.validate();
        popup.getStyle().setWidth(dim.getWidth());
        popup.getStyle().setHeight(dim.getHeight());
    }
}
