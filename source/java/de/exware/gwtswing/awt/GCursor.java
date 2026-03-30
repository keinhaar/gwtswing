package de.exware.gwtswing.awt;

public enum GCursor
{
    DEFAULT_CURSOR("default")
    , CROSSHAIR_CURSOR("crosshair")
    , COLUMN_RESIZE_CURSOR("col-resize")
    , HORIZONTAL_RESIZE_CURSOR("ew-resize")
    , VERTICAL_RESIZE_CURSOR("ns-resize")
    , NORTHWEST_SOUTHEAST_RESIZE_CURSOR("nwse-resize")
    ;
    
    private String cursorName;
    
    private GCursor(String cursorName)
    {
        this.cursorName = cursorName;
    }

    public String getCursorName()
    {
        return cursorName;
    }

    public GCursor getByName(String name)
    {
        GCursor cursor = DEFAULT_CURSOR;
        for(GCursor c : values())
        {
            if(c.getCursorName().equals(name))
            {
                cursor = c;
                break;
            }
        }
        return cursor;
    }
}
