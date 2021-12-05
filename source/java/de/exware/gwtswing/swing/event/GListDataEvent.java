package de.exware.gwtswing.swing.event;

import de.exware.gwtswing.util.GEventObject;

public class GListDataEvent
    extends GEventObject
{
    public static final int CONTENTS_CHANGED = 0;
    public static final int INTERVAL_ADDED = 1;
    public static final int INTERVAL_REMOVED = 2;

    private int type;
    private int minIndex;
    private int maxIndex;
    
    public GListDataEvent(Object source, int type, int minIndex, int maxIndex) 
    {
        super(source);
        this.type = type;
        this.minIndex = minIndex < maxIndex ? minIndex : maxIndex;
        this.maxIndex = maxIndex > minIndex ? maxIndex : minIndex;
    }
}
