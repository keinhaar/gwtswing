package de.exware.gwtswing.swing;

import com.google.gwt.dom.client.BodyElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Unit;

import de.exware.gwtswing.awt.GDimension;

public class GPopup
{
    private DivElement popup;
    private GComponent owner;
    private GComponent contents;
    
    GPopup(GComponent owner, GComponent contents, int x, int y)
    {
        popup = Document.get().createDivElement();
        popup.addClassName("gwts-GPopup");
//        popup.getStyle().setZIndex(Integer.MAX_VALUE);
        popup.getStyle().setLeft(x, Unit.PX);
        popup.getStyle().setTop(y, Unit.PX);
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
        BodyElement body = Document.get().getBody();
        body.appendChild(popup);
        GDimension dim = contents.getPreferredSize();
        contents.setBounds(0, 0, dim.getWidth(), dim.getHeight());
        contents.validate();
        popup.getStyle().setWidth(dim.getWidth(), Unit.PX);
        popup.getStyle().setHeight(dim.getHeight(), Unit.PX);
    }
}
