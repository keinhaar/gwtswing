package de.exware.gwtswing.swing;

import de.exware.gwtswing.awt.GCursor;
import de.exware.gwtswing.awt.GDimension;
import de.exware.gwtswing.awt.event.GMouseAdapter;
import de.exware.gwtswing.awt.event.GMouseEvent;
import de.exware.gwtswing.awt.event.GMouseMotionListener;
import de.exware.gwtswing.awt.event.GTouchEvent;
import de.exware.gwtswing.awt.event.GTouchListener;

public class GSplitPane extends GComponent
{
    public static final String LEFT = "LEFT";
    public static final String RIGHT = "RIGHT";
    public static final int HORIZONTAL_SPLIT = 1;
    public static final int VERTICAL_SPLIT = 0;
    private int orientation = HORIZONTAL_SPLIT;
    private GComponent leftComponent;
    private GComponent rightComponent;
    private GPanel divider = new GPanel();
    private int dividerLocation = 100;
    private int dividerSize = 8;
    
    public GSplitPane()
    {
        this(HORIZONTAL_SPLIT);
    }
   
    public GSplitPane(int orientation)
    {
        this.orientation = orientation;
        divider.setBackground(GUIManager.getColor(".gwts-GComponent/background-color").darker());
        getPeer().appendChild(divider.getPeer());
        DividerListener dListener = new DividerListener();
        addMouseMotionListener(dListener);
        addMouseListener(dListener);
        addTouchListener(dListener);
        divider.getPeer().getStyle().setProperty("userSelect", "none");
        if(orientation == HORIZONTAL_SPLIT)
        {
            divider.setCursor(GCursor.HORIZONTAL_RESIZE_CURSOR);
        }
        else
        {
            divider.setCursor(GCursor.VERTICAL_RESIZE_CURSOR);
        }
        getPeer().getStyle().setProperty("userSelect", "none");
    }

    class DividerListener extends GMouseAdapter implements GMouseMotionListener, GTouchListener
    {
        boolean loaded = false;
        private int diff;
        
        @Override
        public void mouseReleased(GMouseEvent evt)
        {
            super.mouseReleased(evt);
            loaded = false;
        }
        
        @Override
        public void mousePressed(GMouseEvent evt)
        {
            super.mousePressed(evt);
            if(orientation == HORIZONTAL_SPLIT
                && evt.getX() >= dividerLocation 
                && evt.getX() <= dividerLocation + dividerSize)
            {
                loaded = true;
                diff = dividerLocation - evt.getX();
            }
            else if(orientation == VERTICAL_SPLIT
                && evt.getY() >= dividerLocation 
                && evt.getY() <= dividerLocation + dividerSize)
            {
                loaded = true;
                diff = dividerLocation - evt.getY();
            }
        }
        
        @Override
        public void mouseMoved(GMouseEvent e)
        {
            if(loaded)
            {
                GDimension size = getSize();
                int dividerLocation = 0;
                if(orientation == HORIZONTAL_SPLIT)
                {
                    dividerLocation = e.getX() + diff;
                    if(dividerLocation < 0) dividerLocation = 0;
                    else if(dividerLocation > size.width - dividerSize) dividerLocation = size.width - dividerSize;
                }
                else if(orientation == VERTICAL_SPLIT)
                {
                    dividerLocation = e.getY() + diff;
                    if(dividerLocation < 0) dividerLocation = 0;
                    else if(dividerLocation > size.height - dividerSize) dividerLocation = size.height - dividerSize;
                }
                setDividerLocation(dividerLocation);
            }
        }

        @Override
        public void touchStart(GTouchEvent evt)
        {
            if(orientation == HORIZONTAL_SPLIT
                && evt.getX() >= dividerLocation - dividerSize
                && evt.getX() <= dividerLocation + dividerSize*2)
            {
                loaded = true;
                diff = dividerLocation - evt.getX();
            }
            else if(orientation == VERTICAL_SPLIT
                && evt.getY() >= dividerLocation - dividerSize 
                && evt.getY() <= dividerLocation + dividerSize * 2)
            {
                loaded = true;
                diff = dividerLocation - evt.getY();
            }
        }

        @Override
        public void touchEnd(GTouchEvent evt)
        {
            loaded = false;
        }
        
        @Override
        public void touchMove(GTouchEvent e)
        {
            if(loaded && e.getX() >= 0)
            {
                GDimension size = getSize();
                int dividerLocation = 0;
                if(orientation == HORIZONTAL_SPLIT)
                {
                    dividerLocation = e.getX() + diff;
                    if(dividerLocation < 0) dividerLocation = 0;
                    else if(dividerLocation > size.width - dividerSize) dividerLocation = size.width - dividerSize;
                }
                else if(orientation == VERTICAL_SPLIT)
                {
                    dividerLocation = e.getY() + diff;
                    if(dividerLocation < 0) dividerLocation = 0;
                    else if(dividerLocation > size.height - dividerSize) dividerLocation = size.height - dividerSize;
                }
                setDividerLocation(dividerLocation);
            }
        }
    }
    
    @Override
    public void validate()
    {
        GDimension size = getSize();
        if(orientation == HORIZONTAL_SPLIT)
        {
            leftComponent.setSize(dividerLocation, size.height);
            rightComponent.setBounds(dividerLocation + dividerSize, 0
                , size.width - dividerLocation - dividerSize, size.height);
            divider.setBounds(dividerLocation, 0, dividerSize, size.height);
        }
        else
        {
            leftComponent.setSize(size.width, dividerLocation);
            rightComponent.setBounds(0, dividerLocation + dividerSize
                , size.width, size.height - dividerLocation - dividerSize);
            divider.setBounds(0, dividerLocation, size.width, dividerSize);
        }
        validateChildren();
    }
    
    @Override
    public String toString()
    {
        return super.toString() + ";";
    }

    public int getOrientation()
    {
        return orientation;
    }

    public void setOrientation(int orientation)
    {
        this.orientation = orientation;
    }

    public GComponent getLeftComponent()
    {
        return leftComponent;
    }

    public void setLeftComponent(GComponent leftComponent)
    {
        this.leftComponent = leftComponent;
        add(leftComponent);
    }

    public GComponent getRightComponent()
    {
        return rightComponent;
    }

    public void setRightComponent(GComponent rightComponent)
    {
        this.rightComponent = rightComponent;
        add(rightComponent);
    }

    public int getDividerLocation()
    {
        return dividerLocation;
    }

    public void setDividerLocation(int dividerLocation)
    {
        this.dividerLocation = dividerLocation;
        revalidate();
    }
}
