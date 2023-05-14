package de.exware.gwtswing.awt.event;

import de.exware.gplatform.event.GPEvent;

public class GKeyEvent extends GAWTEvent
{
    public static final int VK_BACK_SPACE     = '\b';
    public static final int VK_DELETE         = 46;
    public static final int VK_DOWN           = 0x28;
    public static final int VK_ENTER          = 0x0D;
    public static final int VK_LEFT           = 0x25;
    public static final int VK_RIGHT          = 0x27;
    public static final int VK_TAB            = '\t';
    public static final int VK_UP             = 0x26;
    public static final int VK_SPACE          = 0x20;
    public static final int VK_CONTROL        = 17;
    public static final int VK_SHIFT          = 16;
    
    public static final int VK_S              = 0x53;

    public static final int KEY_TYPED = 1;
    public static final int KEY_RELEASED = 2;
    public static final int KEY_PRESSED = 3;

    private boolean isShift;
    private boolean isAlt;
    private boolean isControl;
    private char character;
    private int keyCode;
    
    public GKeyEvent(Object source, GPEvent jsEvent)
    {
        super(source); 
        isShift = jsEvent.getShiftKey();
        isAlt = jsEvent.getAltKey();
        isControl = jsEvent.getCtrlKey();
        keyCode = jsEvent.getKeyCode();
        if(jsEvent.getType() == GPEvent.Type.ONKEYPRESS)
        {
            character = (char)jsEvent.getCharCode();
            setId(KEY_TYPED);
        }
        else if(jsEvent.getType() == GPEvent.Type.ONKEYUP)
        {
            setId(KEY_RELEASED);
        }
        else if(jsEvent.getType() == GPEvent.Type.ONKEYDOWN)
        {
            setId(KEY_PRESSED);
        }
    }
    
    public char getKeyChar()
    {
        return character;
    }
    
    public int getKeyCode()
    {
        return keyCode;
    }
    
    public boolean isControlDown()
    {
        return isControl;
    }
    
    public boolean isShiftDown()
    {
        return isShift;
    }
    
    public boolean isAltDown()
    {
        return isAlt;
    }
}
