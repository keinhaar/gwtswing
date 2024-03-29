package de.exware.gwtswing.awt.event;

import de.exware.gplatform.event.GPEvent;
import de.exware.gwtswing.awt.GInsets;
import de.exware.gwtswing.awt.GPoint;
import de.exware.gwtswing.swing.GComponent;

public class GMouseEvent extends GAWTEvent
{
    public static final int MOUSE_MOVED = 1;
    public static final int MOUSE_PRESSED = 2;
    public static final int MOUSE_RELEASED = 3;
    public static final int MOUSE_CLICKED = 4;
    public static final int MOUSE_ENTERED = 5;
    public static final int MOUSE_EXITED = 6;
    public static final int MOUSE_WHEEL = 7;
    public static final int MOUSE_DRAGGED = 8;
    public static final int MOUSE_LONG_CLICK = 9;
    public static final int NOBUTTON = 0;
    public static final int BUTTON1 = 1;
    public static final int BUTTON2 = 2;
    public static final int BUTTON3 = 3;
    private int x, y;
    private boolean isShift;
    private boolean isAlt;
    private boolean isControl;
    private int mousebutton;
    private int clickCount;
    
    public GMouseEvent(Object source, int x, int y, int clickcount, int button)
    {
        super(source);
        this.x = x;
        this.y = y;
        this.clickCount = clickcount;
        this.mousebutton = button;
    }
    
    public GMouseEvent(Object source, GPEvent jsEvent)
    {
        this(source, jsEvent, 0);
    }
    
    public GMouseEvent(Object source, GPEvent jsEvent, boolean stopPropagation)
    {
        this(source, jsEvent, 0, stopPropagation);
    }
    
    public GMouseEvent(Object source, GPEvent jsEvent, int clickCount, boolean stopPropagation)
    {
        this(source, jsEvent, stopPropagation, clickCount, jsEvent.getButton() == GPEvent.Button.BUTTON_LEFT ? BUTTON1
            : jsEvent.getButton() == GPEvent.Button.BUTTON_MIDDLE ? BUTTON2 : BUTTON3);
    }
    
    public GMouseEvent(Object source, GPEvent jsEvent, int clickCount)
    {
        this(source, jsEvent, true, clickCount, jsEvent.getButton() == GPEvent.Button.BUTTON_LEFT ? BUTTON1
            : jsEvent.getButton() == GPEvent.Button.BUTTON_MIDDLE ? BUTTON2 : BUTTON3);
    }
    
    public GMouseEvent(Object source, GPEvent jsEvent, boolean stopPropagation, int clickCount, int button)
    {
        super(source);
        if(stopPropagation)
        {
            jsEvent.stopPropagation();
        }
        this.mousebutton = button;
        GComponent c = (GComponent) source;
        GPoint loc = c.getLocationOnScreen();
        GInsets insets = c.getInsets();
        x = jsEvent.getClientX() - loc.x - insets.left;
        y = jsEvent.getClientY() - loc.y - insets.top;
        isShift = jsEvent.getShiftKey();
        isAlt = jsEvent.getAltKey();
        isControl = jsEvent.getCtrlKey();
        switch(jsEvent.getType())
        {
            case ONMOUSEMOVE:
                setId(MOUSE_MOVED);
            break;
            case ONMOUSEUP:
                setId(MOUSE_RELEASED);
            break;
            case ONMOUSEDOWN:
                setId(MOUSE_PRESSED);
            break;
            case ONCLICK:
                setId(MOUSE_CLICKED);
            break;
            case ONMOUSEWHEEL:
                setId(MOUSE_WHEEL);
            break;
            case ONMOUSEOVER:
                setId(MOUSE_ENTERED);
            break;
            case ONMOUSEOUT:
                setId(MOUSE_EXITED);
            break;
        }
        this.clickCount = clickCount;
    }

    public boolean isControlDown()
    {
        return isControl;
    }
    
    public int getButton()
    {
        return mousebutton;
    }
    
    public boolean isShiftDown()
    {
        return isShift;
    }
    
    public boolean isAltDown()
    {
        return isAlt;
    }
    
    public int getX()
    {
        return x;
    }
    
    public int getY()
    {
        return y;
    }

    public int getClickCount()
    {
        return clickCount;
    }
    
    public GPoint getPoint()
    {
        return new GPoint(x, y);
    }
}
