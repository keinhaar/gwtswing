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
        popup.getStyle().setLeft(x);
        popup.getStyle().setTop(y);
        popup.getStyle().setBackgroundColor("#ff0000");
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
