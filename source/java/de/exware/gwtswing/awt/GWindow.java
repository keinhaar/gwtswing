package de.exware.gwtswing.awt;

import de.exware.gplatform.GPlatform;
import de.exware.gwtswing.awt.event.GAWTEvent;
import de.exware.gwtswing.awt.event.GAWTEventListener;
import de.exware.gwtswing.awt.event.GActionEvent;
import de.exware.gwtswing.awt.event.GActionListener;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
import de.exware.gwtswing.swing.GButton;
import de.exware.gwtswing.swing.GComponent;
import de.exware.gwtswing.swing.GLabel;
import de.exware.gwtswing.swing.GPanel;
import de.exware.gwtswing.swing.GSwingUtilities;
import de.exware.gwtswing.swing.GUtilities;
import de.exware.gwtswing.swing.border.GBorderFactory;
import de.exware.gwtswing.swing.border.SelectiveLineBorder;

public class GWindow extends GComponent
{
    private GLabel title;
    private DragListener dragListener;
    private GPanel contentpane;
    private GPanel header;

    public GWindow()
    {
        super.setLayout(new GBorderLayout());
        setBorder(GBorderFactory.createLineBorder(GColor.DARK_GRAY, 3));
        header = new GPanel();
        GUtilities.insertClassNameBefore(header.getPeer(), "gwts-GPWindow-header");
        header.setLayout(new GBorderLayout());
        title = new GLabel("");
        GUtilities.insertClassNameBefore(title.getPeer(), "gwts-GPWindow-header-text");
        title.setOpaque(false);
        header.add(title, GBorderLayout.CENTER);
        GButton bt = new GButton("\u2715");
        header.add(bt, GBorderLayout.EAST);
        header.setBorder(new SelectiveLineBorder(GColor.DARK_GRAY, 0, 0, 1, 0));
        bt.addActionListener(new GActionListener()
        {
            @Override
            public void actionPerformed(GActionEvent evt)
            {
                setVisible(false);
            }
        });
        _add(header, GBorderLayout.NORTH);
        contentpane = new GPanel();
        contentpane.setLayout(new GGridLayout(1, 1));
        _add(contentpane, GBorderLayout.CENTER);
        dragListener = new DragListener();
        title.addMouseListener(dragListener);
        title.addMouseMotionListener(dragListener);
//        GToolkit.getDefaultToolkit().addAWTEventListener(dragListener, 0);
    }
    
    public GComponent getContentPane()
    {
        return contentpane;
    }
    
    protected void _add(GComponent comp, Object contraints)
    {
        super.addImpl(comp, contraints, -1);
    }
    
    public void setUndecorated(boolean b)
    {
        header.setVisible(!b);
    }
    
    @Override
    protected void addImpl(GComponent comp, Object constraints, int index)
    {
        getContentPane().add(comp, constraints, index);
    }
    
    @Override
    public void remove(GComponent comp)
    {
        getContentPane().remove(comp);
    }
    
    @Override
    public void setLayout(GLayoutManager layout)
    {
        getContentPane().setLayout(layout);
    }
    
    public void setLocationRelativeTo(GComponent c) 
    {
        GPoint pos = null;
        GDimension dim = null;
        if(c != null)
        {
            pos = c.getLocationOnScreen();
            dim = c.getSize();
        }
        else
        {
            pos = new GPoint();
            dim = new GDimension(GPlatform.getWin().getClientWidth(), GPlatform.getWin().getClientHeight());
        }
        int x = pos.x + (dim.width / 2);
        int y = pos.y + (dim.height / 2);
        GDimension dim2 = this.getSize();
        x -= dim2.width / 2;
        y -= dim2.height / 2;
        if(x < 0)
        {
            x = 0;
        }
        if(y < 0)
        {
            y = 0;
        }
        setLocation(x, y);
    }
    
    class DragListener extends GMouseAdapter
        implements GMouseMotionListener, GAWTEventListener
    {
        boolean dragging;
        int dragStartX, dragStartY;
        int mouseStartX, mouseStartY;
        
        @Override
        public void mousePressed(GMouseEvent e)
        {            
            GComponent comp = (GComponent) e.getSource();
            if(comp == title)
            {
                GPoint loc = getLocationOnScreen();
                GPoint cloc = comp.getLocationOnScreen();
                dragging = true;
                mouseStartX = e.getX() + cloc.x;
                mouseStartY = e.getY() + cloc.y;
                dragStartX = loc.x;
                dragStartY = loc.y;
            }
        }
        
        @Override
        public void mouseReleased(GMouseEvent e)
        {
            if(dragging)
            {
                dragging = false;
            }
        }

        @Override
        public void mouseMoved(GMouseEvent e)
        {
        }

        @Override
        public void eventDispatched(GAWTEvent event)
        {
            //Events outside of the title, because the mouse moved to fast.
            if(event.getId() == GMouseEvent.MOUSE_MOVED)
            {
                move((GMouseEvent) event);
            }
            else if(event.getId() == GMouseEvent.MOUSE_RELEASED)
            {
                mouseReleased((GMouseEvent) event);
            }
        }
        
        private void move(GMouseEvent e)
        {
            if(dragging)
            {
                GComponent comp = (GComponent) e.getSource();
                GPoint point = e.getPoint();
                point = GSwingUtilities.convertPoint(comp, point.x, point.y, title);
                GPoint loc = title.getLocationOnScreen();
                int dX = point.x + loc.x - mouseStartX;
                int dY = point.y + loc.y - mouseStartY;
                setLocation(dragStartX + dX, dragStartY + dY);
                GUtilities.clearSelection();
            }
        }
    }
    
    public void setTitle(String title)
    {
        this.title.setText(title);
    }
    
    @Override
    public void setVisible(boolean visible)
    {
        super.setVisible(visible);
        if(visible)
        {
            GPlatform.getDoc().getBody().appendChild(getPeer());
            GToolkit.getDefaultToolkit().addAWTEventListener(dragListener, GAWTEvent.MOUSE_EVENT_MASK);
            revalidate();
        }
        else
        {
            getPeer().removeFromParent();
            GToolkit.getDefaultToolkit().removeAWTEventListener(dragListener);
        }
//        GDimension dim = getSize();
//        setSize(0,0); //Correct size can only be calculated after adding the  component.
//        setSize(dim);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
    }
    
    public void pack()
    {
        boolean b = getPeer().getParentElement() == null;
        if(b)
        {
            GPlatform.getDoc().getBody().appendChild(getPeer());
        }
        GDimension dim = getPreferredSize();
        setSize(dim);
        if(b)
        {
            getPeer().removeFromParent();
        }
    }
    
    public void show()
    {
        setVisible(true);
    }
}
