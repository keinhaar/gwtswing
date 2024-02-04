package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GColor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.GGridBagConstraints;
import de.exware.gwtswing.awt.GGridBagLayout;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.awt.GToolkit;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.swing.border.GBorderFactory;

public class GPopupMenu extends GComponent
{
    private GGridBagConstraints gbc = new GGridBagConstraints();
    private GPopup popup;
    private static GPopupMenu visibleMenu;
    private static GAWTEventListener awtListener;
    private boolean visible;
    
    public GPopupMenu()
    {
        if(awtListener == null)
        {
            awtListener = new GAWTEventListener()
            {
                @Override
                public void eventDispatched(GAWTEvent event)
                {
                    if(visibleMenu != null 
                        && ((GMouseEvent)event).getClickCount() > 0
                        )
                    {
                        GComponent comp = (GComponent) event.getSource();
                        GComponent root = GSwingUtilities.getRoot(comp);
                        if(root != visibleMenu)
                        {
                            visibleMenu.setVisible(false);
                        }
                    }
                }
            };    
            GToolkit.getDefaultToolkit().addAWTEventListener(awtListener, GAWTEvent.MOUSE_EVENT_MASK);
        }
        setLayout(new GGridBagLayout());
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = gbc.HORIZONTAL;
        setBorder(GBorderFactory.createLineBorder(GColor.GRAY, 1));
    }
    
    public void add(GAction action)
    {
        GMenuItem item = new GMenuItem(action);
        add(item);
    }
    
    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        constraints = gbc;
        gbc.gridy++;
        super.addImpl(comp, constraints, index);
    }
    
    @Override
    public void setVisible(boolean visible)
    {
        this.visible = visible;
//        super.setVisible(visible);
        if(popup != null && visible == false)
        {
            popup.hide();
            popup = null;
            visibleMenu = null;
        }
        if(visible)
        {
            fireComponentShown();
        }
        else
        {
            fireComponentHidden();
        }
    }
    
    @Override
    public boolean isVisible()
    {
        return visible;
    }
    
    public void show(GComponent comp, int x, int y)
    {
        if(visibleMenu != null)
        {
            visibleMenu.setVisible(false);
        }
        setVisible(true);
        GPoint p = comp.getLocationOnScreen();
        popup = GPopupFactory.getSharedInstance().getPopup(comp, this, p.x + x, p.y + y);
        popup.show();
        GSwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                visibleMenu = GPopupMenu.this;
            }
        });
    }
    
    @Override
    public GDimension getPreferredSize()
    {
        return super.getPreferredSize();
    }
}
